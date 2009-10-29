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

package org.araneaframework.uilib.list.structure.filter.field;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.util.Event;


public class EqualFilter extends BaseFieldFilter {
	private static final long serialVersionUID = 1L;
	private Comparator comparator;
	
	public static EqualFilter getInstance(final FilterContext ctx, final String fieldId, String valueId) {
		final EqualFilter filter = new EqualFilter();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);

		ctx.addInitEvent(new Event() {
			public void run() {
				filter.setComparator(ctx.getFieldComparator(fieldId));
			}			
		});

		return filter;
	}
	
	public static EqualFilter getConstantInstance(FilterContext ctx, String fieldId, String valueId, Object value) {
		EqualFilter filter = getInstance(ctx, fieldId, valueId);
		filter.setValue(value);
		return filter;
	}

	public static void addToForm(FilterContext ctx, String id, FormElement element) {
		ctx.getForm().addElement(id, element);
	}
	
	public static void addToForm(FilterContext ctx, String id, Control control) {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, control));
	}
	
	public static void addToForm(FilterContext ctx, String id) {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id));
	}
	
	private EqualFilter() {
		// private
	}

	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
    Assert.isInstanceOfParam(this, Serializable.class, comparator, "comparator");
		this.comparator = comparator;
	}

	public Expression buildExpression(Map filterInfo) {
		if (!isActive(filterInfo)) {
			return null;
		}
		return ExpressionUtil.eq(
				buildVariableExpression(),
				buildValueExpression(filterInfo), getComparator());
	}
}
