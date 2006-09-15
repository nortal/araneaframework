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

import org.araneaframework.Environment;
import org.araneaframework.uilib.form.Constraint;

/**
 * Constraint that will be applied iff the field has been read from the request.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class OptionalConstraint extends BaseFieldConstraint {
  private Constraint constraint;

  public OptionalConstraint(Constraint constraint) {
    this.constraint = constraint;
  }
  
  protected void validateConstraint() throws Exception {
    if (isRead()) {
      constraint.validate();
      addErrors(constraint.getErrors());
      constraint.clearErrors();
    }
  }

  public void setCustomErrorMessage(String customErrorMessage) {
    constraint.setCustomErrorMessage(customErrorMessage);
  }

  public void setEnvironment(Environment environment) {
    super.setEnvironment(environment);
    constraint.setEnvironment(environment);
  }
}
