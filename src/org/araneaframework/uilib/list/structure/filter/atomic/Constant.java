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
package org.araneaframework.uilib.list.structure.filter.atomic;

import java.util.Map;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.ExpressionBuilder;
import org.araneaframework.uilib.list.util.ExpressionUtil;

public class Constant implements ExpressionBuilder {
	private static final long serialVersionUID = 1L;
	
	private String valueId;
	private Object value;
			
	public Constant(String value) {
		this(null, value);
	}
	
	public Constant(String valueId, Object value) {
		this.valueId = valueId;
		this.value = value;
	}

	public Expression buildExpression(Map filterInfo) {
		if (this.value == null) {
			return ExpressionUtil.nullValue(this.valueId);
		}
		return ExpressionUtil.value(this.valueId, this.value);
	}
}