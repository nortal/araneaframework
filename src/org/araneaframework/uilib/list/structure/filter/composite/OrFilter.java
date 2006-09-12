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

import java.util.Iterator;
import java.util.Map;

import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.filter.MultiFilter;


public class OrFilter extends MultiFilter {
	private static final long serialVersionUID = 1L;
	
	public Expression buildExpression(Map data) {
		OrExpression or = new OrExpression();
		int count = 0;
		for (Iterator i = this.children.iterator(); i.hasNext();) {
			ListFilter filter = (ListFilter) i.next();
			Expression child = filter.buildExpression(data);
			if (child != null) {
				or.add(child);
				count++;
			}
		}
		if (count == 0) {
			return null;
		}
		return or;
	}
}
