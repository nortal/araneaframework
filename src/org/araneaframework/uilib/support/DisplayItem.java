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

package org.araneaframework.uilib.support;

import org.araneaframework.uilib.form.control.SelectControl;



/**
 * Represents one item in the combo-box of the select element.
 * <p>
 * {@link SelectControl} manages a list of them.
 * Each item is characterized by its value and label.
 *
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class DisplayItem implements java.io.Serializable {
  /**
   * Item's label.
   */
  protected String displayString;
  /**
   * Item's value.
   */
  protected String value;
  
  protected boolean disabled;

  /**
   * Creates a new instance of <code>SelectElementItem</code>.
   *
   * @param displayString the label (or other string to show) of the item.
   * @param value the value of the item.
   */
  public DisplayItem(String value, String displayString) {
  	this(value, displayString, false);
  }
  
  /**
   * Creates a new instance of <code>SelectElementItem</code>.
   *
   * @param displayString the label (or other string to show) of the item.
   * @param value the value of the item.
   * @param disabled whether item is disabled
   */
  public DisplayItem(String value, String displayString, boolean disabled) {
  	if ("".equals(value))
  		throw new RuntimeException("Empty strings are not allowed as values");
  	
    this.displayString = displayString;
    this.value = value;
    this.disabled = disabled;
  }

  /** 
   * Getter for property <code>label</code>.
   *
   * @return value of property <code>label</code>.
   */
  public java.lang.String getDisplayString() {
    return displayString;
  }

  /** 
   * Getter for property <code>value</code>.
   *
   * @return value of property <code>value</code>.
   */
  public java.lang.String getValue() {
    return value;
  }
  
	public boolean isDisabled() {
		return disabled;
	}
	
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
