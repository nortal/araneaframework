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

package org.araneaframework.uilib.list.structure.order;

import java.util.Comparator;
import java.util.Locale;

import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.backend.list.memorybased.compexpr.FieldComparatorExpression;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.structure.ListContext;
import org.araneaframework.uilib.list.structure.ListOrder;
import org.araneaframework.uilib.list.util.CompareHelper;
import org.araneaframework.uilib.list.util.comparator.ComparatorInfo;
import org.araneaframework.uilib.list.util.comparator.StringComparatorInfo;


/**
 * ListOrder of one field.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class FieldOrder implements ListOrder {

	private static final long serialVersionUID = 1L;

	private String fieldId;
	
	private Locale locale;
	private Boolean ignoreCase;
	
	private Comparator comparator;	
	private ComparatorInfo comparatorInfo;
	
	public String getFieldId() {
		return this.fieldId;
	}
	
	public void setFieldId(String id) {
		Validate.isTrue(fieldId == null, "Field id can not be changed");
		this.fieldId = id;
	}
	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setIgnoreCase(Boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	public void setComparator(Comparator comparator, ComparatorInfo info) {
		this.comparator = comparator;
		this.comparatorInfo = info;
	}

	public void init(ListContext context) {
		Validate.notNull(fieldId, "No field id specified");
		if (this.comparator == null) {
			Class type = context.resolveType(fieldId);
			Locale locale = this.locale != null ? this.locale : context.getLocale();
			Boolean ignoreCase = this.ignoreCase != null ? this.ignoreCase : Boolean.valueOf(CompareHelper.IGNORE_CASE_BY_DEFAULT);
			if (String.class.equals(type)) {
				this.comparatorInfo = new StringComparatorInfo(locale, ignoreCase.booleanValue()); 
			}
			this.comparator = new CompareHelper(type, context.getConfiguration(),
					locale, ignoreCase).getComparator();
		}
	}

	public ComparatorExpression buildExpression(OrderInfo orderInfo) {
		return new FieldComparatorExpression(getFieldId(), this.comparator,
				this.comparatorInfo);
	}
}
