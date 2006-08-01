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

import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.FormElement;

/**
 * Constraint that will be applied iff the constraint's group is active.
 * 
 * @author Ilja Livenson (ilja@webmedia.ee)
 * 
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

  protected void validateConstraint() throws Exception {
    // in case the constraint's group is inactive, just ignore it
    if (!this.conditionalConstraintHelper.isGroupActive(this.group))
      return;
    else
      this.constraint.validate();
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

  public void setEnviroment(Environment enviroment) {
    constraint.setEnvironment(enviroment);
  }

  public void setField(FormElement field) {
    constraint.setField(field);
  }

  public void clearErrors() {
    constraint.clearErrors();
  }

  public List getErrors() {
    return constraint.getErrors();
  }

  public boolean isValid() {
    return constraint.isValid();
  }

}
