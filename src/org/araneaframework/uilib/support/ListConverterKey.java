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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A converter key that extends the base <tt>ConverterKey</tt> functionality to represent a list converter.
 * 
 * @param <C> Indicates source object type.
 * @param <D> Indicates target object type.
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 * @see ConverterKey
 * @see org.araneaframework.uilib.form.converter.ConverterFactory
 */
@SuppressWarnings("rawtypes")
public class ListConverterKey<C, D> extends ConverterKey<List, List> {

  private final Class<C> itemFromType;

  private final Class<D> itemToType;

  /**
   * Constructs a new list converter where the source list items are of type <code>itemFromType</code> and the target
   * list items are of type <code>itemToType</code>.
   * 
   * @param itemFromType The type of source list items.
   * @param itemToType The type of destination list items.
   */
  public ListConverterKey(Class<C> itemFromType, Class<D> itemToType) {
    super(List.class, List.class);
    this.itemFromType = itemFromType;
    this.itemToType = itemToType;
  }

  /**
   * Provides the source list item type.
   * 
   * @return The source list item type.
   */
  public final Class<C> getItemFromType() {
    return this.itemFromType;
  }

  /**
   * Provides the destination list item type.
   * 
   * @return The destination list item type.
   */
  public final Class<D> getItemToType() {
    return this.itemToType;
  }

  @Override
  public final boolean equals(Object o) {
    boolean equal = super.equals(o);
    if (equal && o instanceof ListConverterKey) {
      ListConverterKey other = (ListConverterKey) o;
      return new EqualsBuilder().append(this.itemFromType, other.itemFromType)
          .append(this.itemToType, other.itemToType).isEquals();
    }
    return equal;
  }

  /**
   * Implements the {@link Object#hashCode()} method, using types from both source and destination lists.
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(super.hashCode(), 37).append(this.itemFromType).append(this.itemToType).hashCode();
  }
}
