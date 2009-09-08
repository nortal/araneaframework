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

package org.araneaframework.uilib.list;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the ordering information supplied by user in a series
 * of UI interactions.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class OrderInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  protected List<OrderInfoField> fields = new ArrayList<OrderInfoField>();

  /**
   * Returns the ordering fields.
   * 
   * @return the ordering fields.
   */
  public List<OrderInfoField> getFields() {
    return this.fields;
  }

  /**
   * Clears ordering fields.
   */
  public void clearFields() {
    this.fields.clear();
  }

  /**
   * Adds an ordering field.
   * 
   * @param field an ordering field.
   */
  public void addField(OrderInfoField field) {
    this.fields.add(field);
  }

  /**
   * Returns view model.
   * 
   * @return view model.
   */
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * View model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   */
  public class ViewModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<OrderInfoField.ViewModel> fields = new ArrayList<OrderInfoField.ViewModel>();

    /**
     * Contains values <String,Boolean> when the list field ID of type String
     * has been ordered ascending (true) or descending (false).
     * 
     * @since 1.2.2
     */
    private Map<String, Boolean> fieldsMap = new HashMap<String, Boolean>();

    /**
     * Takes a snapshot of outer class state.
     */
    public ViewModel() {
      for (OrderInfoField field : OrderInfo.this.fields) {
        this.fields.add(field.getViewModel());
        this.fieldsMap.put(field.getId(), new Boolean(field.isAscending()));
      }
    }

    /**
     * Returns the ordering fields.
     * 
     * @return the ordering fields.
     */
    public List<OrderInfoField.ViewModel> getFields() {
      return this.fields;
    }

    /**
     * @since 1.2.2
     * @return
     */
    public Map<String, Boolean> getFieldsMap() {
      return this.fieldsMap;
    }

  }

  public String toString() {
    StringBuffer sb = new StringBuffer("OrderInfo (");
    for (Iterator i = this.fields.iterator(); i.hasNext();) {
      OrderInfoField field = (OrderInfoField) i.next();
      sb.append(field.toString());
      if (i.hasNext()) {
        sb.append("; ");
      }
    }
    sb.append(")");
    return sb.toString();
  }
}
