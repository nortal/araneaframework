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

package org.araneaframework.uilib.form.constraint;

import org.araneaframework.uilib.form.FormElementContext;

public abstract class BaseFieldConstraint extends BaseConstraint {
  public BaseFieldConstraint() {
  }
	
  protected FormElementContext getField() {
    return (FormElementContext)getEnvironment().requireEntry(FormElementContext.class);
  }
  
  protected String getLabel() {
    return getField().getLabel();
  }

  protected Object getValue() {
    return getField().getValue();
  }
  
  public boolean isRead() {
    return getField().isRead();
  }
  
  public boolean isDisabled() {
    return getField().isDisabled();
  }

  public boolean isMandatory() {
    return getField().isMandatory();
  }
}
