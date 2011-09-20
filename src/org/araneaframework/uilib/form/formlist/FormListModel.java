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
import java.util.List;

/**
 * The interface that defines methods for callbacks that dynamically provide the data a the form list.
 * 
 * @param <R> The type of list row values.
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see BaseFormListWidget
 */
public interface FormListModel<R> extends Serializable {

  /**
   * Provides the rows to the form list.
   * 
   * @return A list of row data objects.
   * @see BaseFormListWidget
   */
  List<R> getRows();
}
