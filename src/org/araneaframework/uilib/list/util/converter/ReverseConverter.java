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
 * Converter that reverses the converting direction of another
 * <code>converter</code>.
 */
public class ReverseConverter implements Converter {
	protected Converter converter;

	public ReverseConverter(Converter converter) {
		this.converter = converter;
	}

	public Object convert(Object data) throws ConvertionException {
		return this.converter.reverseConvert(data);
	}

	public Object reverseConvert(Object data) throws ConvertionException {
		return this.converter.convert(data);
	}

	public Class getSourceType() {
		return this.converter.getDestinationType();
	}

	public Class getDestinationType() {
		return this.converter.getSourceType();
	}
}
