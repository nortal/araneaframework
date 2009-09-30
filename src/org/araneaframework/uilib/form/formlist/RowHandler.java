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

package org.araneaframework.uilib.form.formlist;

import java.io.Serializable;

/**
 * Callback handler that is used to query for the key of the row object. The generic parameter K corresponds to the type
 * of the key values, and the generic parameter R corresponds to the type of the row values.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface RowHandler<K, R> extends Serializable {

  /**
   * The underlying implementation should return the key that uniquely identifies the row object among others.
   * 
   * @param row The row object that must be identified.
   * @return The key that uniquely identifies the given row object among others.
   */
  public K getRowKey(R row);
}
