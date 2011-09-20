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
 * This constraint implements "AND" Boolean logic (checks that all contained constraints are satisfied). It is eager by
 * default, but can be set to act lazily, (note that sub-constraints produce error messages as they are being validated,
 * unless some custom error message has been set, it makes often sense to process all sub-constraints).
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class AndConstraint extends BaseCompositeConstraint {

  private boolean lazy;

  /**
   * An empty constructor that expects other constraints to be added later. Use {@link #addConstraint(Constraint)} or
   * {@link #addConstraints(Collection)} to add constraints.
   * <p>
   * Later all added constraints are expected to not fail, or this constraint fails also.
   */
  public AndConstraint() {
  }

  /**
   * A constructor that requires a constraint to be provided immediately, and other constraints later. Use
   * {@link #addConstraint(Constraint)} or {@link #addConstraints(Collection)} to add more constraints.
   * <p>
   * Later all added constraints are expected to not fail, or this constraint fails also.
   * 
   * @param constraint a single constraint.
   * @since 1.0.9
   */
  public AndConstraint(Constraint constraint) {
    super(constraint);
  }

  /**
   * A constructor that lets all constraints be added at once. Use {@link #addConstraint(Constraint)} or
   * {@link #addConstraints(Collection)} to add more constraints.
   * <p>
   * Later all added constraints are expected to not fail, or this constraint fails also.
   * 
   * @param constraints a collection of {@link Constraint}s.
   * @since 1.0.9
   */
  public AndConstraint(Collection<Constraint> constraints) {
    super(constraints);
  }

  /**
   * A constructor that lets all constraints be added at once. Use {@link #addConstraint(Constraint)} or
   * {@link #addConstraints(Collection)} to add more constraints.
   * <p>
   * Later all added constraints are expected to not fail, or this constraint fails also.
   * 
   * @param constraints an array of {@link Constraint}s.
   * @since 2.0
   */
  public AndConstraint(Constraint... constraints) {
    super(Arrays.asList(constraints));
  }

  /**
   * Checks that all contained constraints are satisfied.
   */
  @Override
  public void validateConstraint() throws Exception {
    for (Constraint constraint : this.constraints) {
      boolean valid = constraint.validate();
      addErrors(constraint.getErrors());
      constraint.clearErrors();
      if (!valid && this.lazy) {
        break;
      }
    }
  }

  /**
   * Sets whether this constraint acts lazily, default is <code>false</code>.
   * 
   * @param lazy <code>true</code>, if this constraint should acts lazily. Otherwise, <code>false</code>.
   * @return this constraint.
   */
  public AndConstraint setLazy(boolean lazy) {
    this.lazy = lazy;
    return this;
  }

}
