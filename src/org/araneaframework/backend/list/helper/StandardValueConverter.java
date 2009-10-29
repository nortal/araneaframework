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

package org.araneaframework.backend.list.helper;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.backend.list.helper.builder.ValueConverter;
import org.araneaframework.backend.list.memorybased.expression.Value;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.list.util.Converter;

/**
 * ValueConverter that has a map of Value names and their <code>Converter</code> objects.
 * 
 * @see Converter
 * @author Rein Raudjärv
 * @since 1.1
 */
public class StandardValueConverter implements ValueConverter {

  /**
   * Value name --&gt; Converter that is used by convert() method.
   */
  private final Map<String, Converter<Object, Object>> converters = new HashMap<String, Converter<Object, Object>>();

  @SuppressWarnings("unchecked")
  public void addConverter(String valueName, Converter converter) {
    Assert.notNullParam(converter, "converter");
    this.converters.put(valueName, converter);
  }

  @SuppressWarnings("unchecked")
  public <S, D> D convert(Value<S> value) {
    // Find Converter
    Converter<Object, Object> converter = this.converters.get(value.getName());
    if (converter == null) {
      // No Converter registered
      return (D) value.getValue();
    }
    // Convert
    return (D) converter.convert(value.getValue());
  }
}
