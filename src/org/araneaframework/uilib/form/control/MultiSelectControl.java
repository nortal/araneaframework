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

package org.araneaframework.uilib.form.control;

import java.util.List;
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.util.SelectControlUtil;
import org.springframework.util.Assert;


/**
 * This class represents a multi-select control (a.k.a. multi-choice list).
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class MultiSelectControl<T> extends BaseSelectControl<T, List<T>> {

  /**
   * Creates a new instance of MultiSelectControl with given label and value property names, which are both required.
   * 
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see MultiSelectControl#MultiSelectControl(Class, String, String)
   */
  public MultiSelectControl(String itemLabelProperty, String itemValueProperty) {
    this((Class<T>) null, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines initial items and label and value property names.
   * The label and value property names are required.
   * 
   * @param items Predefined select items. May be <code>null</code>.
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see MultiSelectControl#MultiSelectControl(List, Class, String, String)
   */
  public MultiSelectControl(Class<T> itemClass, String itemLabelProperty, String itemValueProperty) {
    this(null, itemClass, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines item class and label and value property names. Note
   * that usually the class parameter is not needed. It is needed only when the select values are defined one-by-one
   * with {@link #addItem(String, String)} method (then class is used to create new instances). The label and value
   * property names are required.
   * 
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see MultiSelectControl#MultiSelectControl(String, String)
   */
  public MultiSelectControl(List<T> items, String itemLabelProperty, String itemValueProperty) {
    this(items, null, itemLabelProperty, itemValueProperty);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines item class, initial values, label and value property
   * names. Note that usually the class parameter is not needed. It is needed only when the select values are defined
   * one-by-one with {@link #addItem(String, String)} method (then class is used to create new instances). The label and
   * value property names are required.
   * 
   * @param items Predefined select items. May be <code>null</code>.
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The property of select item to retrieve the label of select item (required).
   * @param itemValueProperty The property of select item to retrieve the value of select item (required).
   * @see MultiSelectControl#MultiSelectControl(List, String, String)
   */
  public MultiSelectControl(List<T> items, Class<T> itemClass, String itemLabelProperty, String itemValueProperty) {
    this(items, itemClass, itemLabelProperty, itemValueProperty, null, null);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines initial values, label and value property names. The
   * label and value property names are required.
   * <p>
   * Optional properties for making use of <code>&lt;optgroup&gt;</code>s are <code>itemIsGroupProperty</code> and
   * <code>groupChildrenProperty</code>. The former property, when specified, is checked on every display item, and when
   * the property is <code>true</code>, the display item is considered to represent an <code>&lt;optgroup&gt;</code>
   * (its label property is used for fetching the name of the group; its value property will be ignored). For every
   * <code>&lt;optgroup&gt;</code> item, the <code>groupChildrenProperty</code> will be checked to retrieve items of
   * type <code>T</code> in an array or {@link List} to fetch the <code>&lt;option&gt;</code>s for the group.
   * <p>
   * <b>NB!</b> Specifying <code>itemIsGroupProperty</code> and <code>groupChildrenProperty</code> to a select control
   * can only be done through this constructor!
   * 
   * @param items Predefined select items. May be <code>null</code>.
   * @param itemLabelProperty The {@link String} property of select item to retrieve the label of select item
   *          (required).
   * @param itemValueProperty The {@link String} property of select item to retrieve the value of select item
   *          (required).
   * @param itemIsGroupProperty The {@link Boolean} property of select item to retrieve the condition value that this
   *          select item is a non-selectable &lt;optgroup&gt; containing other child <code>&lt;option&gt;</code>s
   *          (optional, but mandatory when <code>groupChildrenProperty</code> is provided).
   * @param groupChildrenProperty The array/{@link List}&lt;T&gt; property of a group item to retrieve the child
   *          <code>&lt;option&gt;</code>s to render in the <code>&lt;optgroup&gt;</code> (optional, but mandatory when
   *          <code>itemIsGroupProperty</code> is provided).
   * @see MultiSelectControl#MultiSelectControl(List, Class, String, String, String, String)
   * @see MultiSelectControl#addItem(String, String)
   */
  public MultiSelectControl(List<T> items, String itemLabelProperty, String itemValueProperty,
      String itemIsGroupProperty, String groupChildrenProperty) {
    this(items, null, itemLabelProperty, itemValueProperty, itemIsGroupProperty, groupChildrenProperty);
  }

  /**
   * Creates a new instance of MultiSelectControl, and also defines item class and label and value property names. Note that
   * usually the class parameter is not needed. It is needed only when the select values are defined one-by-one with
   * {@link #addItem(String, String)} method (then class is used to create new instances). The label and value property
   * names are required.
   * <p>
   * Optional properties for making use of <code>&lt;optgroup&gt;</code>s are <code>itemIsGroupProperty</code> and
   * <code>groupChildrenProperty</code>. The former property, when specified, is checked on every display item, and when
   * the property is <code>true</code>, the display item is considered to represent an <code>&lt;optgroup&gt;</code>
   * (its label property is used for fetching the name of the group; its value property will be ignored). For every
   * <code>&lt;optgroup&gt;</code> item, the <code>groupChildrenProperty</code> will be checked to retrieve items of
   * type <code>T</code> in an array or {@link List} to fetch the <code>&lt;option&gt;</code>s for the group.
   * <p>
   * <b>NB!</b> Specifying <code>itemIsGroupProperty</code> and <code>groupChildrenProperty</code> to a select control
   * can only be done through this constructor!
   * 
   * @param items Predefined select items. May be <code>null</code>.
   * @param itemClass The class of the items stored in this select (needed when select values are defined one-by-one).
   * @param itemLabelProperty The {@link String} property of select item to retrieve the label of select item
   *          (required).
   * @param itemValueProperty The {@link String} property of select item to retrieve the value of select item
   *          (required).
   * @param itemIsGroupProperty The {@link Boolean} property of select item to retrieve the condition value that this
   *          select item is a non-selectable &lt;optgroup&gt; containing other child <code>&lt;option&gt;</code>s
   *          (optional, but mandatory when <code>groupChildrenProperty</code> is provided).
   * @param groupChildrenProperty The array/{@link List}&lt;T&gt; property of a group item to retrieve the child
   *          <code>&lt;option&gt;</code>s to render in the <code>&lt;optgroup&gt;</code> (optional, but mandatory when
   *          <code>itemIsGroupProperty</code> is provided).
   * @see MultiSelectControl#MultiSelectControl(List, String, String, String, String)
   * @see MultiSelectControl#addItem(String, String)
   */
  public MultiSelectControl(List<T> items, Class<T> itemClass, String itemLabelProperty, String itemValueProperty,
      String itemIsGroupProperty, String groupChildrenProperty) {
    super(items, itemClass, itemLabelProperty, itemValueProperty, itemIsGroupProperty, groupChildrenProperty);
  }

  @Override
  protected List<T> fromRequestParameters(String[] parameterValues) {
    return SelectControlUtil.getSelectItems(this, parameterValues);
  }

  @Override
  protected String[] toResponseParameters(List<T> controlValues) {
    return SelectControlUtil.getItemsValues(controlValues, this.valueProperty);
  }

  public DataType getRawValueType() {
    this.itemClass = SelectControlUtil.resolveClass(this.itemClass, this.items);
    Assert.notNull(this.itemClass != null,
        "Cannot resolve data type because select item class nor select items provided!");
    return new DataType(List.class, this.itemClass);
  }
}
