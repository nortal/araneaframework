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

import java.util.Map;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.list.structure.filter.FilterContext;
import org.araneaframework.uilib.list.util.ExpressionUtil;
import org.araneaframework.uilib.list.util.like.LikeConfiguration;


public class LikeFilter extends BaseFieldFilter {

	private static final long serialVersionUID = 1L;
	
	public static final boolean IGNORE_CASE_BY_DEFAULT = true;
	
	private boolean ignoreCase;
	private LikeConfiguration configuration;
	
	public static LikeFilter getInstance(FilterContext ctx, String fieldId, String valueId) {
		LikeFilter filter = new LikeFilter();
		filter.setFieldId(fieldId);
		filter.setValueId(valueId);
		filter.setIgnoreCase(ctx.isIgnoreCase());
		
		// Like confiugration
		filter.configuration = (LikeConfiguration) ((ConfigurationContext)
				ctx.getEnvironment().getEntry(ConfigurationContext.class)).
				getEntry(ConfigurationContext.LIKE_CONFIGURATION);
		if (filter.configuration == null) {
			filter.configuration = new LikeConfiguration();
		}
		
		return filter;
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
