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

package org.araneaframework.backend.list.memorybased.compexpr;

import java.util.Comparator;

import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.Resolver;
import org.araneaframework.uilib.list.util.comparator.ComparatorInfo;


/**
 * {@link ComparatorExpression} that compares two different values (resolved
 * from two {@link Resolver}s) of one field.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class FieldComparatorExpression implements ComparatorExpression {

	private static final long serialVersionUID = 1L;
	
	protected String fieldId;

	protected Comparator comparator;
	protected ComparatorInfo comparatorInfo;

	public FieldComparatorExpression(String fieldId, Comparator comparator,
			ComparatorInfo info) {
		Validate.notNull(fieldId, "No field Id specified");
		Validate.notNull(comparator, "No comparator specified");

		this.fieldId = fieldId;
		this.comparator = comparator;
		this.comparatorInfo = info;
	}


	public Comparator getComparator() {
		return comparator;
	}

	public ComparatorInfo getComparatorInfo() {
		return comparatorInfo;
	}

	public String getFieldId() {
		return fieldId;
	}

	public int compare(Resolver resolver1, Resolver resolver2) {
		Object value1 = resolver1.resolve(this.fieldId);
		Object value2 = resolver2.resolve(this.fieldId);
		int result = this.comparator.compare(value1, value2);		
		return result;
	}
}
