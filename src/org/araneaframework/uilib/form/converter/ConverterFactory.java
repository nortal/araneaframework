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

package org.araneaframework.uilib.form.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.support.ConverterKey;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.util.ConfigurationContextUtil;

/**
 * This class is a Factory pattern implementation, that provides methods to make
 * {@link org.araneaframework.uilib.form.FormElement}s and {@link org.araneaframework.uilib.form.converter.BaseConverter}s.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org) 
 */
@SuppressWarnings("unchecked")
public class ConverterFactory implements ConverterProvider {

  /** @since 1.1 */
  public static final ConverterProvider DEFAULT_CONVERTER_FACTORY = new ConverterFactory();

  protected final Map<ConverterKey<Class, Class>, Converter> converters = new HashMap<ConverterKey<Class, Class>, Converter>();

  protected ConverterFactory() {
    addConverter(String.class, Boolean.class, new StringToBooleanConverter());
    addConverter(String.class, Long.class, new StringToLongConverter());
    addConverter(String.class, Integer.class, new StringToIntegerConverter());
    addConverter(String.class, BigDecimal.class, new StringToBigDecimalConverter());
    addConverter(Boolean.class, Long.class, new BooleanToLongConverter());
    addConverter(Boolean.class, String.class, new BooleanToYNConverter());
    addConverter(BigDecimal.class, Float.class, new BigDecimalToFloatConverter());
    addConverter(BigDecimal.class, Double.class, new BigDecimalToDoubleConverter());
    addConverter(BigInteger.class, Long.class, new BigIntegerToLongConverter());
    addConverter(BigInteger.class, Integer.class, new BigIntegerToIntegerConverter());
    addConverter(Timestamp.class, Date.class, new TimestampToDateConverter());
  }

  private void addConverter(Class source, Class dest, Converter converter) {
    this.converters.put(new ConverterKey(source, dest), converter);
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
  public Converter findConverter(DataType fromType, DataType toType) throws ConverterNotFoundException {
    if (fromType == null || toType == null) {
      throw new ConverterNotFoundException(fromType, toType);
    }

    ConverterKey key = new ConverterKey(fromType.getType(), toType.getType());
    ConverterKey keyReverse = new ConverterKey(toType.getType(), fromType.getType());

    if (fromType.equals(toType) || fromType.getType().equals(Object.class) || toType.equals(Object.class)) {
      return new IdenticalConverter();

    } else if (this.converters.containsKey(key)) {
      return addWrappers(fromType, toType, this.converters.get(key));

    } else if (this.converters.containsKey(keyReverse)) {
      return addWrappers(fromType, toType, this.converters.get(keyReverse));

    } else {
      throw new ConverterNotFoundException(fromType, toType);
    }
  }

  private Converter addWrappers(DataType fromType, DataType toType, Converter converter) {
    return fromType.isList() && toType.isList() ? new ListConverter(converter) : converter;
  }

  /**
   * Returns an instance of a <code>ConverterFactory</code>. This method is here to simplify the configuration of the
   * <code>ConverterFactory</code> in future.
   * 
   * @return an instance of a <code>ConverterFactory</code>.
   */
  public static ConverterProvider getInstance(ConfigurationContext conf) {
    ConverterProvider provider = ConfigurationContextUtil.getCustomConverterProvider(conf);
    return provider == null ? DEFAULT_CONVERTER_FACTORY : provider;
  }
}
