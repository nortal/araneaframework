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

package org.araneaframework.uilib.form.constraint;

import org.araneaframework.Environment;
import org.araneaframework.uilib.form.Constraint;

/**
 * Optional <code>Constraint</code> only applies when constrained field has some real value. This class is a wrapper
 * around the <code>Constraint</code>, and the wrapped <code>Constraint</code> will be applied only if the constrained
 * field has been read from the request.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class OptionalConstraint<C, D> extends BaseFieldConstraint<C, D> {

  private Constraint constraint;

  /**
   * Creates a new <code>OptionalConstraint</code> wrapper for given <code>constraint</code>. The latter will be
   * validated only if the field has a value.
   * 
   * @param constraint The constraint to use for validation.
   */
  public OptionalConstraint(Constraint constraint) {
    this.constraint = constraint;
  }

  @Override
  protected void validateConstraint() throws Exception {
    if (getValue() != null) {
      this.constraint.validate();
      addErrors(this.constraint.getErrors());
      this.constraint.clearErrors();
    }
  }

  @Override
  public void setCustomErrorMessage(String customErrorMessage) {
    this.constraint.setCustomErrorMessage(customErrorMessage);
  }

  @Override
  public void setEnvironment(Environment environment) {
    super.setEnvironment(environment);
    this.constraint.setEnvironment(environment);
  }
}
