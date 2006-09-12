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

package org.araneaframework.uilib.list.structure.filter.column;

import java.util.Comparator;
import java.util.Map;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.list.util.NestedFormUtil;


public class EqualFilter extends BaseFieldFilter {

	private static final long serialVersionUID = 1L;
	
	private Comparator comparator;
	
	public static EqualFilter getInstance(FilterContext ctx, String fieldId, String valueId) {
		EqualFilter filter = new EqualFilter();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setComparator(ctx.getComparator(fieldId));
		return filter;
	}
	
	public static EqualFilter getConstantInstance(FilterContext ctx, String fieldId, String valueId, Object value) {
		EqualFilter filter = new EqualFilter();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setValue(value);
		filter.setComparator(ctx.getComparator(fieldId));
		return filter;
	}

	public static void addToForm(FilterContext ctx, String id, FormElement element) throws Exception {
		NestedFormUtil.addElement(ctx.getForm(), id, element);
	}
	
	public static void addToForm(FilterContext ctx, String id, Control control) throws Exception {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, control));
	}
	
	private EqualFilter() {
		// private
	}

	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
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
