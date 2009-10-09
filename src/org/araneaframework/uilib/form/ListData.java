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

package org.araneaframework.uilib.form;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections.ListUtils;
import org.araneaframework.uilib.support.DataType;

/**
 * This class represents <code>List</code>-typed form element data.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @see Data
 */
public class ListData<T> extends Data<List<T>> {

  public ListData(Class<T> clazz) {
    super(new DataType(List.class, clazz));
    this.value = new LinkedList<T>();
  }

  /**
   * This Data object requires special check so that the order of elements in the values lists would not have any effect
   * on whether the lists contain the same data.
   * 
   * @since 1.2
   */
  public boolean isStateChanged() {
    if (this.markedBaseValue == null || this.value == null) {
      return this.markedBaseValue != null || this.value != null;
    } else {
      return !ListUtils.isEqualList(this.markedBaseValue, this.value);
    }
  }
}
