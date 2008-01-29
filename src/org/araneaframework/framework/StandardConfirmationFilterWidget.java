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
package org.araneaframework.framework;

import org.araneaframework.Environment;
import org.araneaframework.Scope;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class StandardConfirmationFilterWidget extends BaseFilterWidget implements ConfirmationContext {
  private static final long serialVersionUID = 1L;
  private String message;
  private Scope scope;
  
  public void setConfirmation(Scope confirmationScope, String confirmationMessage) {
    this.scope = confirmationScope;
    this.message = confirmationMessage;
  }

  public String getConfirmationMessage() {
    return message;
  }

  public Scope getConfirmationScope() {
    return scope;
  }

  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), ConfirmationContext.class, this);
  }
}
