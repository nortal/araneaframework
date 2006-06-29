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

package org.araneaframework.uilib.list.structure.filter.column;

import java.util.Map;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.AlwaysTrueExpression;
import org.araneaframework.backend.list.memorybased.expression.compare.IsNullExpression;
import org.araneaframework.backend.list.memorybased.expression.variable.VariableExpression;
import org.araneaframework.uilib.list.structure.ComparableType;
import org.araneaframework.uilib.list.structure.filter.ColumnFilter;

public class IsNullColumnFilter extends ComparableType implements ColumnFilter {

	private static final long serialVersionUID = 1L;
	
	public static final Object DEFAULT_FILTER_INFO_VALUE = Boolean.TRUE;
	
	private String columnId;
	private String filterInfoKey;
	private Object filterInfoValue = DEFAULT_FILTER_INFO_VALUE;
	
	public IsNullColumnFilter(String columnId) {
		setColumnId(columnId);
	}
	
	public IsNullColumnFilter() {
		// empty constructor
	}

	public String getColumnId() {
		return this.columnId;
	}

	public void setColumnId(String columnId) {
		if (columnId == null) {
			throw new RuntimeException("Column Id must be not null");
		}
		this.columnId = columnId;
		if (this.filterInfoKey == null) {
			this.filterInfoKey = columnId;
		}
	}

	public String getFilterInfoKey() {
		return this.filterInfoKey;
	}

	public void setFilterInfoKey(String filterInfoKey) {
		this.filterInfoKey = filterInfoKey;
	}

	public Object getFilterInfoValue() {
		return this.filterInfoValue;
	}

	public void setFilterInfoValue(Object filterInfoValue) {
		this.filterInfoValue = filterInfoValue;
	}

	public Expression buildExpression(Map filterInfo) {
		if (this.columnId == null) {
			throw new RuntimeException("Column Id must be provided"); 
		}
		if (!filterInfo.containsKey(this.filterInfoKey)) {
			throw new RuntimeException("FilterInfo does not contain the specified key");
		}
		Object value = filterInfo.get(this.filterInfoKey);
		boolean isNull = this.filterInfoValue == null ? value == null : this.filterInfoValue.equals(value);
		if (!isNull) {
			return new AlwaysTrueExpression();
		}
		return new IsNullExpression(new VariableExpression(this.columnId));
	} 
}
