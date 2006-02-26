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

package org.araneaframework.uilib.widgets.lists.tests.mock;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.uilib.widgets.lists.backend.helper.builder.ValueConverter;
import org.araneaframework.uilib.widgets.lists.presentation.memorybased.expression.Value;
import org.araneaframework.uilib.widgets.lists.util.Converter;


public class MockValueConverter implements ValueConverter {
	protected Map converters;

	public MockValueConverter(Map converters) {
		this.converters = converters;
	}

	public MockValueConverter(String name, Converter converter) {
		this.converters = new HashMap();
		this.converters.put(name, converter);
	}

	public MockValueConverter() {
		this.converters = new HashMap();
	}

	public Object convert(Value value) {
		Converter converter = (Converter) this.converters.get(value.getName());
		if (converter != null) {
			return converter.convert(value.getValue());
		}
		return value.getValue();
	}
}
