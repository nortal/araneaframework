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

import java.io.Serializable;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.DataType;

/**
 * Provides access to registered {@link Converter}s. The {@link DataType}s should provide enough information to make
 * right conversions.
 * 
 * Since 1.1 this interface extends <code>Serializable</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @see Converter
 * @see ConverterFactory
 * @see FormElement#convert()
 */
@SuppressWarnings("unchecked")
public interface ConverterProvider extends Serializable {

  /**
   * This method should return a converter to convert an object of type <code>fromType</code> to an object
   * <code>toType</code>.
   * 
   * @param fromType The type information about the conversion source object.
   * @param toType The type information about the conversion target object.
   * @return {@link BaseConverter} corresponding to the types given.
   * @throws ConverterNotFoundException if {@link BaseConverter}is not found
   */
  public Converter findConverter(DataType fromType, DataType toType) throws ConverterNotFoundException;

}
