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

package org.araneaframework.uilib.list.util;

import java.io.Serializable;

import org.araneaframework.uilib.list.util.converter.ConvertionException;

/**
 * Data converter between <code>source</code> and <code>destination</code>
 * types.
 */
public interface Converter extends Serializable {
	/**
	 * Converts data from source type into destination type.
	 * 
	 * @param data
	 *            Source typed data.
	 * @return Destination typed data.
	 * @throws ConvertionException
	 *             when convertion fails.
	 */
	Object convert(Object data) throws ConvertionException;

	/**
	 * Converts data from destination type into source type.
	 * 
	 * @param data
	 *            Destination typed data.
	 * @return Source typed data.
	 * @throws ConvertionException
	 *             when convertion fails.
	 */
	Object reverseConvert(Object data) throws ConvertionException;

	/**
	 * Returns the source data type.
	 * 
	 * @return the source data type.
	 */
	Class getSourceType();

	/**
	 * Returns the destination data type.
	 * 
	 * @return the destination data type.
	 */
	Class getDestinationType();
}
