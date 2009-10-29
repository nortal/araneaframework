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

package org.araneaframework.uilib.list.structure.order;

import java.util.Comparator;
import org.araneaframework.Environment;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.VariableComparatorExpression;
import org.araneaframework.uilib.list.OrderInfo;


/**
 * @author Rein Raudjärv
 */
public class SimpleFieldOrder implements FieldOrder {

	private static final long serialVersionUID = 1L;

	private String fieldId;
	private Comparator comparator;
	
	public SimpleFieldOrder(String fieldId, Comparator comparator) {
		setFieldId(fieldId);
		setComparator(comparator);
	}
	
	public SimpleFieldOrder(String fieldId) {
		this(fieldId, null);
	}	
	
	public String getFieldId() {
		return this.fieldId;
	}
	
	public void setFieldId(String id) {
		this.fieldId = id;
	}
	
	public Comparator getComparator() {
		return comparator;
	}

	public void setComparator(Comparator comparator) {
		this.comparator = comparator;
	}

	public void init(Environment env)  {}	
	
	public void destroy() {}

	public ComparatorExpression buildComparatorExpression(OrderInfo orderInfo) {
		if (this.fieldId == null) {
			throw new RuntimeException("Column Id must be provided"); 
		}
		return new VariableComparatorExpression(getFieldId(), getComparator());
	}
}
