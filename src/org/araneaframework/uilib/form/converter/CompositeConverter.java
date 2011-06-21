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

package org.araneaframework.uilib.form.converter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElementContext;

/**
 * Represents a converter that is composed of multiple converters. This allows reuse of existing converters to simplify
 * creation of different converters.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class CompositeConverter extends BaseConverter<Object, Object> {

  /**
   * All internal converters used for converting.
   */
  protected List<Converter> converters = new LinkedList<Converter>();

  public CompositeConverter() {}

  /**
   * Initializes composite converter with given converters. The converters will be processed in the order they are
   * provided here.
   *  
   * @param converters Converters to use in the given to reach value conversion to the correct type.
   */
  public CompositeConverter(Converter... converters) {
    this.converters.addAll(Arrays.asList(converters));
  }

  /**
   * Initializes composite converter with given converters. The converters will be processed in the order they are
   * provided here.
   *  
   * @param converters Converters to use in the given to reach value conversion to the correct type.
   */
  public CompositeConverter(Collection<Converter> converters) {
    this.converters.addAll(converters);
  }

  @Override
  protected Object convertNotNull(Object data) {
    for (Converter converter : this.converters) {
      data = converter.convert(data);
    }
    return data;
  }

  @Override
  protected Object reverseConvertNotNull(Object data) {
    ListIterator<Converter> i = this.converters.listIterator(this.converters.size() - 1);
    while(i.hasPrevious()) {
      data = i.previous().reverseConvert(data);
    }
    return data;
  }

  @Override
  public Converter newConverter() {
    return new CompositeConverter(this.converters);
  }

  @Override
  public void setFormElementCtx(FormElementContext feCtx) {
    super.setFormElementCtx(feCtx);
    for (Converter converter : this.converters) {
      converter.setFormElementCtx(feCtx);
    }
  }
}
