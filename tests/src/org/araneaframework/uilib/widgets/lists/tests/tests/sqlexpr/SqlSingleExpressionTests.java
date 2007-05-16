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
import org.araneaframework.backend.list.sqlexpr.SqlBracketsExpression;
import org.araneaframework.backend.list.sqlexpr.compare.SqlIsNullExpression;
import org.araneaframework.backend.list.sqlexpr.logical.SqlNotExpression;
import org.araneaframework.backend.list.sqlexpr.order.SqlAscendingExpression;
import org.araneaframework.backend.list.sqlexpr.order.SqlDescendingExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlLowerExpression;
import org.araneaframework.backend.list.sqlexpr.string.SqlUpperExpression;
import org.araneaframework.uilib.widgets.lists.tests.mock.MockSqlStringExpression;


public class SqlSingleExpressionTests extends TestCase {
	private static final Log log = LogFactory.getLog(SqlSingleExpressionTests.class);

	public void testSqlNotExpression() {
		log.debug("Testing SqlNotExpression");
		// constructing
		try {
			new SqlNotExpression(null);
			fail("Constructing of SqlNotExpression with NULL as SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlNotExpression must return \"NOT \"",
				new SqlNotExpression(new MockSqlStringExpression(""))
						.toSqlString(), "NOT ");
		assertEquals("SqlNotExpression must return \"NOT a\"",
				new SqlNotExpression(new MockSqlStringExpression("a"))
						.toSqlString(), "NOT a");

		// SQL arguments
		assertTrue("SqlNotExpression must return an empty array", Arrays
				.equals(new SqlNotExpression(new MockSqlStringExpression(""))
						.getValues(), new Object[0]));
		assertTrue("SqlNotExpression must return an empty array", Arrays
				.equals(new SqlNotExpression(new MockSqlStringExpression(
						new Object[0])).getValues(), new Object[0]));
		assertTrue("SqlNotExpression must return (\"a\") as values", Arrays
				.equals(new SqlNotExpression(new MockSqlStringExpression(
						new Object[] { "a" })).getValues(),
						new Object[] { "a" }));
	}

	public void testSqlUpperExpression() {
		log.debug("Testing SqlUpperExpression");
		// constructing
		try {
			new SqlUpperExpression(null);
			fail("Constructing of SqlUpperExpression with NULL as SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlUpperExpression must return \"UPPER()\"",
				new SqlUpperExpression(new MockSqlStringExpression(""))
						.toSqlString(), "UPPER()");
		assertEquals("SqlUpperExpression must return \"UPPER(a)\"",
				new SqlUpperExpression(new MockSqlStringExpression("a"))
						.toSqlString(), "UPPER(a)");

		// SQL arguments
		assertTrue("SqlUpperExpression must return an empty array", Arrays
				.equals(new SqlUpperExpression(new MockSqlStringExpression(""))
						.getValues(), new Object[0]));
		assertTrue("SqlUpperExpression must return an empty array", Arrays
				.equals(new SqlUpperExpression(new MockSqlStringExpression(
						new Object[0])).getValues(), new Object[0]));
		assertTrue("SqlUpperExpression must return (\"a\") as values", Arrays
				.equals(new SqlUpperExpression(new MockSqlStringExpression(
						new Object[] { "a" })).getValues(),
						new Object[] { "a" }));
	}

	public void testSqlLowerExpression() {
		log.debug("Testing SqlLowerExpression");
		// constructing
		try {
			new SqlLowerExpression(null);
			fail("Constructing of SqlUpperExpression with NULL as SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlLowerExpression must return \"LOWER()\"",
				new SqlLowerExpression(new MockSqlStringExpression(""))
						.toSqlString(), "LOWER()");
		assertEquals("SqlLowerExpression must return \"LOWER(a)\"",
				new SqlLowerExpression(new MockSqlStringExpression("a"))
						.toSqlString(), "LOWER(a)");

		// SQL arguments
		assertTrue("SqlLowerExpression must return an empty array", Arrays
				.equals(new SqlLowerExpression(new MockSqlStringExpression(""))
						.getValues(), new Object[0]));
		assertTrue("SqlLowerExpression must return an empty array", Arrays
				.equals(new SqlLowerExpression(new MockSqlStringExpression(
						new Object[0])).getValues(), new Object[0]));
		assertTrue("SqlLowerExpression must return (\"a\") as values", Arrays
				.equals(new SqlLowerExpression(new MockSqlStringExpression(
						new Object[] { "a" })).getValues(),
						new Object[] { "a" }));
	}

	public void testSqlIsNullExpression() {
		log.debug("Testing SqlIsNullExpression");
		// constructing
		try {
			new SqlIsNullExpression(null);
			fail("Constructing of SqlIsNullExpression with NULL as SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlIsNullExpression must return \" IS NULL\"",
				new SqlIsNullExpression(new MockSqlStringExpression(""))
						.toSqlString(), " IS NULL");
		assertEquals("SqlIsNullExpression must return \"a IS NULL\"",
				new SqlIsNullExpression(new MockSqlStringExpression("a"))
						.toSqlString(), "a IS NULL");

		// SQL arguments
		assertTrue("SqlIsNullExpression must return an empty array", Arrays
				.equals(new SqlIsNullExpression(new MockSqlStringExpression(""))
						.getValues(), new Object[0]));
		assertTrue("SqlIsNullExpression must return an empty array", Arrays
				.equals(new SqlIsNullExpression(new MockSqlStringExpression(
						new Object[0])).getValues(), new Object[0]));
		assertTrue("SqlIsNullExpression must return (\"a\") as values", Arrays
				.equals(new SqlIsNullExpression(new MockSqlStringExpression(
						new Object[] { "a" })).getValues(),
						new Object[] { "a" }));
	}

	public void testSqlAscendingExpression() {
		log.debug("Testing SqlAscendingExpression");
		// constructing
		try {
			new SqlAscendingExpression(null);
			fail("Constructing of SqlAscendingExpression with NULL as SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlAscendingExpression must return \" ASC\"",
				new SqlAscendingExpression(new MockSqlStringExpression(""))
						.toSqlString(), " ASC");
		assertEquals("SqlAscendingExpression must return \"a ASC\"",
				new SqlAscendingExpression(new MockSqlStringExpression("a"))
						.toSqlString(), "a ASC");

		// SQL arguments
		assertTrue("SqlAscendingExpression must return an empty array", Arrays
				.equals(new SqlAscendingExpression(new MockSqlStringExpression(""))
						.getValues(), new Object[0]));
		assertTrue("SqlAscendingExpression must return an empty array", Arrays
				.equals(new SqlAscendingExpression(new MockSqlStringExpression(
						new Object[0])).getValues(), new Object[0]));
		assertTrue("SqlAscendingExpression must return (\"a\") as values", Arrays
				.equals(new SqlAscendingExpression(new MockSqlStringExpression(
						new Object[] { "a" })).getValues(),
						new Object[] { "a" }));
	}

	public void testSqlDescendingExpression() {
		log.debug("Testing SqlDescendingExpression");
		// constructing
		try {
			new SqlDescendingExpression(null);
			fail("Constructing of SqlDescendingExpression with NULL as SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlDescendingExpression must return \" DESC\"",
				new SqlDescendingExpression(new MockSqlStringExpression(""))
						.toSqlString(), " DESC");
		assertEquals("SqlDescendingExpression must return \"a DESC\"",
				new SqlDescendingExpression(new MockSqlStringExpression("a"))
						.toSqlString(), "a DESC");

		// SQL arguments
		assertTrue("SqlDescendingExpression must return an empty array", Arrays
				.equals(new SqlDescendingExpression(new MockSqlStringExpression(""))
						.getValues(), new Object[0]));
		assertTrue("SqlDescendingExpression must return an empty array", Arrays
				.equals(new SqlDescendingExpression(new MockSqlStringExpression(
						new Object[0])).getValues(), new Object[0]));
		assertTrue("SqlDescendingExpression must return (\"a\") as values", Arrays
				.equals(new SqlDescendingExpression(new MockSqlStringExpression(
						new Object[] { "a" })).getValues(),
						new Object[] { "a" }));
	}

	public void testSqlParanthesesExpression() {
		log.debug("Testing SqlParanthesesExpression");
		// constructing
		try {
			new SqlBracketsExpression(null);
			fail("Constructing of SqlParanthesesExpression with NULL as SqlExpression argument should fail");
		} catch (Exception e) {
			// normal
		}

		// SQL String
		assertEquals("SqlParanthesesExpression must return \"()\"",
				new SqlBracketsExpression(new MockSqlStringExpression(""))
						.toSqlString(), "()");
		assertEquals("SqlParanthesesExpression must return \"(a)\"",
				new SqlBracketsExpression(new MockSqlStringExpression("a"))
						.toSqlString(), "(a)");

		// SQL arguments
		assertTrue("SqlParanthesesExpression must return an empty array", Arrays
				.equals(new SqlBracketsExpression(new MockSqlStringExpression(""))
						.getValues(), new Object[0]));
		assertTrue("SqlParanthesesExpression must return an empty array", Arrays
				.equals(new SqlBracketsExpression(new MockSqlStringExpression(
						new Object[0])).getValues(), new Object[0]));
		assertTrue("SqlParanthesesExpression must return (\"a\") as values", Arrays
				.equals(new SqlBracketsExpression(new MockSqlStringExpression(
						new Object[] { "a" })).getValues(),
						new Object[] { "a" }));
	}
}
