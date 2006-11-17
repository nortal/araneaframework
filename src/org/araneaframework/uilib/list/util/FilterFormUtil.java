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

import java.text.MessageFormat;
import java.util.MissingResourceException;

import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.Event;

/**
 * List filters form utils.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class FilterFormUtil {
	
	public static final String TEMPORARY_LABEL = "";
	public static final String DO_NOT_LOCALIZE_PREFIX = "#"; 
	
	public static FormElement createElement(FilterContext ctx, String id)
		throws Exception {
		Class type = ctx.getFieldType(id);
		FormElement result = FormUtil.createElement(TEMPORARY_LABEL,
				FormUtil.createControl(type),
				FormUtil.createData(type), false);
		setLabel(ctx, result, id);
		return result ;
	}
	
	public static FormElement createElement(FilterContext ctx, String id,
			Control control) throws Exception {
		FormElement result = FormUtil.createElement(TEMPORARY_LABEL, control,
				FormUtil.createData(ctx.getFieldType(id)), false);
		setLabel(ctx, result, id);
		return result;
	}
	
	public static FormElement createElement(FilterContext ctx, String id,
			Control control, Data data) throws Exception {
		FormElement result = FormUtil.createElement(TEMPORARY_LABEL, control, data, false); 
		setLabel(ctx, result, id);
		return result;
	}
	
	public static String getLabelForLowField(LocalizationContext loc, String fieldLabelId) {
		return DO_NOT_LOCALIZE_PREFIX +
			format(localize(loc, UiLibMessages.LOW_OF), localize(loc, fieldLabelId));
	}
	
	public static String getLabelForHighField(LocalizationContext loc, String fieldLabelId) {
		return DO_NOT_LOCALIZE_PREFIX +
			format(localize(loc, UiLibMessages.HIGH_OF), localize(loc, fieldLabelId));
	}
	
	private static String format(String pattern, String arg) {
		return MessageFormat.format(pattern, new Object[] {arg});
	}
	
	private static String localize(LocalizationContext loc, String key) {
		try {
			return loc.localize(key);
		}
		catch (MissingResourceException e) {
			return key;
		}
	}
	
	public static void setLabel(final FilterContext ctx,
			final FormElement formElement, final String fieldId) {
		ctx.addInitEvent(new Event() {
			public void run() {
				formElement.setLabel(ctx.getFieldLabel(fieldId));
			}
		});
	}
}
