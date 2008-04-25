package org.araneaframework.backend.list.helper;

import java.util.Iterator;
import java.util.List;
import javax.sql.DataSource;
import org.araneaframework.backend.list.SqlExpression;
import org.araneaframework.backend.list.helper.builder.expression.PostgreExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.helper.builder.expression.StandardExpressionToSqlExprBuilder;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.backend.list.sqlexpr.SqlCollectionExpression;
import org.araneaframework.backend.list.sqlexpr.constant.SqlStringExpression;

/**
 * Extends the <code>ListSqLHelper</code> to support PostgreSQL database
 * queries.
 * 
 * @author Roman Tekhov
 * @since 1.1.3
 */
public class PostgreListSqlHelper extends ListSqlHelper {

	protected SqlStatement statement = new SqlStatement();

	protected String countSqlQuery = null;

	public PostgreListSqlHelper(DataSource dataSource, ListQuery query) {
		super(dataSource, query);
	}

	public PostgreListSqlHelper(DataSource dataSource) {
		super(dataSource);
	}

	public PostgreListSqlHelper(ListQuery query) {
		super(query);
	}

	public PostgreListSqlHelper() {
	}

	protected SqlStatement getCountSqlStatement() {
		if (countSqlQuery != null) {
			return new SqlStatement(countSqlQuery, statement.getParams());
		}

		String temp = new StringBuffer("SELECT COUNT(*) FROM (").append(
				statement.getQuery()).append(") t").toString();

		return new SqlStatement(temp, this.statement.getParams());
	}

	protected SqlStatement getRangeSqlStatement() {
		StringBuffer sb = new StringBuffer(this.statement.getQuery());

		if (this.itemRangeCount != null) {
			sb.append(" LIMIT ?");
		}

		sb.append(" OFFSET ?");

		// Create a SQL statement to hold the query and its parameters:
		SqlStatement rangeStmt = new SqlStatement(sb.toString());
		rangeStmt.addAllParams(this.statement.getParams());
		if (this.itemRangeCount != null) {
			rangeStmt.addParam(this.itemRangeCount);
		}
		rangeStmt.addParam(this.itemRangeStart);

		return rangeStmt;
	}

	protected SqlExpression getFieldsSqlExpression() {
		SqlCollectionExpression result = new SqlCollectionExpression();

		for (Iterator it = fields.getNames().iterator(); it.hasNext();) {
			String variable = (String) it.next();
			String dbField = namingStrategy.fieldToColumnName(variable);
			String dbAlias = namingStrategy.fieldToColumnAlias(variable);

			if (dbAlias.equals(dbField)) {
				result.add(new SqlStringExpression(dbField));
			} else {
				result.add(new SqlStringExpression(new StringBuffer(dbField).append(
						" AS ").append(dbAlias).toString()));
			}
		}

		return result;
	}

	protected StandardExpressionToSqlExprBuilder createFilterSqlExpressionBuilder() {
		return new PostgreExpressionToSqlExprBuilder();
	}

	public void setCountSqlQuery(String countSqlQuery) {
		this.countSqlQuery = countSqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.statement.setQuery(sqlQuery);
	}

	public void addNullParam(int valueType) {
		this.statement.addNullParam(valueType);
	}

	public void addStatementParam(Object param) {
		this.statement.addParam(param);
	}

	public void addStatementParams(List params) {
		this.statement.addAllParams(params);
	}

}
