/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package org.araneaframework.http.filter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import net.iharder.base64.Base64;
import org.apache.commons.collections.Buffer;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.framework.FilterWidget;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.ClientStateContext;
import org.araneaframework.http.util.EncodingUtil;

/**
 * A filter providing saving the state on the client side. On every render
 * the descendent of the filter is serialized and written out to the client.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author "Jevgeni Kabanov" <ekabanov@webmedia.ee>
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandardClientStateFilterWidget extends BaseFilterWidget implements FilterWidget, ClientStateContext {
  private static final Log log = LogFactory.getLog(StandardClientStateFilterWidget.class);
  public static final int MAX_STORED_STATES = 10;
  
  private boolean serverSideStorage = false;
  private boolean compress = false;
  
  private Buffer digestSet;
  private Map states;
  private int maxStoredStates = 0;
  
  {
    if (maxStoredStates == 0)
      maxStoredStates = MAX_STORED_STATES;

    digestSet = new CircularFifoBuffer(maxStoredStates);
    states = new LRUMap(maxStoredStates);
  }
  
  public void setServerSideStorage(boolean serverSideStorage) {
    this.serverSideStorage = serverSideStorage;
  }

  private void refreshClientState(InputData input) throws Exception {
    if (childWidget == null) {
      String state = isServerSideStorage() ? 
          (String)states.get(getStateVersionFromInput(input)) : (String)input.getGlobalData().get(CLIENT_STATE);  

      byte[] lastDigest = Base64.decode(getStateVersionFromInput(input)+"");
      
      // probably some unpleasant refresh. When client-side storage, there is nothing much we can do.
      // When server-side storage -- use the latest state 
      if (lastDigest == null) {
        if (serverSideStorage) {
         String mostRecentDigest = Base64.encodeBytes((byte[])digestSet.get(), Base64.DONT_BREAK_LINES);
         state = (String) states.get(mostRecentDigest);
        } else {
          // TODO: recover and show at least some information to the user
        }
      }

      if (!digestSet.contains(new Digest(lastDigest))) {
        // this is most likely the case when session has expired
        throw new SecurityException("Invalid session digest -- '" + Base64.encodeBytes(lastDigest, Base64.DONT_BREAK_LINES) + "'!");
      } else {
        // figure out which position the digestSet has
        if (!digestSet.get().equals(new Digest(lastDigest))) {
          int j = 0;
          for (Iterator i = digestSet.iterator(); i.hasNext(); ) {
            if (i.next().equals(new Digest(lastDigest)))
              log.debug("---------------- Client side navigation to cached page #" + j + " detected.");
            j++;
          }
        }
      }

      if (!EncodingUtil.checkDigest(state.getBytes(), lastDigest)) {
        // probably an evil hacker :)
        throw new SecurityException("Invalid session state!");
      }

      childWidget = (RelocatableWidget)EncodingUtil.decodeObjectBase64(state, compress);
      ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(getChildWidgetEnvironment());
    }
  }

  private String getStateVersionFromInput(InputData input) {
    return (String)input.getGlobalData().get(CLIENT_STATE_VERSION);
  }

  protected void update(InputData input) throws Exception {
    refreshClientState(input);
    super.update(input);
  }

  protected void render(OutputData output) throws Exception {
    refreshClientState(output.getInputData());
    
    // state changes in render (rendered flags for formelements)
    super.render(output);
    
    childWidget = null;
  }
  
  public State registerState() {
    try {
      return internalRegisterState();
    } catch (Exception e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }
  
  public boolean isServerSideStorage() {
    return serverSideStorage;
  }

  protected State internalRegisterState() throws Exception {
    Environment env = ((RelocatableWidget) childWidget)._getRelocatable().getCurrentEnvironment();
    
    ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(null);

    String base64 = EncodingUtil.encodeObjectBase64(this.childWidget, compress);

    byte[] lastDigest = EncodingUtil.buildDigest(base64.getBytes());

    String clientStateVersion = Base64.encodeBytes(lastDigest, Base64.DONT_BREAK_LINES);
    digestSet.add(new Digest(lastDigest));

    ((RelocatableWidget) childWidget)._getRelocatable().overrideEnvironment(env);

    if (isServerSideStorage())
      states.put(clientStateVersion, base64);
    
    if (log.isDebugEnabled()) {
      log.debug("Registered client state version: " + clientStateVersion);
    }

    return new State(base64, clientStateVersion);
  }

  /**
   * Sets the child to childWidget.
   */
  public void setChildWidget(Widget childWidget) {
    this.childWidget = new RelocatableDecorator(childWidget);
  }

  private static class Digest implements Serializable {
    private byte[] digest;

    public Digest(byte[] digest) {
      this.digest = digest;
    }

    public byte[] getDigest() {
      return digest;
    }

    public boolean equals(Object obj) {
      return Arrays.equals(digest, ((Digest) obj).getDigest());
    }

    public int hashCode() {
      int result = 37;
      for (int i = 0; i < digest.length; i++) {
        result += 11 * digest[i];
      }
      return result;			
    }
  }

  public void addClientNavigationListener(ClientNavigationListener listener) {
    
  }

  public void removeClientNavigationListener(ClientNavigationListener listener) {
    
  }

  /**
   * If true, the serialized state will also be GZIP'ed.
   * @param compress
   */
  public void setCompress(boolean compress) {
    this.compress = compress;
  }

  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), ClientStateContext.class, this);
  }
}
