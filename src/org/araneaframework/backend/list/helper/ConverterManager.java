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

package org.araneaframework.backend.list.helper;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.uilib.list.util.Converter;


/**
 * ValueConverter that has a map of Value names and their <code>Converter</code>
 * objects. It also has a global <code>Converter</code> which is used by
 * default.
 */
public class ConverterManager implements ValueConverter {
	private Converter globalConverter = null;

	private Map converters = new HashMap();

	public void addGlobalConverter(Converter converter) {
		this.globalConverter = converter;
	}

	public void addConverter(String valueName, Converter converter) {
		this.converters.put(valueName, converter);
	}

	public Object convert(Value value) {
		if (this.globalConverter == null && this.converters.size() == 0) {
			throw new RuntimeException("No converters provided");
		}
		String name = value.getName();
		if (name != null) {
			Converter converter = (Converter) this.converters.get(name);
			if (converter != null) {
				return converter.convert(value.getValue());
			}
		}
		if (this.globalConverter != null) {
			return this.globalConverter.convert(value.getValue());
		}
		throw new RuntimeException("Either converter for value with name "
				+ name + " nor global converter provided");
	}
}
