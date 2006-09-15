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

package org.araneaframework.uilib.list.structure.filter.field;

import java.util.Map;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.list.util.NestedFormUtil;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;


public class LikeFilter extends BaseFieldFilter {

	private static final long serialVersionUID = 1L;
	
	private boolean ignoreCase;
	private LikeConfiguration configuration;
	
	public static LikeFilter getInstance(FilterContext ctx, String fieldId, String valueId) {
		LikeFilter filter = new LikeFilter();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setIgnoreCase(ctx.isIgnoreCase());
		filter.setConfiguration(getConfiguration(ctx));
		return filter;
	}
	
	public static LikeFilter getConstantInstance(FilterContext ctx, String fieldId, String valueId, Object value) {
		LikeFilter filter = new LikeFilter();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setValue(value);
		filter.setIgnoreCase(ctx.isIgnoreCase());
		filter.setConfiguration(getConfiguration(ctx));
		return filter;
	}
		
	private static LikeConfiguration getConfiguration(FilterContext ctx) {
		LikeConfiguration result = (LikeConfiguration)
			ctx.getConfiguration().getEntry(ConfigurationContext.LIKE_CONFIGURATION);
		if (result == null) {
			result = new LikeConfiguration();
		}
		return result;
	}
	
	public static void addToForm(FilterContext ctx, String id, FormElement element) throws Exception {
		NestedFormUtil.addElement(ctx.getForm(), id, element);
	}
	
	public static void addToForm(FilterContext ctx, String id, Control control) throws Exception {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, control, new StringData()));
	}

	public static void addToForm(FilterContext ctx, String id) throws Exception {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, new TextControl(), new StringData()));
	}

	private LikeFilter() {
		// private
	}

	public LikeConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(LikeConfiguration configuration) {
		this.configuration = configuration;
	}

	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public Expression buildExpression(Map filterInfo) {
		if (!isActive(filterInfo)) {
			return null;
		}
		return ExpressionUtil.like(
				buildVariableExpression(),
				buildValueExpression(filterInfo),
				isIgnoreCase(),
				getConfiguration());
	}
}
