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

import org.araneaframework.backend.list.SqlExpression;

public class MockSqlStringExpression implements SqlExpression {

	protected String string;
	protected Object[] values;

	public MockSqlStringExpression(String string, Object[] values) {
		this.string = string;
		this.values = values;
	}

	public MockSqlStringExpression(String string) {
		this(string, new Object[0]);
	}

	public MockSqlStringExpression(Object[] values) {
		this("", values);
	}

	public String toSqlString() {
		return this.string;
	}

	public Object[] getValues() {
		return this.values;
	}

}
