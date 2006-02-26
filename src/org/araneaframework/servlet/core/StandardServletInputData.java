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

package org.araneaframework.servlet.core;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.EmptyPathStackException;
import org.araneaframework.core.NoCurrentOutputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.StandardPath;
import org.araneaframework.servlet.ServletInputData;

/**
 * A ServletInputdata implementation which uses a StandardPath for determining
 * the scope.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletInputData implements ServletInputData {
  private HttpServletRequest req;
  private StringBuffer scopeBuf = new StringBuffer();
  private Map extensions = new HashMap();
  
  private Map globalData = new HashMap();
  private Map scopedData = new HashMap();
  
  /**
   * Constructs a StandardServletInputData from the request. 
   * @param request
   */
  public StandardServletInputData(HttpServletRequest request) {
    req = request;
    Enumeration params = req.getParameterNames();
    
    while (params.hasMoreElements()) {
      String key = (String)params.nextElement();
      
      if (key.lastIndexOf(".") == -1) {
        //global data - no prefix data
        globalData.put(key, req.getParameter(key));
      }
      else {
        //scoped data
        String prefix = key.substring(0, key.lastIndexOf("."));
        String subKey = key.substring(key.lastIndexOf(".") + 1);
        
        Map map = (Map)scopedData.get(prefix);
        
        if (map == null) {
          map = new HashMap();
          scopedData.put(prefix, map);
        }

        map.put(subKey, req.getParameter(key));
      }
    }
  }

  public Path getScope() {
    return new StandardPath(scopeBuf.toString());
  }

  public void pushScope(Object step) {
    if (scopeBuf.length()>0) {
      scopeBuf.append("."+step);
    }
    else {
      scopeBuf.append(step);
    }
  }

  public void popScope() {
    if (scopeBuf.toString().lastIndexOf(".") != -1) {
      scopeBuf.setLength(scopeBuf.toString().lastIndexOf("."));
    }
    else {
      if (scopeBuf.length()==0) {
        throw new EmptyPathStackException();
      }
      scopeBuf.setLength(0);
    }
  }
  
  public Map getGlobalData() {    
    return Collections.unmodifiableMap(globalData);
  }

  public Map getScopedData() {
    Map result = (Map)scopedData.get(scopeBuf.toString());
    if (result != null) {
      return Collections.unmodifiableMap(result);  
    }
    else {
      return Collections.unmodifiableMap(new HashMap());
    }
  }

  public HttpServletRequest getRequest() {
    return this.req;
  }

  public void extend(Class interfaceClass, Object implementation) {
    extensions.put(interfaceClass, implementation);
  }

  public Object narrow(Class interfaceClass) {
    Object extension = extensions.get(interfaceClass); 
    if (extension == null)
      throw new NoSuchNarrowableException(interfaceClass);
    return extension;
  }

	public OutputData getCurrentOutputData() {
		OutputData output = (OutputData)req.getAttribute(StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
		if (output == null)
			throw new NoCurrentOutputDataSetException("No OutputData set in the request.");
		else
			return output;
	}
}
