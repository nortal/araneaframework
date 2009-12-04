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

import org.araneaframework.core.Assert;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.araneaframework.uilib.form.formlist.FormListModel;
import org.araneaframework.uilib.form.formlist.FormListWidget;

/**
 * A <code>FormListModel</code> implementation that accepts list rows data as {@link Map} (the values of the map are
 * retrieved and returned in {@link #getRows()}). This object can be provided to {@link FormListWidget} to provide the
 * model data to the list as the list rows.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class MapFormListModel<R> implements FormListModel<R> {

  private Map<?, R> map;

  /**
   * Constructs a new <code>FormListModel</code> using the given <code>map</code>. Note that the parameter is required.
   * 
   * @param map The values of this map will be used as the rows of the {@link FormListWidget}.
   */
  public MapFormListModel(Map<?, R> map) {
    Assert.notNullParam(map, "map");
    this.map = map;
  }

  public List<R> getRows() {
    return new LinkedList<R>(this.map.values());
  }
}
