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

import org.araneaframework.Environment;
import org.araneaframework.backend.list.memorybased.ExpressionBuilder;


/**
 * Static list filtering information that can be used along with
 * <code>FilterInfo</code> to build <code>FilterExpression</code> (an
 * <code>Expression</code> that evualuates into <code>Boolean.TRUE</code>
 * when the filter matches with the current record that a
 * <code>VariableResolver</code> provides and <code>Boolean.FALSE</code>
 * otherwise).
 */
public interface ListFilter extends ExpressionBuilder {
	void init(Environment env) throws Exception;
	void destroy() throws Exception;
}
