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

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.control.MultiSelectControl;
import org.araneaframework.uilib.form.control.SelectControl;

/**
 * Represents one item in the combo-box of the select element.
 * <p>
 * {@link SelectControl} and {@link MultiSelectControl} manage a list of them. Each item is characterized by its value
 * and label or by target object. If the latter is defined, it is used for resolving label and value.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DisplayItem implements Serializable {

  /**
   * Item's label.
   */
  protected String label;

  /**
   * Item's value.
   */
  protected String value;

  /**
   * Whether this display item is disabled.
   */
  protected boolean disabled;

  /**
   * Creates a new instance of {@link DisplayItem}.
   * 
   * @since 2.0
   */
  public DisplayItem() {}

  /**
   * Creates a new instance of {@link DisplayItem}.
   * 
   * @param value The value of the item.
   * @param label The label (or other string to show) of the item.
   */
  public DisplayItem(String value, String label) {
    this(value, label, false);
  }

  /**
   * Creates a new instance of {@link DisplayItem}.
   * 
   * @param value The value of the item.
   * @param label The label (or other string to show) of the item.
   * @param disabled Whether this item is disabled.
   */
  public DisplayItem(String value, String label, boolean disabled) {
    if (StringUtils.isBlank(value)) {
      value = null;
    }
    this.label = label;
    this.value = value;
    this.disabled = disabled;
  }

  /**
   * Provides the label of this item.
   * 
   * @return The label of this item.
   * @since 2.0
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Sets the label of this item.
   * 
   * @param label The label for this item.
   * @since 2.0
   */
  public void setLabel(String label) {
    this.label = label;
  }

  /**
   * Provides the value of this item.
   * 
   * @return The value of this item.
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Sets the value of this item.
   * 
   * @param value The value for this item.
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Provides a Boolean indicating whether this item is disabled and cannot be selected.
   * 
   * @return A Boolean indicating whether this item is disabled and cannot be selected.
   * @since 2.0
   */
  public boolean isDisabled() {
    return this.disabled;
  }

  /**
   * Allows to modify whether this item is disabled or not.
   * 
   * @param disabled A Boolean indicating whether this item is disabled and cannot be selected.
   */
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  /**
   * @since 1.2
   */
  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof DisplayItem) {
      DisplayItem other = (DisplayItem) obj;
      return StringUtils.equals(other.value, this.value);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return 227 * (this.value == null ? 1 : this.value.hashCode());
  }

  @Override
  public String toString() {
    return this.label + "=" + this.value;
  }
}
