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

package org.araneaframework.uilib.list.util.converter;

import org.araneaframework.uilib.list.util.Converter;

/**
 * Converter that leaves <code>null</code> values unaltered and uses a another
 * not-null <code>converter</code> for all other cases.
 */
public class NullConverter implements Converter {

	protected Converter notNullConverter;

	public NullConverter(Converter notNullConverter) {
		this.notNullConverter = notNullConverter;
	}

	public Object convert(Object data) throws ConversionException {
		if (data == null) {
			return null;
		}
		return this.notNullConverter.convert(data);
	}

	public Object reverseConvert(Object data) throws ConversionException {
		if (data == null) {
			return null;
		}
		return this.notNullConverter.reverseConvert(data);
	}

	public Class getSourceType() {
		return this.notNullConverter.getSourceType();
	}

	public Class getDestinationType() {
		return this.notNullConverter.getDestinationType();
	}

}
