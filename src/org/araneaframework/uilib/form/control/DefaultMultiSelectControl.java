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
import org.araneaframework.core.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.uilib.support.DisplayItem;
import org.araneaframework.uilib.util.DisplayItemUtil;

/**
 * Concrete type ({@link DisplayItem}) implementation for {@link MultiSelectControl}. This means that unlike MultiSelectControl
 * that stores its data in the type specified, this sub implementation stores data only in {@link DisplayItem}s. This
 * implementation is additionally needed as <code>DisplayItem</code>s don't have all getters and setters that are
 * required when adding new items through <code>MultiSelectControl</code>.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public class DefaultMultiSelectControl extends MultiSelectControl<DisplayItem> {

  /**
   * Initializes a new <code>DefaultMultiSelectControl</code>.
   */
  public DefaultMultiSelectControl() {
    this(null);
  }

  /**
   * Initializes a new <code>DefaultMultiSelectControl</code> with given items as its initial data.
   * 
   * @param items The items to add to this new select control. Technically, <code>null</code> is allowed, too, but then
   *          no data will be added to this select control.
   */
  public DefaultMultiSelectControl(List<DisplayItem> items) {
    super(items, DisplayItem.class, "label", "value", "group", "childOptions");
  }

  @Override
  public void addItem(String label, String value) {
    Assert.notNullParam(label, "label");
    try {
      DisplayItem newItem = new DisplayItem(value, label);
      DisplayItemUtil.assertUnique(this, newItem);
      this.items.add(newItem);
    } catch (Exception e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  protected class ViewModel extends MultiSelectControl<DisplayItem>.ViewModel {

    @Override
    public String getControlType() {
      return MultiSelectControl.class.getSimpleName();
    }
  }
}
