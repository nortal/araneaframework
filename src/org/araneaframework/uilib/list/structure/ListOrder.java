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

package org.araneaframework.uilib.list.structure;

import java.io.Serializable;

import org.araneaframework.Environment;
import org.araneaframework.backend.list.memorybased.ComparatorExpression;
import org.araneaframework.uilib.list.OrderInfo;


/**
 * Static list ordering information that can be used along with
 * <code>OrderInfo</code> to build <code>OrderExpression</code> (an
 * <code>ComparatorExpression</code> that compares the values that two
 * <code>VariableResolvers</code> provides).
 * 
 * @see org.araneaframework.uilib.list.OrderInfo
 */
public interface ListOrder extends Serializable {
	
	void init(Environment env) throws Exception;
	void destroy() throws Exception;

	/**
	 * Builds <code>ComparatorExpression</code> from this
	 * <code>ListOrder</code> according to the <code>OrderInfo</code>.
	 * 
	 * @param orderInfo
	 *            Current ordered fields and their directions.
	 * @return <code>ComparatorExpression</code> that is built according to
	 *         the <code>OrderInfo</code>.
	 */
	ComparatorExpression buildComparatorExpression(OrderInfo orderInfo);
}
