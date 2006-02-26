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

package org.araneaframework.uilib.widgets.lists.tests.tests.sqlexpr;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.araneaframework.uilib.widgets.lists.backend.sqlexpr.constant.SqlStringExpression;
import org.araneaframework.uilib.widgets.lists.backend.sqlexpr.constant.SqlValueExpression;


public class SqlExpressionTests extends TestCase {
	private static final Logger log = Logger
			.getLogger(SqlExpressionTests.class);

	public void setUp() {
		// reserved
	}

	public void tearDown() {
		// reserved
	}

	public void testSqlStringExpression() {
		log.debug("Testing SqlStringExpression");
		// constructing
		try {
			new SqlStringExpression("");
		} catch (Exception e) {
			fail("Constructing of SqlStringExpression failed with empty string");
		}
		try {
			new SqlStringExpression("a");
		} catch (Exception e) {
			fail("Constructing of SqlStringExpression failed with non-empty string");
		}
		try {
			new SqlStringExpression("a", new Object[0]);
		} catch (Exception e) {
			fail("Constructing of SqlStringExpression failed with non-empty string and an empty array");
		}
		try {
			new SqlStringExpression("a", new Object[] { "b", "c" });
		} catch (Exception e) {
			fail("Constructing of SqlStringExpression failed with non-empty string and two values");
		}
		try {
			new SqlStringExpression(null);
			fail("SqlStringExpression's string can not be null");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlStringExpression("a", null);
			fail("SqlStringExpression's values array can not be null");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlStringExpression must return an empty string",
				new SqlStringExpression("").toSqlString(), "");
		assertEquals("SqlStringExpression must return \"a\"",
				new SqlStringExpression("a").toSqlString(), "a");

		// SQL arguments
		assertTrue("SqlStringExpression must return an empty array",
				Arrays.equals(new SqlStringExpression("a", new Object[0])
						.getValues(), new Object[0]));
		assertTrue("SqlStringExpression must return (\"b\", \"c\") as values",
				Arrays.equals(new SqlStringExpression("a", new Object[] { "b",
						"c" }).getValues(), new Object[] { "b", "c" }));
	}

	public void testSqlValueExpression() {
		log.debug("Testing SqlValueExpression");
		// constructing
		try {
			new SqlValueExpression(null);
		} catch (Exception e) {
			fail("Constructing of SqlValueExpression failed with null-value");
		}
		try {
			new SqlValueExpression("a");
		} catch (Exception e) {
			fail("Constructing of SqlValueExpression failed with not-null value");
		}

		// SQL String
		assertEquals("SqlValueExpression must return ?",
				new SqlValueExpression(null).toSqlString(), "?");
		assertEquals("SqlValueExpression must return ?",
				new SqlValueExpression("a").toSqlString(), "?");

		// SQL arguments
		assertTrue("SqlValueExpression must return (null) as values", Arrays
				.equals(new SqlValueExpression(null).getValues(),
						new Object[] { null }));
		assertTrue("SqlValueExpression must return (\"a\") as values", Arrays
				.equals(new SqlValueExpression("a").getValues(),
						new Object[] { "a" }));
	}
}
