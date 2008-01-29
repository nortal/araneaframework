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

package org.araneaframework.uilib.flowcontext.transitionhandler;

import org.apache.commons.collections.Closure;
import org.araneaframework.InputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.EventListener;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.framework.ConfirmationContext;

/**
 * {@link EventListener} which executes the given closure when end-user input
 * confirms that previously cached event should take place.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class UserTransitionConfirmationListener extends StandardEventListener {
  private static final long serialVersionUID = 1L;
  private BaseApplicationWidget flow;
  private Closure transition;
  
  public UserTransitionConfirmationListener(BaseApplicationWidget flow, Closure transition) {
    this.flow = flow;
    this.transition = transition;
  }

  public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
    ConfirmationContext ctx = (ConfirmationContext) flow.getEnvironment().requireEntry(ConfirmationContext.class);
    if ("true".equalsIgnoreCase(eventParam)) {
      transition.execute(flow);
      ctx.setConfirmation(null, null);
      flow.removeEventListener(this);
    } else if ("false".equalsIgnoreCase(eventParam)) {
      ctx.setConfirmation(null, null);
      flow.removeEventListener(this);
    }
  }
}