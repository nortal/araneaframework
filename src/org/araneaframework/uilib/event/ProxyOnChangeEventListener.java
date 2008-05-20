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

package org.araneaframework.uilib.event;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ProxiedHandlerUtil;


/**
 * An implementation of <code>OnChangeEventListener</code> that does not
 * require to be sublassed, but instead allows registering a
 * <code>handleEvent*()</code> method of given widget that will be called when
 * the change occurs.
 * <p>
 * It is quite easy to use compared to usual {@link OnChangeEventListener}:
 * 
 * <pre><code>
 * control.addOnChangeEventListener(new ProxyOnChangeEventListener(this, &quot;magic&quot;));
 * ...
 * public void handleEventMagic() throws Exception {
 *   ...
 * }
 * </code></pre>
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @see ProxyOnClickEventListener
 */
public class ProxyOnChangeEventListener implements OnChangeEventListener {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(ProxyOnChangeEventListener.class);

  /**
   * The widget where the event handler will be invoked.
   */
  protected Widget eventTarget;

  /**
   * The name of the event handler that will be invoked. The target widget is
   * expected to have a method like this:
   * 
   * <pre><code>
   * public void handleEvent[eventId](String param) throws Exception
   * </code></pre>
   */
  protected String eventId;

  /**
   * A constructor that initializes the event listener with the given target
   * widget (<code>eventTarget</code>) and event handler name (<code>eventId</code>).
   * The target widget is expected to have a method like this:
   * 
   * <pre><code>
   * public void handleEvent[eventId](String param) throws Exception
   * </code></pre>
   * 
   * @param eventTarget The widget that contains the event handling method.
   * @param eventId The name of the event handler.
   */
  public ProxyOnChangeEventListener(Widget eventTarget, String eventId) {
    this.eventTarget = eventTarget;
    this.eventId = eventId;
  }

  public void onChange() throws Exception {
    Method eventHandler;

    // lets try to find a handle method with an empty argument
    try {
      eventHandler = ProxiedHandlerUtil.getEventHandler(eventId, eventTarget);
      eventHandler.invoke(eventTarget, new Object[] {});
      return;
    } catch (NoSuchMethodException e) {/* OK */}

    // lets try to find a method with a String type argument
    try {
      eventHandler = ProxiedHandlerUtil.getEventHandler(eventId, eventTarget,
          new Class[] { String.class });
      eventHandler.invoke(eventTarget, new Object[] { null });
      return;
    } catch (NoSuchMethodException e) {/* OK */}

    if (log.isWarnEnabled()) {
      log.warn("Widget '" + eventTarget.getScope() + "' cannot deliver "
          + "event as no event listeners were registered for the event id '"
          + eventId + "'!" + Assert.thisToString(eventTarget));
    }
  }

}
