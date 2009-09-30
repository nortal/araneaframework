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

package org.araneaframework.uilib.form.formlist.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.araneaframework.uilib.form.formlist.FormListModel;

public class MapFormListModel<T> implements FormListModel<T> {

  private Map<?, T> map;

  public MapFormListModel(Map<?, T> map) {
    this.map = map;
  }

  public List<T> getRows() {
    return new LinkedList<T>(this.map.values());
  }
}
