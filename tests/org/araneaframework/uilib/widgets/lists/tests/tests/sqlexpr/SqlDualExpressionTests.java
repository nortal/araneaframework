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
import org.araneaframework.uilib.widgets.lists.backend.sqlexpr.compare.SqlEqualsExpression;
import org.araneaframework.uilib.widgets.lists.backend.sqlexpr.compare.SqlGreaterThanExpression;
import org.araneaframework.uilib.widgets.lists.backend.sqlexpr.compare.SqlLikeExpression;
import org.araneaframework.uilib.widgets.lists.backend.sqlexpr.compare.SqlLowerThanExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockSqlStringExpression;


public class SqlDualExpressionTests extends TestCase {
	private static final Logger log = Logger
			.getLogger(SqlDualExpressionTests.class);

	public void testSqlEqualsExpression() {
		log.debug("Testing SqlEqualsExpression");
		// constructing
		try {
			new SqlEqualsExpression(null, null);
			fail("Constructing of SqlEqualsExpression with NULL as SqlExpression arguments should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlEqualsExpression(null, new MockSqlStringExpression("a"));
			fail("Constructing of SqlEqualsExpression with NULL as first SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlEqualsExpression(new MockSqlStringExpression("a"), null);
			fail("Constructing of SqlEqualsExpression with NULL as second SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlEqualsExpression must return \" = \"",
				new SqlEqualsExpression(new MockSqlStringExpression(""),
						new MockSqlStringExpression("")).toSqlString(), " = ");
		assertEquals("SqlEqualsExpression must return \"a = b\"",
				new SqlEqualsExpression(new MockSqlStringExpression("a"),
						new MockSqlStringExpression("b")).toSqlString(),
				"a = b");

		// SQL arguments
		assertTrue("SqlEqualsExpression must return an empty array", Arrays
				.equals(new SqlEqualsExpression(
						new MockSqlStringExpression(""),
						new MockSqlStringExpression("")).getValues(),
						new Object[0]));
		assertTrue("SqlEqualsExpression must return an empty array", Arrays
				.equals(new SqlEqualsExpression(new MockSqlStringExpression(
						new Object[] { "a" }), new MockSqlStringExpression(
						new Object[] { "b" })).getValues(), new Object[] { "a",
						"b" }));
	}

	public void testSqlGreaterThanExpression() {
		log.debug("Testing SqlGreaterThanExpression");
		// constructing
		try {
			new SqlGreaterThanExpression(null, null);
			fail("Constructing of SqlGreaterThanExpression with NULL as SqlExpression arguments should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlGreaterThanExpression(null, new MockSqlStringExpression("a"));
			fail("Constructing of SqlGreaterThanExpression with NULL as first SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlGreaterThanExpression(new MockSqlStringExpression("a"), null);
			fail("Constructing of SqlGreaterThanExpression with NULL as second SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlGreaterThanExpression must return \" > \"",
				new SqlGreaterThanExpression(new MockSqlStringExpression(""),
						new MockSqlStringExpression("")).toSqlString(), " > ");
		assertEquals("SqlGreaterThanExpression must return \"a > b\"",
				new SqlGreaterThanExpression(new MockSqlStringExpression("a"),
						new MockSqlStringExpression("b")).toSqlString(),
				"a > b");

		// SQL arguments
		assertTrue("SqlGreaterThanExpression must return an empty array",
				Arrays.equals(new SqlGreaterThanExpression(
						new MockSqlStringExpression(""),
						new MockSqlStringExpression("")).getValues(),
						new Object[0]));
		assertTrue("SqlGreaterThanExpression must return an empty array",
				Arrays.equals(new SqlGreaterThanExpression(
						new MockSqlStringExpression(new Object[] { "a" }),
						new MockSqlStringExpression(new Object[] { "b" }))
						.getValues(), new Object[] { "a", "b" }));
	}

	public void testSqlLowerThanExpression() {
		log.debug("Testing SqlLowerThanExpression");
		// constructing
		try {
			new SqlLowerThanExpression(null, null);
			fail("Constructing of SqlLowerThanExpression with NULL as SqlExpression arguments should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlLowerThanExpression(null, new MockSqlStringExpression("a"));
			fail("Constructing of SqlLowerThanExpression with NULL as first SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlLowerThanExpression(new MockSqlStringExpression("a"), null);
			fail("Constructing of SqlLowerThanExpression with NULL as second SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlLowerThanExpression must return \" < \"",
				new SqlLowerThanExpression(new MockSqlStringExpression(""),
						new MockSqlStringExpression("")).toSqlString(), " < ");
		assertEquals("SqlLowerThanExpression must return \"a < b\"",
				new SqlLowerThanExpression(new MockSqlStringExpression("a"),
						new MockSqlStringExpression("b")).toSqlString(),
				"a < b");

		// SQL arguments
		assertTrue("SqlLowerThanExpression must return an empty array", Arrays
				.equals(new SqlLowerThanExpression(new MockSqlStringExpression(
						""), new MockSqlStringExpression("")).getValues(),
						new Object[0]));
		assertTrue("SqlLowerThanExpression must return an empty array", Arrays
				.equals(new SqlLowerThanExpression(new MockSqlStringExpression(
						new Object[] { "a" }), new MockSqlStringExpression(
						new Object[] { "b" })).getValues(), new Object[] { "a",
						"b" }));
	}

	public void testSqlLikeExpression() {
		log.debug("Testing SqlLikeExpression");
		// constructing
		try {
			new SqlLikeExpression(null, null);
			fail("Constructing of SqlLikeExpression with NULL as SqlExpression arguments should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlLikeExpression(null, new MockSqlStringExpression("a"));
			fail("Constructing of SqlLikeExpression with NULL as first SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlLikeExpression(new MockSqlStringExpression("a"), null);
			fail("Constructing of SqlLikeExpression with NULL as second SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlLikeExpression must return \" LIKE \"",
				new SqlLikeExpression(new MockSqlStringExpression(""),
						new MockSqlStringExpression("")).toSqlString(),
				" LIKE ");
		assertEquals("SqlLikeExpression must return \"a LIKE b\"",
				new SqlLikeExpression(new MockSqlStringExpression("a"),
						new MockSqlStringExpression("b")).toSqlString(),
				"a LIKE b");

		// SQL arguments
		assertTrue("SqlLikeExpression must return an empty array", Arrays
				.equals(new SqlLikeExpression(new MockSqlStringExpression(""),
						new MockSqlStringExpression("")).getValues(),
						new Object[0]));
		assertTrue("SqlLikeExpression must return an empty array", Arrays
				.equals(new SqlLikeExpression(new MockSqlStringExpression(
						new Object[] { "a" }), new MockSqlStringExpression(
						new Object[] { "b" })).getValues(), new Object[] { "a",
						"b" }));
	}
}
