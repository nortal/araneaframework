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

package org.araneaframework.uilib.form.data;

import java.util.ArrayList;
import java.util.List;
import org.araneaframework.uilib.form.Data;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StringListData extends Data {

  private static final long serialVersionUID = 1L;

  public StringListData() {
    super(List.class, "List<String>");
    this.value = new ArrayList();
  }

  /**
   * This Data object requires special check so that the order of elements in
   * the values lists would not have any effect on whether the lists contain the
   * same data.
   * 
   * @since 1.2
   */
  public boolean isStateChanged() {
    if (this.markedBaseValue == null && this.value == null) {
      return false;
    } else if (this.markedBaseValue == null || this.value == null) {
      return true;
    } else {
      List baseValues = (List) this.markedBaseValue;
      List values = (List) this.value;
      return !(baseValues.containsAll(values) && baseValues.size() == values.size());
    }
  }
}
