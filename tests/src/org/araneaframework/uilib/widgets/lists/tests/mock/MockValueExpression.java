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

package org.araneaframework.uilib.widgets.lists.tests.mock;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;

public class MockValueExpression<T> implements Expression, Value<T> {

	private T value;

	public MockValueExpression(T value) {
		this.value = value;
	}

	public String getName() {
		return null;
	}

	public T getValue() {
		return this.value;
	}

	public Object evaluate(VariableResolver resolver) {
		return this.value;
	}
}
