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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.araneaframework.uilib.form.Constraint;

/**
 * Helper for the {@link GroupedConstraint} that holds a set of currently active
 * groups and provides a factory method for wrapping <code>Constraint</code>
 * instances in {@link GroupedConstraint}. An example would look like this:
 * <pre><code>
 * ConstraintGroupHelper helper = new ConstraintGroupHelper();
 * ...
 * myFormElement1.setConstraint(
 *   helper.createGroupedConstraint(new MyConstraint1(), "group1"));
 * myFormElement2.setConstraint(
 *   helper.createGroupedConstraint(new MyConstraint2(), "group1"));
 * ...
 * myFormElement3.setConstraint(
 *   helper.createGroupedConstraint(new MyConstraint3(), "group2"));
 * myFormElement4.setConstraint(
 *   helper.createGroupedConstraint(new MyConstraint4(), "group2"));
 * ...
 * helper.setActiveGroup("group1")
 * </code></pre>
 * 
 * @author Ilja Livenson (ilja@webmedia.ee)
 */
public class ConstraintGroupHelper implements Serializable {

  private Set<String> activeGroups = new HashSet<String>();

  /**
   * Wrap the <code>constraint</code> in the <code>GroupedConstraint</code>
   * instance that is assigned to the specified <code>group<code>.
   * 
   * @param constraint Constraint to wrap.
   * @param group Name of the group to assign the constraint to.
   * @return The created grouped constraint
   */
  public Constraint createGroupedConstraint(Constraint constraint, String group) {
    return new GroupedConstraint(this, constraint, group);
  }

  /**
   * Provides a way to specify multiple active groups as a <code>Set</code>.
   * The previously activated groups, that are not contained in given
   * <code>activeGroups</code> parameter, are made inactive.
   * 
   * @param activeGroups The names of the groups to be made active.
   */
  public void setActiveGroups(Set<String> activeGroups) {
    this.activeGroups = activeGroups == null ? new HashSet<String>() : activeGroups;
  }

  /**
   * Provides a way to specify multiple active groups as an array of
   * <code>String</code>s. The previously activated groups, that are not
   * contained in given <code>activeGroups</code> parameter, are made
   * inactive.
   * 
   * @param activeGroups The names of the groups to be made active.
   */
  public void setActiveGroups(String[] activeGroups) {
    this.activeGroups = new HashSet<String>();
    CollectionUtils.addAll(this.activeGroups, activeGroups);
  }

  /**
   * Provides a way to specify only one active group. The previously activated
   * groups, that are not the same as the given <code>activeGroup</code>, are
   * made inactive.
   * <p>
   * If the <code>activeGroup</code> parameter is <code>null</code>, all
   * groups will be made inactive.
   * 
   * @param activeGroup The name of the group to be made active, or
   *            <code>null</code>.
   */
  public void setActiveGroup(String activeGroup) {
    if (activeGroup == null) {
      this.activeGroups = new HashSet<String>();
    } else {
      this.activeGroups = new HashSet<String>();
      this.activeGroups.add(activeGroup);
    }
  }

  /**
   * Provides currently activated groups.
   * 
   * @return currently activated groups.
   */
  public Set<String> getActiveGroups() {
    return this.activeGroups;
  }

  /**
   * Specifies whether the given group is active. The group name used in search
   * is case-sensitive.
   * 
   * @param group The name of the group to check whether it
   * @return <code>true</code>, if the group with given name is active.
   */
  public boolean isGroupActive(String group) {
    return this.activeGroups.contains(group);
  }

}
