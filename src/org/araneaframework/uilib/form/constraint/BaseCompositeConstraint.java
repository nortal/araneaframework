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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.araneaframework.Environment;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Constraint;

/**
 * This class is a generalization of a constraint that may contain other constraints.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BaseCompositeConstraint extends BaseConstraint {

  /**
   * List of {@link Constraint}s that this constraint should handle.
   */
  protected List<Constraint> constraints = new ArrayList<Constraint>();

  /**
   * An empty constructor that expects other constraints to be added later. Use {@link #addConstraint(Constraint)} or
   * {@link #addConstraints(Collection)} to add constraints.
   */
  public BaseCompositeConstraint() {}

  /**
   * A constructor that requires a constraint to be provided immediately, and other constraints later. Use
   * {@link #addConstraint(Constraint)} or {@link #addConstraints(Collection)} to add more constraints.
   * 
   * @param constraint a single constraint.
   * @since 1.0.9
   */
  public BaseCompositeConstraint(Constraint constraint) {
    addConstraint(constraint);
  }

  /**
   * A constructor that lets all constraints be added at once. Use {@link #addConstraint(Constraint)} or
   * {@link #addConstraints(Collection)} to add more constraints.
   * 
   * @param constraints a collection of {@link Constraint}s.
   * @since 1.0.9
   */
  public BaseCompositeConstraint(Collection<Constraint> constraints) {
    addConstraints(constraints);
  }

  /**
   * Adds a contained constraint.
   * 
   * @param constraint contained constraint.
   * @return this composite constraint
   */
  public BaseCompositeConstraint addConstraint(Constraint constraint) {
    this.constraints.add(constraint);
    return this;
  }

  /**
   * Adds contained constraints from supplied Collection.
   * 
   * @param constraints Collection&lt;Constraint&gt;
   * @return this composite constraint
   * @since 1.0.9
   */
  public BaseCompositeConstraint addConstraints(Collection<Constraint> constraints) {
    Assert.notNullParam(constraints, "constraints");
    this.constraints.addAll(constraints);
    return this;
  }

  /**
   * Clears contained constraints.
   */
  public void clearConstraints() {
    this.constraints.clear();
  }

  @Override
  public void setEnvironment(Environment environment) {
    super.setEnvironment(environment);
    for (Constraint c : this.constraints) {
      c.setEnvironment(environment);
    }
  }
}
