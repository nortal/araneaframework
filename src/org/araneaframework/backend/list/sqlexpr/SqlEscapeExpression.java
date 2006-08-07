package org.araneaframework.backend.list.sqlexpr;

import org.araneaframework.backend.list.SqlExpression;

public class SqlEscapeExpression implements SqlExpression {
	private SqlExpression expr;
	private String escapeChar;
	public SqlEscapeExpression(SqlExpression expr, String escapeChar) {
		if (expr == null) {
			throw new RuntimeException("SqlExpression must be provided");
		}
		if (escapeChar == null) {
			throw new RuntimeException("Escape character must be provided");
		}
		this.expr = expr;
		this.escapeChar = escapeChar;
	}
	public String toSqlString() {
		return new StringBuffer(this.expr.toSqlString()).append(" ESCAPE '").append(escapeChar).append("'").toString(); 		
	}
	public Object[] getValues() {
		return this.expr.getValues();
	}
}
