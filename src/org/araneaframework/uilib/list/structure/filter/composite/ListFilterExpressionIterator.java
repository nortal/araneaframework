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
package org.araneaframework.uilib.list.structure.filter.composite;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.uilib.list.structure.ListFilter;

/**
 * Iterator that iterates over {@link Expression}s using an iterator over
 * {@link ListFilter} objects and filter data.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class ListFilterExpressionIterator implements Iterator, Serializable {
	
	private Iterator filterIterator;
	private Map filterData;
	
	public ListFilterExpressionIterator(Iterator filterIterator, Map filterData) {
		Validate.notNull(filterIterator);
		Validate.notNull(filterData);
		this.filterIterator = filterIterator;
		this.filterData = filterData;
	}

	public boolean hasNext() {
		return filterIterator.hasNext();
	}

	public Object next() {
		ListFilter listFilter = (ListFilter) filterIterator.next();
		return listFilter.buildExpression(filterData);
	}

	public void remove() {
		throw new UnsupportedOperationException("Remove is not supported");
	}

}
