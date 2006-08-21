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

package org.araneaframework.backend.list.memorybased.expression.constant;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.Resolver;
import org.araneaframework.backend.list.memorybased.expression.Value;

public class ValueExpression implements Expression, Value {

	private static final long serialVersionUID = 1L;

	private String name;

	private Object value;

	public ValueExpression(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public ValueExpression(Object value) {
		this(null, value);
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public Object evaluate(Resolver resolver) {
		return this.value;
	}
}
