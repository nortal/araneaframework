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

package org.araneaframework.uilib.support;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.backend.util.BeanUtil;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.SelectControl;

/**
 * Represents one item in the combo-box of the select element that is associated with an object. The object is used to
 * resolve the label and value of this item. One can also access the associated object later through
 * {@link #getTargetObject()}.
 * <p>
 * {@link SelectControl} and {@link MultiSelectControl} manage a list of them. Each item is characterized by its value
 * and label and target object.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class BeanDisplayItem<T> extends DisplayItem {

  /**
   * The value object behind this item.
   */
  protected T targetObjct;

  /**
   * Item's label property to retrieve a label.
   */
  protected String labelProperty;

  /**
   * Item's value property to retrieve a value.
   */
  protected String valueProperty;

  /**
   * Whether this display item is disabled.
   */
  protected boolean disabled;

  /**
   * Creates a new instance of {@link DisplayItem}.
   * 
   */
  public BeanDisplayItem() {}

  /**
   * Creates a new instance of {@link DisplayItem}.
   * 
   * @param target The target object behind this item (required).
   * @param labelProperty The property of target object that returns the label of this select item (required).
   * @param valueProperty The property of target object that returns the value of this select item (required).
   */
  public BeanDisplayItem(T target, String labelProperty, String valueProperty) {
    this(target, labelProperty, valueProperty, false);
  }

  /**
   * Creates a new instance of {@link DisplayItem}.
   * 
   * @param target The target object behind this item (required).
   * @param labelProperty The property of target object that returns the label of this select item (required).
   * @param valueProperty The property of target object that returns the value of this select item (required).
   * @param disabled Whether this item is disabled.
   */
  public BeanDisplayItem(T target, String labelProperty, String valueProperty, boolean disabled) {
    super(resolveProperty(target, valueProperty), resolveProperty(target, labelProperty), disabled);
    this.targetObjct = target;
  }

  /**
   * Provides the underlying bean for this display item.
   * 
   * @return The underlying bean.
   */
  public T getTargetObject() {
    return this.targetObjct;
  }

  /**
   * Resolves the property value of given bean. The value is coerced into String.
   * 
   * @return value of property <code>label</code>.
   */
  protected static <T> String resolveProperty(T bean, String property) {
    try {
      String label = ObjectUtils.toString(BeanUtil.getPropertyValue(bean, property));
      return StringUtils.isBlank(label) ? null : label;
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
      return null;
    }
  }

  /**
   * <code>BeanDisplayItem</code> basically uses the same <code>equals</code> logic because all display items need only
   * to be compared by the <code>value</code> property. This method is explicitly overridden here to show that we have
   * not forgot it.
   */
  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  /**
   * <code>BeanDisplayItem</code> basically uses the same <code>hashCode</code> logic because all display items need
   * only to be compared by the <code>value</code> property. This method is explicitly overridden here to show that we
   * have not forgot it.
   */
  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
