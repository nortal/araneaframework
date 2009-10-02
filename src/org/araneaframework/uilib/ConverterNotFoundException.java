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

package org.araneaframework.uilib;

/**
 * This exception is thrown when the {@link org.araneaframework.uilib.form.converter.BaseConverter} 
 * for the current {@link org.araneaframework.uilib.form.control.BaseControl} and {@link org.araneaframework.uilib.form.Data} 
 * could not be found.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class ConverterNotFoundException extends Exception {
  
  /**
   * Creates an exception which means that a {@link org.araneaframework.uilib.form.converter.BaseConverter} between the
   * <code>fromType</code> and <code>toType</code> could not be found.
   * 
   * @param fromType the source type of the {@link org.araneaframework.uilib.form.converter.BaseConverter}.
   * @param toType the target type of the {@link org.araneaframework.uilib.form.converter.BaseConverter}.
   */
  public <C, D> ConverterNotFoundException(Class<C> fromType, Class<D> toType) {
    super("Could not find a Converter from type '" + fromType + "' to type '" + toType + "'.");
  }
}
