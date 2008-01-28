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

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.FlowEventConfirmationContext;
import org.araneaframework.framework.FlowEventConfirmationContext.ConfirmationCondition;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandardFlowEventConfirmationHandler implements FlowEventConfirmationContext.FlowEventConfirmationHandler {
  private static final long serialVersionUID = 1L;
  private ConfirmationCondition conditionProvider;
  private Closure onConfirm;
  private Closure doConfirm;

  public StandardFlowEventConfirmationHandler(ConfirmationCondition conditionProvider) {
    setConfirmationCondition(conditionProvider);
  }

  public void setConfirmationCondition(ConfirmationCondition conditionProvider) {
    this.conditionProvider = conditionProvider;   
  }

  public void setOnConfirm(Closure onConfirm) {
    Assert.isInstanceOf(Serializable.class, onConfirm, "onConfirm Closure must implement java.io.Serializable");
    this.onConfirm = onConfirm;
  }

  public Closure getOnConfirm() {
    return this.onConfirm;
  }

  public void setDoConfirm(Closure doConfirm) {
    Assert.isInstanceOf(Serializable.class, doConfirm, "doConfirm Closure must implement java.io.Serializable");
    this.doConfirm = doConfirm;
  }

  public Closure getDoConfirm() {
    return doConfirm;
  }

  public ConfirmationCondition getConfirmationCondition() {
    return conditionProvider;
  }
}