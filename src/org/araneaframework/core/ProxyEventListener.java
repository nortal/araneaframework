/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

package org.araneaframework.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ProxiedHandlerUtil;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class ProxyEventListener implements EventListener {

  private static final long serialVersionUID = 1L;

  public static final Log log = LogFactory.getLog(ProxyEventListener.class);

  protected Widget eventTarget;

  public ProxyEventListener(Widget eventTarget) {
    this.eventTarget = eventTarget;
  }

  public void processEvent(String eventId, InputData input) throws Exception {
    String eventParam = (String) input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);
    ProxiedHandlerUtil.invokeEventHandler(eventId, eventParam, eventTarget);
  }
}
