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
 */

package org.araneaframework.uilib.form.converter;

import java.io.Serializable;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.Converter;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.support.DataType;

/**
 * Provides access to registered {@link Converter}s.
 * <p>
 * Since 1.1 this interface extends <code>Serializable</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * @see Converter
 * @see ConverterFactory
 * @see FormElement#convert()
 */
public interface ConverterProvider extends Serializable {

  /**
   * This method should return a {@link BaseConverter }corresponding to the two types given.
   * 
   * @param fromType from type.
   * @param toType to type.
   * @return {@link BaseConverter} corresponding to the types given.
   * 
   * @throws ConverterNotFoundException if {@link BaseConverter}is not found  
   */
  @SuppressWarnings("unchecked")
  public Converter findConverter(DataType fromType, DataType toType) throws ConverterNotFoundException;
}
