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

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.Environment;
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
  protected final Map converters = new HashMap();

  protected ConverterFactory() {
    //String -> Type
    converters.put(new ConverterKey("String", "Boolean"), new StringToBooleanConverter());
    converters.put(new ConverterKey("String", "Long"), new StringToLongConverter());
    converters.put(new ConverterKey("String", "Integer"), new StringToIntegerConverter());
    converters.put(new ConverterKey("String", "BigDecimal"), new StringToBigDecimalConverter());

    converters.put(new ConverterKey("BigDecimal", "Float"), new BigDecimalToFloatConverter());
    converters.put(new ConverterKey("BigDecimal", "Double"), new BigDecimalToDoubleConverter());

    converters.put(new ConverterKey("BigInteger", "Long"), new BigIntegerToLongConverter());
    converters.put(new ConverterKey("BigInteger", "Integer"), new BigIntegerToIntegerConverter());

    //List<String> -> List<Type>
    converters
        .put(new ConverterKey("List<String>", "List<Boolean>"), new ListConverter(new StringToBooleanConverter()));
    converters.put(new ConverterKey("List<String>", "List<Long>"), new ListConverter(new StringToLongConverter()));
    converters
        .put(new ConverterKey("List<String>", "List<Integer>"), new ListConverter(new StringToIntegerConverter()));
    converters.put(new ConverterKey("List<String>", "List<BigDecimal>"), new ListConverter(
        new StringToBigDecimalConverter()));

    //Boolean -> Type
    converters.put(new ConverterKey("Boolean", "String"), new ReverseConverter(new StringToBooleanConverter()));
    converters.put(new ConverterKey("Boolean", "Long"), new BooleanToLongConverter());

    converters.put(new ConverterKey("Boolean", "YN"), new BooleanToYNConverter());

    //Date -> Type
    converters.put(new ConverterKey("Timestamp", "Date"), new TimestampToDateConverter());

    //Long -> Type
    converters.put(new ConverterKey("Long", "Boolean"), new ReverseConverter(new BooleanToLongConverter()));    
  }

  /**
   * {@inheritDoc}
	 */
  public Converter findConverter(String fromType, String toType, Environment env) throws ConverterNotFoundException {
    if (fromType == null || toType == null) {
			throw new ConverterNotFoundException(fromType, toType);
		} else if (fromType.equals(toType)) {
			return new IdenticalConverter();
		} else if ("Object".equals(fromType) || "Object".equals(toType)) {
			return new IdenticalConverter();
		} else {
			Converter result = ((Converter) converters.get(new ConverterKey(fromType, toType)));

			if (result == null) {
				throw new ConverterNotFoundException(fromType, toType);
			}

			return result.newConverter();
		}
  }

  /**
	 * Returns an instance of a <code>ConverterFactory</code>. This method is
	 * here to simplify the configuration of the <code>ConverterFactory</code>
	 * in future.
	 * 
	 * @return an instance of a <code>ConverterFactory</code>.
	 */
  public static ConverterProvider getInstance(ConfigurationContext configuration) {
    ConverterProvider confConverterProvider = (ConverterProvider) configuration.getEntry(ConfigurationContext.CUSTOM_CONVERTER_PROVIDER);
    if (confConverterProvider == null) {
      confConverterProvider = DEFAULT_CONVERTER_FACTORY;
    }

    return confConverterProvider;
  }

}
