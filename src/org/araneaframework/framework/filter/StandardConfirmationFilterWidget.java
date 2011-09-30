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

package org.araneaframework.framework.filter;

import java.io.Serializable;
import org.apache.commons.collections.Closure;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * Filter widget implementing confirmation context support. This service also exposes {@link ConfirmationContext}to
 * child components through environment.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class StandardConfirmationFilterWidget extends BaseFilterWidget implements ConfirmationContext {

  private Closure closure;

  private String message;

  /**
   * {@inheritDoc}
   */
  public void confirm(Closure onConfirmClosure, String message) {
    Assert.isInstanceOfParam(Serializable.class, onConfirmClosure, "onConfirmClosure");
    Assert.notNullParam(this, message, "message");
    this.closure = onConfirmClosure;
    this.message = message;
  }

  // IMPLEMENTATION

  /**
   * {@inheritDoc}
   */
  public String getConfirmationMessage() {
    return this.message;
  }

  // IMPLEMENTATION

  /**
   * Resets confirmation context, removing the confirmation message an callback closure.
   */
  protected void removeConfirmation() {
    this.closure = null;
    this.message = null;
  }

  /**
   * Provides whether the confirmation context is active, i.e. a confirmation message/question exists for the user.
   * 
   * @return A Boolean that is <code>true</code> when this confirmation context is active.
   */
  protected boolean isActive() {
    return this.closure != null;
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
    if (isActive()) {
      String confirmationResult = input.getGlobalData().get(ConfirmationContext.CONFIRMATION_RESULT_KEY);

      if ("true".equalsIgnoreCase(confirmationResult)) {
        this.closure.execute(null);
        removeConfirmation();
      } else if ("false".equalsIgnoreCase(confirmationResult)) {
        removeConfirmation();
      }
    }

    super.event(path, input);
  }

  @Override
  protected void render(OutputData output) throws Exception {
    SystemFormContext systemFormContext = EnvironmentUtil.requireSystemFormContext(getEnvironment());
    systemFormContext.addField(ConfirmationContext.CONFIRMATION_RESULT_KEY, "");
    super.render(output);
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), ConfirmationContext.class, this);
  }
}
