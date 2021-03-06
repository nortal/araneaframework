/*
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
 */

package org.araneaframework.uilib.widgets.lists.tests.mock;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.uilib.list.util.Converter;


public class MockValueConverter implements ValueConverter {
	protected Map<String, Converter<?, ?>> converters;

	public MockValueConverter(Map<String, Converter<?, ?>> converters) {
		this.converters = converters;
	}

	public MockValueConverter(String name, Converter<?, ?> converter) {
		this.converters = new HashMap<String, Converter<?,?>>();
		this.converters.put(name, converter);
	}

	public MockValueConverter() {
		this.converters = new HashMap<String, Converter<?,?>>();
	}

	@SuppressWarnings("unchecked")
  public <S, D> D convert(Value<S> value) {
		Converter<S, D> converter = (Converter<S, D>) this.converters.get(value.getName());
		if (converter != null) {
			return converter.convert(value.getValue());
		}
		return (D) value.getValue();
	}
}
