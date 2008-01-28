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

package org.araneaframework.framework.container;

import org.apache.commons.collections.Predicate;
import org.araneaframework.framework.FlowEventConfirmationContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class StandardFlowEventConfirmationContextImpl implements FlowEventConfirmationContext {
  private static final long serialVersionUID = 1L;
  private FlowEventConfirmationHandler handler;

  public void setFlowEventConfirmationHandler(FlowEventConfirmationHandler handler) {
    this.handler = handler;
  }
  
  public FlowEventConfirmationHandler getFlowEventConfirmationHandler() {
    return this.handler;
  }

  /** @since 1.1 */
  public static class NoopConfirmationCondition implements FlowEventConfirmationContext.ConfirmationCondition {
    private static final long serialVersionUID = 1L;
    public static final NoopConfirmationCondition INSTANCE = new NoopConfirmationCondition();

    public Predicate getCancelPredicate() {
      return null;
    }

    public Predicate getFinishPredicate() {
      return null;
    }

    public Predicate getReplacePredicate() {
      return null;
    }

    public Predicate getResetPredicate() {
      return null;
    }

    public Predicate getStartPredicate() {
      return null;
    }
  }
}
