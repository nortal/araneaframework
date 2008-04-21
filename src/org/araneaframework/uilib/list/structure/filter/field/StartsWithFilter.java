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
import org.araneaframework.Environment;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.compare.StartsWithExpression;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.FilterFormUtil;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;

/**
 * A filter for StartsWith expressions. Handles also form element adding.
 * Similar to <code>LikeFilter</code>, but has a different
 * <code>buildExpression()</code> method return value.
 * 
 * @see StartsWithExpression
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 */
public class StartsWithFilter extends BaseFieldFilter {

	private static final long serialVersionUID = 1L;

	private boolean ignoreCase;

	private LikeConfiguration configuration;

	public static StartsWithFilter getInstance(FilterContext ctx, String fieldId,
			String valueId) {
		StartsWithFilter filter = new StartsWithFilter();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setIgnoreCase(ctx.isIgnoreCase());
		return filter;
	}

	public static StartsWithFilter getConstantInstance(FilterContext ctx,
			String fieldId, String valueId, Object value) {
		StartsWithFilter filter = getInstance(ctx, fieldId, valueId);
		filter.setValue(value);
		return filter;
	}

	public static void addToForm(FilterContext ctx, String id, FormElement element) {
		ctx.getForm().addElement(id, element);
	}

	public static void addToForm(FilterContext ctx, String id, Control control) {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, control,
				new StringData()));
	}

	public static void addToForm(FilterContext ctx, String id) {
		addToForm(ctx, id, FilterFormUtil.createElement(ctx, id, new TextControl(),
				new StringData()));
	}

	private StartsWithFilter() {
		// private
	}

	public void init(Environment env) {
		ConfigurationContext cfg = (ConfigurationContext) env.getEntry(ConfigurationContext.class);
		if (cfg != null) {
			configuration = (LikeConfiguration) cfg.getEntry(ConfigurationContext.LIKE_CONFIGURATION);
		}
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
		return ExpressionUtil.startsWith(
				buildVariableExpression(),
				buildValueExpression(filterInfo),
				isIgnoreCase(),
				getConfiguration());
	}

}
