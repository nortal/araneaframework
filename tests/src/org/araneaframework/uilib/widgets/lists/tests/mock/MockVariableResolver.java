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

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.backend.list.memorybased.Variable;
import org.araneaframework.backend.list.memorybased.expression.VariableResolver;


public class MockVariableResolver implements VariableResolver {
	protected Map<String, Object> variables = new HashMap<String, Object>();

	public MockVariableResolver(Map<String, Object> variables) {
		this.variables = variables;
	}

	public MockVariableResolver(String name, Object value) {
		this.variables = new HashMap<String, Object>();
		this.variables.put(name, value);
	}

	public MockVariableResolver() {
		this.variables = null;
	}

	public Object resolve(Variable variable) {
		if (this.variables == null) {
			throw new RuntimeException("Variable resolving not supported");
		}
		Object value = this.variables.get(variable.getName());
		if (value != null) {
			return value;
		}
		throw new RuntimeException("Variable " + variable.getName()
				+ " not supported");
	}
}
