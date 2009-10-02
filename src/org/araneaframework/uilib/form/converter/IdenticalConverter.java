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

import org.araneaframework.uilib.form.Converter;

/**
 * Does not change the data.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class IdenticalConverter<T> extends BaseConverter<T, T> {

  /**
   * Does not change the data.
   */
  @Override
  public T convertNotNull(T data) {
    return data;
  }

  /**
   * Does not change the data.
   */
  @Override
  public T reverseConvertNotNull(T data) {
    return data;
  }

  /**
   * Returns a <code>new IdenticalConverter()</code>.
   */
  @Override
  public Converter<T, T> newConverter() {
    return new IdenticalConverter<T>();
  }
}
