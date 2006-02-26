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

import org.apache.log4j.Logger;
import org.araneaframework.backend.list.memorybased.Expression;
import org.araneaframework.backend.list.memorybased.expression.logical.OrExpression;
import org.araneaframework.uilib.list.structure.ListFilter;
import org.araneaframework.uilib.list.structure.filter.MultiFilter;


public class OrFilter extends MultiFilter {
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(OrFilter.class);
	
	public Expression buildExpression(Map data) {
		log.debug("Building Expression, data = " + data);
		OrExpression expr = new OrExpression();
		for (Iterator i = this.children.iterator(); i.hasNext();) {
			ListFilter filter = (ListFilter) i.next();
			Expression childExpr = filter.buildExpression(data); 
			expr.add(childExpr);
		}
		return expr;
	}
}
