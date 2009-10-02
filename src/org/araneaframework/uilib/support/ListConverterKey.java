
package org.araneaframework.uilib.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

@SuppressWarnings("unchecked")
public class ListConverterKey<C, D> extends ConverterKey<List, List> {

  private Class<C> itemFromType;

  private Class<D> itemToType;

  public ListConverterKey(Class<C> itemFromType, Class<D> itemToType) {
    super(List.class, List.class);
    this.itemFromType = itemFromType;
    this.itemToType = itemToType;
  }

  public Class<C> getItemFromType() {
    return this.itemFromType;
  }

  public Class<D> getItemToType() {
    return this.itemToType;
  }

  @Override
  public boolean equals(Object o) {
    boolean equal = super.equals(o);
    if (equal && o instanceof ListConverterKey) {
      ListConverterKey other = (ListConverterKey) o;
      return new EqualsBuilder().append(this.itemFromType, other.itemFromType)
          .append(this.itemToType, other.itemToType).isEquals();
    }
    return equal;
  }

  /**
   * Implements the {@link Object#hashCode()} method, using both types.
   */
  @Override
  public int hashCode() {
    return new HashCodeBuilder(super.hashCode(), 37).append(this.itemFromType).append(this.itemToType).hashCode();
  }
}
