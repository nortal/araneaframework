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

package org.araneaframework.uilib.list.util;

import java.io.Serializable;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;

/**
 * {@link FormWidget} and {@link BeanFormWidget} <code>addElement</code> methods
 * that take full element Id (separated by dots) as an argument. 
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class RecursiveFormUtil {

	// FormWidget	

	/**
	 * Adds a contained element.
	 * 
	 * @param form form.
	 * @param fullId full element id (separated by dots).
	 * @param element contained element.
	 */
	public static void addElement(FormWidget form, String fullId, final GenericFormElement element) throws Exception {
		addElement(form, fullId, new FormElementAdder() {
			public FormElement addFormElement(FormWidget form, String id) throws Exception {
				form.addElement(id, element);
				return null;
			}
		});		
	}

	/**
	 * This method adds a {@link FormElement}.
	 * 
	 * @param form form.
	 * @param fullId full element id (separated by dots).
	 * @param labelId id of the localized label.
	 * @param control the type of control data.
	 * @param data the type of data.
	 * @param mandatory whether the element must be present in request.
	 * @return FormElement that was just added.
	 */
	public static FormElement addElement(FormWidget form, String fullId, final String labelId, final Control control, final Data data, final boolean mandatory) throws Exception {
		return addElement(form, fullId, new FormElementAdder() {
			public FormElement addFormElement(FormWidget form, String id) throws Exception {
				return form.addElement(id, labelId, control, data, mandatory);
			}
		});
	}

	/**
	 * This method adds a {@link FormElement}.
	 * 
	 * @param form form.
	 * @param fullId full element id (separated by dots).
	 * @param labelId id of the localized label.
	 * @param control the type of control data.
	 * @param data the type of data.
	 * @param initialValue initial value.
	 * @param mandatory whether the element must be present in request.
	 * @return FormElement that was just added.
	 */
	public static FormElement addElement(FormWidget form, String fullId, final String labelId, final Control control, final Data data, final Object initialValue, final boolean mandatory) throws Exception {
		return addElement(form, fullId, new FormElementAdder() {
			public FormElement addFormElement(FormWidget form, String id) throws Exception {
				return form.addElement(id, labelId, control, data, initialValue, mandatory);
			}
		});
	}

	// BeanFormWidget
	
	/**
	 * This method adds a {@link FormElement}.
	 * 
	 * @param form form.
	 * @param fullId full element id (separated by dots).
	 * @param labelId id of the localized label.
	 * @param control the type of control data.
	 * @param mandatory whether the element must be present in request.
	 * @return FormElement that was just added.
	 */	
	public static FormElement addBeanElement(BeanFormWidget form, String fullId, final String labelId, final Control control, final boolean mandatory) throws Exception {
		return addBeanElement(form, fullId, new BeanFormElementAdder() {
			public FormElement addFormElement(BeanFormWidget form, String id) throws Exception {
				return form.addBeanElement(id, labelId, control, mandatory);
			}
		});
	}
	
	/**
	 * This method adds a {@link FormElement}.
	 * 
	 * @param form form.
	 * @param fullId full element id (separated by dots).
	 * @param labelId id of the localized label.
	 * @param control the type of control data.
	 * @param initialValue initial value.
	 * @param mandatory whether the element must be present in request.
	 * @return FormElement that was just added.
	 */
	public static FormElement addBeanElement(BeanFormWidget form, String fullId, final String labelId, final Control control, final Object initialValue, final boolean mandatory) throws Exception {
		return addBeanElement(form, fullId, new BeanFormElementAdder() {
			public FormElement addFormElement(BeanFormWidget form, String id) throws Exception {
				return form.addBeanElement(id, labelId, control, initialValue, mandatory);
			}
		});
	}

	// Private

	private static interface FormElementAdder extends Serializable {
		FormElement addFormElement(FormWidget form, String id) throws Exception;
	}

	private static interface BeanFormElementAdder extends Serializable {
		FormElement addFormElement(BeanFormWidget form, String id) throws Exception;
	}

	private static FormElement addElement(FormWidget form, String fullId, FormElementAdder adder) throws Exception {
		if (fullId.indexOf(".") != -1) {
			String subFormId = fullId.substring(0, fullId.indexOf("."));
			String nextFullId =  fullId.substring(subFormId.length() + 1);

			FormWidget subForm = null;

			if (form.getElement(subFormId) != null) {
				subForm = form.getSubFormByFullName(subFormId);        	
			} else {
				subForm = form.addSubForm(subFormId);        	
			}

			return adder.addFormElement(subForm, nextFullId);
		}

		return adder.addFormElement(form, fullId);
	}


	private static FormElement addBeanElement(BeanFormWidget form, String fullId, BeanFormElementAdder adder) throws Exception {
		if (fullId.indexOf(".") != -1) {
			String subFormId = fullId.substring(0, fullId.indexOf("."));
			String nextFullId =  fullId.substring(subFormId.length() + 1);

			BeanFormWidget subForm = null;

			if (form.getElement(subFormId) != null) {
				subForm = (BeanFormWidget) form.getElement(subFormId);        	
			} else {
				subForm = form.addBeanSubForm(subFormId);        	
			}

			return adder.addFormElement(subForm, nextFullId);
		}

		return adder.addFormElement(form, fullId);
	}
}
