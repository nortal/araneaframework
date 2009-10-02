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

import java.math.BigInteger;

import java.util.Date;

import java.sql.Timestamp;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.support.ConverterKey;

/**
 * This class is a Factory pattern implementation, that provides methods to make
 * {@link org.araneaframework.uilib.form.FormElement}s and {@link org.araneaframework.uilib.form.converter.BaseConverter}s.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org) 
 */
public class ConverterFactory implements ConverterProvider {

  /** @since 1.1 */
  public static final ConverterProvider DEFAULT_CONVERTER_FACTORY = new ConverterFactory();

  @SuppressWarnings("unchecked")
  protected final Map<ConverterKey, Converter> converters = new HashMap<ConverterKey, Converter>();

  protected ConverterFactory() {
    // String -> Type
    add(String.class, Boolean.class, new StringToBooleanConverter());
    add(String.class, Long.class, new StringToLongConverter());
    add(String.class, Integer.class, new StringToIntegerConverter());
    add(String.class, BigDecimal.class, new StringToBigDecimalConverter());
    add(BigDecimal.class, Float.class, new BigDecimalToFloatConverter());
    add(BigDecimal.class, Double.class, new BigDecimalToDoubleConverter());
    add(BigInteger.class, Long.class, new BigIntegerToLongConverter());
    add(BigInteger.class, Integer.class, new BigIntegerToIntegerConverter());
    addReverse(Boolean.class, String.class, new StringToBooleanConverter());
    add(Boolean.class, Long.class, new BooleanToLongConverter());
    add(Boolean.class, String.class, new BooleanToYNConverter()); // YesNo
    add(Timestamp.class, Date.class, new TimestampToDateConverter());
    add(Long.class, Boolean.class, new ReverseConverter<Long, Boolean>(new BooleanToLongConverter()));

//    // List<String> -> List<Type>
//    converters.put(new ConverterKey("List<String>", "List"),
//        new ListConverter<String, String>(new IdenticalConverter<String>()));
//    converters.put(new ConverterKey("List<String>", "List<Boolean>"),
//        new ListConverter<String, Boolean>(new StringToBooleanConverter()));
//    converters.put(new ConverterKey("List<String>", "List<Long>"),
//        new ListConverter<String, Long>(new StringToLongConverter()));
//    converters.put(new ConverterKey("List<String>", "List<Integer>"),
//        new ListConverter<String, Integer>(new StringToIntegerConverter()));
//    converters.put(new ConverterKey("List<String>", "List<BigDecimal>"),
//        new ListConverter<String, BigDecimal>(new StringToBigDecimalConverter()));

  }

  private <C, D> void add(Class<C> source, Class<D> dest, Converter<C, D> converter) {
    this.converters.put(new ConverterKey<C, D>(source, dest), converter);
  }

  private <C, D> void addReverse(Class<C> source, Class<D> dest, Converter<D, C> converter) {
    this.converters.put(new ConverterKey<C, D>(source, dest), new ReverseConverter<C, D>(converter));
  }

  private <C, D> void addList(Class<C> source, Class<D> dest, Converter<D, C> converter) {
    this.converters.put(new ConverterKey<C, D>(source, dest), new ReverseConverter<C, D>(converter));
  }

  /**
   * This method finds a {@link BaseConverter}corresponding to the two types
   * given.
   * 
   * @param fromType from type.
   * @param toType to type.
   * @return {@link BaseConverter}corresponding to the types given.
   * @throws ConverterNotFoundException if {@link BaseConverter}is not found
   */
  @SuppressWarnings("unchecked")
  public <C,D> Converter<C,D> findConverter(Class<C> fromType, Class<D> toType) throws ConverterNotFoundException {
    if (fromType == null || toType == null) {
      throw new ConverterNotFoundException(fromType, toType);

    } else if (fromType.equals(toType)) {
      return new IdenticalConverter();

    } else if (Object.class == fromType || Object.class == toType) {
      return new IdenticalConverter();

    } else {
      Converter result = this.converters.get(new ConverterKey(fromType, toType));
      if (result == null) {
        throw new ConverterNotFoundException(fromType, toType);
      }
      return result.newConverter();
    }
  }

  /**
   * Returns an instance of a <code>ConverterFactory</code>. This method is here
   * to simplify the configuration of the <code>ConverterFactory</code> in
   * future.
   * 
   * @return an instance of a <code>ConverterFactory</code>.
   */
  public static ConverterProvider getInstance(ConfigurationContext configuration) {
    ConverterProvider confConverterProvider = (ConverterProvider) configuration
        .getEntry(ConfigurationContext.CUSTOM_CONVERTER_PROVIDER);
    return confConverterProvider == null ? DEFAULT_CONVERTER_FACTORY : confConverterProvider;
  }
}
