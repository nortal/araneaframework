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

package org.araneaframework.uilib.list.util;

import org.araneaframework.uilib.list.util.converter.ConversionException;

/**
 * Data converter between <code>source</code> and <code>destination</code> types.
 */
public interface Converter<S, D> {

  /**
   * Converts data from source type into destination type.
   * 
   * @param data Source typed data.
   * @return Destination typed data.
   * @throws ConversionException when conversion fails.
   */
  D convert(S data) throws ConversionException;

  /**
   * Converts data from destination type into source type.
   * 
   * @param data Destination typed data.
   * @return Source typed data.
   * @throws ConversionException when convention fails.
   */
  S reverseConvert(D data) throws ConversionException;

  /**
   * Returns the source data type.
   * 
   * @return the source data type.
   */
  Class<S> getSourceType();

  /**
   * Returns the destination data type.
   * 
   * @return the destination data type.
   */
  Class<D> getDestinationType();
}
