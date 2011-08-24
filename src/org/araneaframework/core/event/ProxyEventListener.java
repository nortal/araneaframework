/*
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
 */

package org.araneaframework.core.event;

import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * Event listener that proxies incoming events (where this listener is registered) to the provided event target, calling
 * its <code>handleEvent*</code> methods. The logic of calling these methods is actually contained in
 * {@link ProxiedHandlerUtil#invokeEventHandler(String, String, Widget)}.
 * <p>
 * Note that this event listener can be used in a widget globally and/or with scoped events:
 * <ul>
 * <li><code>setGlobalEventListener(new ProxyEventListener(this));</code>
 * <li>
 * 
 * <code>setEventListener("myEvent", new ProxyEventListener(this));</br>
 * public void handleEventMyEvent() { ... }</code>
 * </ul>
 * 
 * @see ProxiedHandlerUtil
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public final class ProxyEventListener extends StandardEventListener {

  private final Widget eventTarget;

  public ProxyEventListener(Widget eventTarget) {
    Assert.notNullParam(eventTarget, "eventTarget");
    this.eventTarget = eventTarget;
  }

  @Override
  protected void processEvent(String eventId, String eventParam, InputData input) {
    ProxiedHandlerUtil.invokeEventHandler(eventId, eventParam, this.eventTarget);
  }
}
