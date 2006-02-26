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

package org.araneaframework.uilib.widgets.lists.tests.mock;

import org.araneaframework.uilib.widgets.lists.presentation.memorybased.Expression;
import org.araneaframework.uilib.widgets.lists.presentation.memorybased.expression.Value;
import org.araneaframework.uilib.widgets.lists.presentation.memorybased.expression.VariableResolver;

public class MockValueExpression implements Expression, Value {
	private static final long serialVersionUID = 1L;

	private Object value;

	public MockValueExpression(Object value) {
		this.value = value;
	}

	public String getName() {
		return null;
	}

	public Object getValue() {
		return this.value;
	}

	public Object evaluate(VariableResolver resolver) {
		return this.value;
	}
}
