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

import org.araneaframework.uilib.form.Constrainable;
import org.araneaframework.uilib.form.Constraint;

/**
 * Constraint that will be applied iff the constraint's group is active.
 * 
 * @author Ilja Livenson (ilja@webmedia.ee)
 */
public class GroupedConstraint extends BaseConstraint {
  private ConstraintGroupHelper conditionalConstraintHelper;
  private Constraint constraint;
  private String group;

  public GroupedConstraint(ConstraintGroupHelper helper, Constraint constraint, String group) {
    this.conditionalConstraintHelper = helper;
    this.constraint = constraint;
    this.group = group;
  }
  
  public void constrain(Constrainable constrainable) {
    super.constrain(constrainable);
    constraint.constrain(constrainable);
  }

  protected void validateConstraint() throws Exception {
    // in case the constraint's group is inactive, just ignore it
    if (!this.conditionalConstraintHelper.isGroupActive(this.group))
      return;
    else
      this.constraint.validate();
  }
  
  public boolean isValid() {
   if (!this.conditionalConstraintHelper.isGroupActive(this.group))
     return true;
    return (this.constraint.getErrors() == null || this.constraint.getErrors().isEmpty());
  }

  public ConstraintGroupHelper getConditionalConstraintHelper() {
    return conditionalConstraintHelper;
  }

  public void setConditionalConstraintHelper(ConstraintGroupHelper conditionalConstraintHelper) {
    this.conditionalConstraintHelper = conditionalConstraintHelper;
  }

  public void setCustomErrorMessage(String customErrorMessage) {
    constraint.setCustomErrorMessage(customErrorMessage);
  }
}
