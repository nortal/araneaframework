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

import java.util.Arrays;
import java.util.Collection;
import org.araneaframework.uilib.form.Constraint;

/**
 * This constraint implements "OR" Boolean logic (checks that at least one contained constraints is satisfied). It is
 * eager by default, but can be set to act lazily.
 * <p>
 * Note that sub-constraints produce error messages as they are being validated. Unless some custom error message has
 * been set, it makes often sense to process all sub-constraints.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class OrConstraint extends BaseCompositeConstraint {

  private boolean lazy;

  public OrConstraint() {
  }

  /**
   * Creates an <code>OrConstraint</code> for one constraint. Other constraints can be added later with
   * {@link #addConstraint(Constraint)} or {@link #addConstraints(Collection)} methods.
   * 
   * @param constraint The constraint to validate.
   * @since 1.0.9
   */
  public OrConstraint(Constraint constraint) {
    super(constraint);
  }

  /**
   * Creates an <code>OrConstraint</code> for a collection of <code>Constraint</code>s. Other constraints can be added
   * later with {@link #addConstraint(Constraint)} or {@link #addConstraints(Collection)} methods.
   * 
   * @param constraints A collection of {@link Constraint}s.
   * @since 1.0.9
   */
  public OrConstraint(Collection<Constraint> constraints) {
    super(constraints);
  }

  /**
   * Creates an <code>OrConstraint</code> for a collection of <code>Constraint</code>s. Other constraints can be added
   * later with {@link #addConstraint(Constraint)} or {@link #addConstraints(Collection)} methods.
   * 
   * @param constraints A collection of {@link Constraint}s.
   * @since 1.0.9
   */
  public OrConstraint(Constraint... constraints) {
    super(Arrays.asList(constraints));
  }

  /**
   * Checks that at least one contained constraints is satisfied.
   */
  @Override
  public void validateConstraint() throws Exception {
    boolean valid = false;

    for (Constraint constraint : this.constraints) {
      valid = valid || constraint.validate();
      addErrors(constraint.getErrors());
      constraint.clearErrors();
      if (valid && this.lazy) {
        break;
      }
    }

    if (valid) {
      clearErrors();
    }
  }

  /**
   * Sets whether this {@link OrConstraint} acts lazily, default is <code>false</code>.
   * 
   * @param lazy If <code>true</code> then subconstraints will be validated lazily (until one of them succeeds). If
   *          <code>false</code> then all subconstraints will be validated.
   * @return This <code>OrConstraint</code>.
   */
  public OrConstraint setLazy(boolean lazy) {
    this.lazy = lazy;
    return this;
  }

}
