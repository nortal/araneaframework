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

import java.util.List;

import java.util.LinkedList;

import java.util.Collection;

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
   * A Boolean indicating whether this item is rendered as options group. If <code>true</code> then it is expected that
   * the {@link #childOptions} property contains the child-options. Default is <code>false</code>.
   */
  protected boolean group;

  /**
   * A collection of child options that are rendered when this select item has {@link #group} property is set to
   * <code>true</code>.
   */
  protected List<DisplayItem> childOptions;

  /**
   * Whether this display item is disabled.
   */
  protected boolean disabled;

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
   * @param disabled Whether this item is disabled
   */
  public DisplayItem(String value, String label, boolean disabled) {
    if (StringUtils.isBlank(value)) {
      value = null;
    }
    this.label = label;
    this.value = value;
  }

  /**
   * Creates a new instance of {@link DisplayItem}.
   * 
   * @param value The value of the item.
   * @param label The label (or other string to show) of the item.
   * @param disabled Whether this item is disabled
   */
  public DisplayItem(String value, String label, boolean group, Collection<DisplayItem> childOptions, boolean disabled) {
    this(value, label, disabled);
    setGroupAndOptions(group, childOptions);
  }

  /**
   * If this item is a group (<code>group == true</code>) then the group may have child options. A convenience method to
   * specify those properties.
   * 
   * @param group A Boolean indicating whether this item represents a group of select items (this item cannot be
   *          selected).
   * @param childOptions The <code>DisplayItem</code>s that the group contains (if <code>group == false</code> then
   *          these child options won't be stored).
   * @since 2.0
   */
  protected void setGroupAndOptions(boolean group, Collection<DisplayItem> childOptions) {
    this.group = group;
    this.childOptions = this.group && childOptions != null ? new LinkedList<DisplayItem>(childOptions) : null;
  }

  /**
   * Provides the value of this item.
   * 
   * @return The value of this item.
   */
  public String getLabel() {
    return this.label;
  }

  /**
   * Provides the label of this item.
   * 
   * @return The label of this item.
   */
  public String getValue() {
    return this.value;
  }

  /**
   * Provides whether this select item is select items group.
   * 
   * @return A Boolean indicating whether this select item is select items group.
   */
  public boolean isGroup() {
    return this.group;
  }

  /**
   * Provides whether this select item is select items group.
   * 
   * @return A Boolean indicating whether this select item is select items group.
   * @since 2.0
   */
  public List<DisplayItem> getChildOptions() {
    return this.childOptions;
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
    return 227 * this.value.hashCode();
  }

  @Override
  public String toString() {
    return this.label + "=" + this.value;
  }
}
