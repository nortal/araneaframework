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

package org.araneaframework.uilib.util;

import java.util.Collection;
import java.util.List;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface DisplayItemContainer<T> {

  /**
   * Adds a display-item to the element.
   * 
   * @param item the item to be added.
   */
  public void addItem(T item);

  /**
   * Adds {@link DisplayItem}'s from <code>Collection to this {@link DisplayItemContainer}.
   * 
   * @param items <code>Collection &lt;{@link DisplayItem}&gt;</code>.
   */
  public void addItems(Collection<T> items);

  /**
   * Clears the currently held {@link DisplayItem}s.
   */
  public void clearItems();

  /**
   * Returns a <code>List &lt;{@link DisplayItem}&gt;</code> that this {@link DisplayItemContainer} currently helds.
   * 
   * @return a list of display-items.
   */
  public List<T> getAllItems();

  /**
   * Returns the index of the display item with the specified value.
   * 
   * @param value display item value.
   * @return the index of the display item with the specified value.
   */
  public int getValueIndex(String value);

  public String getItemLabelProperty();

  public String getItemValueProperty();
}
