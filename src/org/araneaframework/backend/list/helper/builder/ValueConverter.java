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

package org.araneaframework.backend.list.helper.builder;

import org.araneaframework.backend.list.memorybased.expression.Value;

/**
 * Converter that is used to convert <code>Values</code>. This is generally
 * used to convert different values according to their names.
 * 
 * @see org.araneaframework.backend.list.memorybased.expression.Value
 * @see org.araneaframework.uilib.list.util.Converter
 */
public interface ValueConverter {
	/**
	 * Converts the <code>Value</code>.
	 * 
	 * @param value
	 *            the <code>Value</code>.
	 * @return the converted value.
	 */
	Object convert(Value value);
}
