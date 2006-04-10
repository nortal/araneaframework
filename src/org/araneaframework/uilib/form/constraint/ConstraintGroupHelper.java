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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.araneaframework.uilib.form.Constraint;

/**
 * Helper for the <code>GroupedConstraint</code> that holds a set of currently
 * active groups and provides a factory method for wrapping <code>Constraint</code> 
 * instances in <code>GroupedConstraint</code>.
 * @author Ilja Livenson (ilja@webmedia.ee)
 *
 */
public class ConstraintGroupHelper implements Serializable {
	private Set activeGroups = new HashSet();
		
	/**
	 * Wrap the <code>Constraint</code> in the <code>GroupedConstraint</code> instance
	 * assigned to the specified group.
	 * @param constraint Constraint to wrap.
	 * @param group Name of the group to assign the constraint to.
	 */
	public Constraint createGroupedConstraint(Constraint constraint, String group) {
		return new GroupedConstraint(this, constraint, group);
	}
	
	public void setActiveGroups(Set activeGroups) {
		if (activeGroups == null) 
			this.activeGroups = new HashSet();
		else 
			this.activeGroups = activeGroups;
	}
	
	public void setActiveGroup(String activeGroup) {
		if (activeGroup == null) {
			this.activeGroups = new HashSet();
			return;
		}			
		this.activeGroups = new HashSet(1);
		this.activeGroups.add(activeGroup);
	}
	
	public Set getActiveGroups() {
		return activeGroups;
	}	

	public boolean isGroupActive(String group) {
		return this.activeGroups.contains(group);
	}
}
