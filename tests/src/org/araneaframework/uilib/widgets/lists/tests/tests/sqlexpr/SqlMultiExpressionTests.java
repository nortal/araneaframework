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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.sqlexpr.SqlCollectionExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlAndExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlOrExpression;
import org.araneaframework.backend.list.sqlexpr.procedure.SqlProcedureExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlConcatenationExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockSqlStringExpression;


public class SqlMultiExpressionTests extends TestCase {
	private static final Log log = LogFactory.getLog(SqlMultiExpressionTests.class);

	public void testSqlAndExpression() {
		log.debug("Testing SqlAndExpression");
		// constructing
		try {
			new SqlAndExpression();
		} catch (Exception e) {
			fail("Constructing of SqlAndExpression failed");
		}

		// SQL String
		assertEquals("SqlAndExpression must return an empty string",
				new SqlAndExpression().toSqlString(), "");
		assertEquals("SqlAndExpression must return \"a\"",
				new SqlAndExpression().add(new MockSqlStringExpression("a"))
						.toSqlString(), "a");
		assertEquals("SqlAndExpression must return \"a AND b\"",
				new SqlAndExpression().add(new MockSqlStringExpression("a"))
						.add(new MockSqlStringExpression("b")).toSqlString(),
				"a AND b");

		// SQL arguments
		assertTrue("SqlAndExpression must return an empty array", Arrays
				.equals(new SqlAndExpression().getValues(), new Object[0]));
		assertTrue("SqlAndExpression must return an empty array",
				Arrays
						.equals(
								new SqlAndExpression().add(
										new MockSqlStringExpression(
												new Object[0])).add(
										new MockSqlStringExpression(
												new Object[0])).getValues(),
								new Object[0]));
		assertTrue("SqlAndExpression must return (\"a\", \"b\") as values",
				Arrays.equals(new SqlAndExpression().add(
						new MockSqlStringExpression(new Object[] { "a" })).add(
						new MockSqlStringExpression(new Object[] { "b" }))
						.getValues(), new Object[] { "a", "b" }));
	}

	public void testSqlOrExpression() {
		log.debug("Testing SqlOrExpression");
		// constructing
		try {
			new SqlOrExpression();
		} catch (Exception e) {
			fail("Constructing of SqlOrExpression failed");
		}

		// SQL String
		assertEquals("SqlOrExpression must return an empty string",
				new SqlOrExpression().toSqlString(), "");
		assertEquals("SqlOrExpression must return \"a\"", new SqlOrExpression()
				.add(new MockSqlStringExpression("a")).toSqlString(), "a");
		assertEquals("SqlOrExpression must return \"a AND b\"",
				new SqlOrExpression().add(new MockSqlStringExpression("a"))
						.add(new MockSqlStringExpression("b")).toSqlString(),
				"a OR b");

		// SQL arguments
		assertTrue("SqlOrExpression must return an empty array", Arrays.equals(
				new SqlOrExpression().getValues(), new Object[0]));
		assertTrue("SqlOrExpression must return an empty array",
				Arrays
						.equals(
								new SqlOrExpression().add(
										new MockSqlStringExpression(
												new Object[0])).add(
										new MockSqlStringExpression(
												new Object[0])).getValues(),
								new Object[0]));
		assertTrue("SqlOrExpression must return (\"a\", \"b\") as values",
				Arrays.equals(new SqlOrExpression().add(
						new MockSqlStringExpression(new Object[] { "a" })).add(
						new MockSqlStringExpression(new Object[] { "b" }))
						.getValues(), new Object[] { "a", "b" }));
	}

	public void testSqlConcatenationExpression() {
		log.debug("Testing SqlConcatenationExpression");
		// constructing
		try {
			new SqlConcatenationExpression();
		} catch (Exception e) {
			fail("Constructing of SqlConcatenationExpression failed");
		}

		// SQL String
		assertEquals("SqlConcatenationExpression must return an empty string",
				new SqlConcatenationExpression().toSqlString(), "");
		assertEquals("SqlConcatenationExpression must return \"a\"",
				new SqlConcatenationExpression().add(
						new MockSqlStringExpression("a")).toSqlString(), "a");
		assertEquals("SqlConcatenationExpression must return \"a || b\"",
				new SqlConcatenationExpression().add(
						new MockSqlStringExpression("a")).add(
						new MockSqlStringExpression("b")).toSqlString(),
				"a || b");

		// SQL arguments
		assertTrue("SqlConcatenationExpression must return an empty array",
				Arrays.equals(new SqlConcatenationExpression().getValues(),
						new Object[0]));
		assertTrue("SqlConcatenationExpression must return an empty array",
				Arrays
						.equals(
								new SqlConcatenationExpression().add(
										new MockSqlStringExpression(
												new Object[0])).add(
										new MockSqlStringExpression(
												new Object[0])).getValues(),
								new Object[0]));
		assertTrue(
				"SqlConcatenationExpression must return (\"a\", \"b\") as values",
				Arrays.equals(new SqlConcatenationExpression().add(
						new MockSqlStringExpression(new Object[] { "a" })).add(
						new MockSqlStringExpression(new Object[] { "b" }))
						.getValues(), new Object[] { "a", "b" }));
	}

	public void testSqlCollectionExpression() {
		log.debug("Testing SqlCollectionExpression");
		// constructing
		try {
			new SqlCollectionExpression();
		} catch (Exception e) {
			fail("Constructing of SqlCollectionExpression failed");
		}

		// SQL String
		assertEquals("SqlCollectionExpression must return an empty string",
				new SqlCollectionExpression().toSqlString(), "");
		assertEquals("SqlCollectionExpression must return \"a\"",
				new SqlCollectionExpression().add(
						new MockSqlStringExpression("a")).toSqlString(), "a");
		assertEquals("SqlCollectionExpression must return \"a, b\"",
				new SqlCollectionExpression().add(
						new MockSqlStringExpression("a")).add(
						new MockSqlStringExpression("b")).toSqlString(), "a, b");

		// SQL arguments
		assertTrue("SqlCollectionExpression must return an empty array", Arrays
				.equals(new SqlCollectionExpression().getValues(),
						new Object[0]));
		assertTrue("SqlCollectionExpression must return an empty array",
				Arrays
						.equals(
								new SqlCollectionExpression().add(
										new MockSqlStringExpression(
												new Object[0])).add(
										new MockSqlStringExpression(
												new Object[0])).getValues(),
								new Object[0]));
		assertTrue(
				"SqlCollectionExpression must return (\"a\", \"b\") as values",
				Arrays.equals(new SqlCollectionExpression().add(
						new MockSqlStringExpression(new Object[] { "a" })).add(
						new MockSqlStringExpression(new Object[] { "b" }))
						.getValues(), new Object[] { "a", "b" }));
	}

	public void testSqlProcedureExpression() {
		log.debug("Testing SqlProcedureExpression");
		// constructing
		try {
			new SqlProcedureExpression(null);
			fail("Constructing of SqlProcedureExpression with NULL as name should fail");
		} catch (Exception e) {
			// normal
		}
		try {
			new SqlProcedureExpression("a");
		} catch (Exception e) {
			fail("Constructing of SqlProcedureExpression with not-null name failed");
		}

		// SQL String
		assertEquals("SqlProcedureExpression must return \"a()\"",
				new SqlProcedureExpression("a").toSqlString(), "a()");
		assertEquals("SqlProcedureExpression must return \"a(b)\"",
				new SqlProcedureExpression("a").add(
						new MockSqlStringExpression("b")).toSqlString(), "a(b)");
		assertEquals("SqlProcedureExpression must return \"a(b, c)\"",
				new SqlProcedureExpression("a").add(
						new MockSqlStringExpression("b")).add(
						new MockSqlStringExpression("c")).toSqlString(),
				"a(b, c)");

		// SQL arguments
		assertTrue("SqlProcedureExpression must return an empty array",
				Arrays.equals(new SqlProcedureExpression("x").getValues(),
						new Object[0]));
		assertTrue("SqlProcedureExpression must return an empty array",
				Arrays
						.equals(
								new SqlProcedureExpression("x").add(
										new MockSqlStringExpression(
												new Object[0])).add(
										new MockSqlStringExpression(
												new Object[0])).getValues(),
								new Object[0]));
		assertTrue(
				"SqlProcedureExpression must return (\"a\", \"b\") as values",
				Arrays.equals(new SqlProcedureExpression("x").add(
						new MockSqlStringExpression(new Object[] { "a" })).add(
						new MockSqlStringExpression(new Object[] { "b" }))
						.getValues(), new Object[] { "a", "b" }));
	}

}
