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
package org.araneaframework.uilib.list.structure.filterform;

import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AndConstraint;
import org.araneaframework.uilib.form.constraint.CompositeConstraint;
import org.araneaframework.uilib.form.constraint.RangeConstraint;
import org.araneaframework.uilib.list.util.RecursiveFormUtil;

/**
 * Adding filter to form utils.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class FormFilterUtil {
	private static final boolean DEFAULT_MADATORY = false;
	
	public static void add(FormWidget form, String id, String labelId,
			Control control, Class type) throws Exception {
		
		RecursiveFormUtil.addElement(form, id, labelId, control, new Data(type), DEFAULT_MADATORY);
	}
	
	public static void add(FormWidget form, String id, String labelId,
			FormElement element) throws Exception {
		
		setLabelIfNull(element, labelId);
		RecursiveFormUtil.addElement(form, id, element);
	}
	
	public static void addRange(FormWidget form, String lowId, String highId,
			String lowLabel, String highLabel,
			Control lowControl, Control highControl, Class type, boolean allowEquals) throws Exception {
		
		FormElement low = RecursiveFormUtil.addElement(form, lowId, lowLabel, lowControl, new Data(type), DEFAULT_MADATORY);
		FormElement high = RecursiveFormUtil.addElement(form, highId, highLabel, highControl, new Data(type), DEFAULT_MADATORY);
		
		addConstraint(form, new RangeConstraint(low, high, allowEquals));
	}
	
	public static void addRange(FormWidget form, String lowId, String highId,
			String lowLabel, String highLabel,
			FormElement lowElement, FormElement highElement, boolean allowEquals) throws Exception {
		
		setLabelIfNull(lowElement, lowLabel);
		setLabelIfNull(highElement, highLabel);
		RecursiveFormUtil.addElement(form, lowLabel, lowElement);
		RecursiveFormUtil.addElement(form, highId, highElement);
		
		addConstraint(form, new RangeConstraint(lowElement, highElement, allowEquals));
	}

	/**
	 * Adds a constraint to a form.
	 * 
	 * @param form
	 *            form.
	 * @param constraint
	 *            constraint.
	 */
	private static void addConstraint(FormWidget form, Constraint constraint) {
		if (form.getConstraint() == null) {
			form.setConstraint(constraint);
		} else if (AndConstraint.class.isAssignableFrom(form.getConstraint().getClass())) {
			((AndConstraint) form.getConstraint()).addConstraint(constraint);
		} else {
			CompositeConstraint and = new AndConstraint();
			and.addConstraint(form.getConstraint());
			and.addConstraint(constraint);
			form.setConstraint(and);
		}
	}
	
	private static void setLabelIfNull(FormElement element, String labelId) {
		if (element.getLabel() == null && labelId != null) {
			element.setLabel(labelId);
			element.getControl().setLabel(labelId);
		}
	}	
}
