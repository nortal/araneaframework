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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.FormElementAware;
import org.araneaframework.uilib.form.FormElementContext;
import org.araneaframework.uilib.form.GenericFormElementContext;


/**
 * This class is a generalization of a constraint that may contain other
 * constraints.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public abstract class CompositeConstraint extends BaseConstraint implements FormElementAware {

  protected List constraints = new ArrayList();

  /**
	 * Adds a contained constraint.
	 * 
	 * @param constraint
	 *          contained constraint.
	 */
  public void addConstraint(Constraint constraint) {
    constraints.add(constraint);
  }

  /**
	 * Clears contained constraints.
	 */
  public void clearConstraints() {
    constraints.clear();
  }
  
  public void setGenericFormElementCtx(GenericFormElementContext feCtx) {
    super.setGenericFormElementCtx(feCtx);
    for (Iterator i = constraints.iterator(); i.hasNext();) {
      Constraint c = (Constraint) i.next();
      c.setGenericFormElementCtx(getGenericFormElementCtx());
    }  
  }

  public void setFormElementCtx(FormElementContext feCtx) {
    for (Iterator i = constraints.iterator(); i.hasNext();) {
      Constraint c = (Constraint) i.next();
      if (c instanceof FormElementAware)
        ((FormElementAware) c).setFormElementCtx(feCtx);
    }    
  }
}
