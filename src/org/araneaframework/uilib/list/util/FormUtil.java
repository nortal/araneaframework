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

import java.math.BigDecimal;

import org.araneaframework.uilib.form.Constraint;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AndConstraint;
import org.araneaframework.uilib.form.constraint.CompositeConstraint;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;

/**
 * Form utils.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see FormWidget
 */
public class FormUtil {
	
	// Date
	
	public static Data createData(Class type) {
		return new Data(type);
	}
	
	// Controls
	
	public static Control createControl(Class type) {
		if (String.class.equals(type)) {
			return createTextControl();
		}
		if (Number.class.isAssignableFrom(type)) {
			if (BigDecimal.class.isAssignableFrom(type)) {
				return createFloatControl();
			}
			if (Long.class.equals(type)
					|| Integer.class.equals(type)
					|| Short.class.equals(type)
					|| Byte.class.equals(type)) {
				return createNumberControl();
			}
			return createFloatControl();
		}
		if (java.util.Date.class.isAssignableFrom(type)) {
			if (java.util.Date.class.equals(type)
					|| java.sql.Date.class.isAssignableFrom(type)) {
				return createDateControl();
			}
			if (java.sql.Time.class.isAssignableFrom(type)) {
				return createTimeControl();
			}
			if (java.sql.Timestamp.class.isAssignableFrom(type)) {
				return createDateTimeControl();
			}
		}
		return createTextControl();
	}
	
	public static Control createTextControl() {
		return new TextControl();
	}
	public static Control createNumberControl() {
		return new NumberControl();
	}
	public static Control createFloatControl() {
		return new FloatControl();
	}
	public static Control createDateControl() {
		return new DateControl();
	}
	public static Control createTimeControl() {
		return new TimeControl();
	}	
	public static Control createDateTimeControl() {
		return new DateTimeControl();
	}
	
	// Form elements
	
	public static FormElement createElement(String label, Control control, Data data, boolean mandatory) throws Exception {
	    FormElement result = new FormElement();	    
	    result.setLabel(label);
	    result.setMandatory(mandatory);    
	    result.setControl(control);
	    result.setData(data);
	    return result;
	}
	
	// Constraints
	
	/**
	 * Adds a constraint to a form.
	 * 
	 * @param form
	 *            form.
	 * @param constraint
	 *            constraint.
	 */
	public static void addConstraint(FormWidget form, Constraint constraint) {
		Constraint current = form.getConstraint();
		
		if (current == null) {
			form.setConstraint(constraint);
		} else if (current instanceof AndConstraint) {
			((AndConstraint) current).addConstraint(constraint);
		} else {
			CompositeConstraint and = new AndConstraint();
			and.addConstraint(current);
			and.addConstraint(constraint);
			form.setConstraint(and);
		}
	}	
}
