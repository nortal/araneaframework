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
 * Constraint that will be applied if the group of constraints is active. To
 * check, if the group is active, the {@link ConstraintGroupHelper} is used. In
 * addition, the latter also gives a more convenient way to create this grouped
 * constraint (see
 * {@link ConstraintGroupHelper#createGroupedConstraint(Constraint, String)}).
 * 
 * @author Ilja Livenson (ilja@webmedia.ee)
 * @see ConstraintGroupHelper
 */
public class GroupedConstraint extends BaseConstraint {

  private static final long serialVersionUID = 1L;

  private ConstraintGroupHelper conditionalConstraintHelper;

  private Constraint constraint;

  private String group;

  /**
   * Creates a new grouped constraint. The <code>helper</code> should be
   * common (the same instance) to all constraints as it is used to check
   * whether the group is active. The group must be registered in the
   * <code>helper</code> beforehand.
   * <p>
   * Usually, one should use a more convenient way to add constraints to a group
   * by using
   * {@link ConstraintGroupHelper#createGroupedConstraint(Constraint, String)}.
   * 
   * @param helper The helper for grouped constraints.
   * @param constraint The constraint belonging to that group.
   * @param group The name of the group.
   * @see ConstraintGroupHelper#createGroupedConstraint(Constraint, String)
   */
  public GroupedConstraint(ConstraintGroupHelper helper, Constraint constraint, String group) {
    this.conditionalConstraintHelper = helper;
    this.constraint = constraint;
    this.group = group;
  }

  /**
   * Validates the constraint, if the group is active. If it is not active, the
   * validation succeeds.
   */
  protected void validateConstraint() throws Exception {
    if (!this.conditionalConstraintHelper.isGroupActive(this.group))
      return;
    else
      this.constraint.validate();
    addErrors(constraint.getErrors());
    constraint.clearErrors();
  }

  /**
   * Provides the <code>ConstraintGroupHelper</code> used by this group
   * constraint to handle group names.
   * 
   * @return The <code>ConstraintGroupHelper</code> used by this group
   *         constraint.
   */
  public ConstraintGroupHelper getConditionalConstraintHelper() {
    return conditionalConstraintHelper;
  }

  /**
   * Sets the <code>ConstraintGroupHelper</code> to be used by this group
   * constraint to handle group names.
   * 
   * @param conditionalConstraintHelper A new <code>ConstraintGroupHelper</code>.
   */
  public void setConditionalConstraintHelper(ConstraintGroupHelper conditionalConstraintHelper) {
    this.conditionalConstraintHelper = conditionalConstraintHelper;
  }

  public void setEnvironment(Environment environment) {
    constraint.setEnvironment(environment);
  }

  public void setCustomErrorMessage(String customErrorMessage) {
    constraint.setCustomErrorMessage(customErrorMessage);
  }

  public void clearErrors() {
    constraint.clearErrors();
  }

}
