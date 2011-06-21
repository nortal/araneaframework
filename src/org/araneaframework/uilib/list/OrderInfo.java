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

package org.araneaframework.uilib.list;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.core.Assert;

/**
 * This class represents the ordering information supplied by user in a series of UI interactions.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class OrderInfo implements Serializable {

  protected List<OrderInfoField> fields = new ArrayList<OrderInfoField>();

  /**
   * Default constructor. You must provide order info through getters and setters.
   */
  public OrderInfo() {}

  /**
   * Initiates a new order info using the given initial order info.
   * 
   * @param fieldId The field ID as defined in the list.
   * @param ascending Whether the order of the field identified by <code>fieldId</code> should be ascending or
   *          descending.
   * @since 2.0
   */
  public OrderInfo(String fieldId, boolean ascending) {
    this(new OrderInfoField(fieldId, ascending));
  }

  /**
   * Initiates a new order info using the given initial order info.
   * 
   * @param orderInfoPerField The object that contains information about the field that must be ordered and its order.
   * @since 2.0
   */
  public OrderInfo(OrderInfoField orderInfoPerField) {
    this.fields.add(orderInfoPerField);
  }

  /**
   * Returns a list of data objects storing the order of the associated fields.
   * 
   * @return A list of data objects about fields that are ordered and their order.
   */
  public List<OrderInfoField> getFields() {
    return this.fields;
  }

  /**
   * Removes all information about the list order.
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
   * Adds an ordering field.
   * 
   * @param field The name of the field to order.
   * @param ascending The order information.
   * @since 2.0
   */
  public void addField(String field, boolean ascending) {
    Assert.notEmptyParam(field, "field");
    addField(new OrderInfoField(field, ascending));
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
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel implements Serializable {

    private List<OrderInfoField.ViewModel> fields = new ArrayList<OrderInfoField.ViewModel>();

    /**
     * Contains values <String,Boolean> when the list field ID of type String has been ordered ascending (true) or
     * descending (false).
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
        this.fieldsMap.put(field.getId(), field.isAscending());
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
     * Provides a map of fields that will be sorted. The values of key (field) are booleans that indicate whether the
     * field is sorted ascending or descending.
     * 
     * @return A map of fields where key is field name, and value is sorting order (<code>true == ascending</code>).
     * @since 1.2.2
     */
    public Map<String, Boolean> getFieldsMap() {
      return this.fieldsMap;
    }

  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("OrderInfo (");
    sb.append(StringUtils.join(this.fields, "; "));
    sb.append(")");
    return sb.toString();
  }
}
