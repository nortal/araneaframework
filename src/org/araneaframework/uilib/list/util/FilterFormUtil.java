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

import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.list.structure.filter.FilterContext;

/**
 * List filters form utils.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class FilterFormUtil {
	
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
	
	public static FormElement createElement(FilterContext ctx, String id)
		throws Exception {
		Class type = ctx.getFieldType(id);
		return createElement(ctx, id,
				createControl(type),
				createData(type));
	}
	
	public static FormElement createElement(FilterContext ctx, String id,
			Control control) throws Exception {
		return createElement(ctx, id, control, createData(ctx.getFieldType(id)));
	}
	
	public static FormElement createElement(FilterContext ctx, String id,
			Control control, Data data) throws Exception {
	    FormElement result = new FormElement();	    
	    result.setLabel(ctx.getFieldLabel(id));
	    result.setMandatory(false);    
	    result.setControl(control);
	    result.setData(data);
	    return result;
	}
}
