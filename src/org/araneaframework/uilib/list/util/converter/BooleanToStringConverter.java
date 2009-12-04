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

package org.araneaframework.uilib.list.util.converter;

import org.araneaframework.uilib.list.util.Converter;
import org.springframework.util.Assert;

/**
 * Not-null converter for converting <code>Boolean</code> values into <code>String</code> values and vice-versa.
 */
public class BooleanToStringConverter implements Converter<Boolean, String> {

  public static final Converter<Boolean, String> YN_UPPER_CONVERTER = new BooleanToStringConverter("Y", "N");

  public static final Converter<Boolean, String> YN_LOWER_CONVERTER = new BooleanToStringConverter("y", "n");

  protected String trueValue;

  protected String falseValue;

  public BooleanToStringConverter(String trueValue, String falseValue) {
    if (trueValue == null || falseValue == null) {
      throw new ConversionException("Target values can not be null");
    }
    if (trueValue.equals(falseValue)) {
      throw new ConversionException("Target values can not be the same");
    }
    this.trueValue = trueValue;
    this.falseValue = falseValue;
  }

  public String convert(Boolean data) throws ConversionException {
    Assert.notNull(data, "Data can not be null");
    return data ? this.trueValue : this.falseValue;
  }

  public Boolean reverseConvert(String data) throws ConversionException {
    if (data == null) {
      throw new ConversionException("Data can not be null");
    } else if (this.trueValue.equals(data)) {
      return true;
    } else if (this.falseValue.equals(data)) {
      return false;
    }
    throw new ConversionException("Data " + data + " not supported");
  }

  public Class<Boolean> getSourceType() {
    return Boolean.class;
  }

  public Class<String> getDestinationType() {
    return String.class;
  }
}
