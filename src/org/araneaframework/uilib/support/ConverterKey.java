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
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This class defines the <code>Map</code> key, that is used to find a converter between data held in
 * {@link org.araneaframework.uilib.form.Control} and corresponding {@link org.araneaframework.uilib.form.Data}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see org.araneaframework.uilib.form.converter.ConverterFactory
 */
public class ConverterKey <C, D> implements Serializable {

  private Class<C> fromType;

  private Class<D> toType;

  /**
   * Creates the class, initializing both "to" and "from" types.
   * 
   * @param fromType the type from which the conversion goes.
   * @param toType the type to which the conversion goes.
   */
  @SuppressWarnings("unchecked")
  public ConverterKey(Class<C> fromType, Class<D> toType) {
    this.fromType = ClassUtils.primitiveToWrapper(fromType);
    this.toType = ClassUtils.primitiveToWrapper(toType);
  }

  /**
   * Returns the type from which the conversion goes.
   * 
   * @return the type from which the conversion goes.
   */
  public Class<C> getFromType() {
    return this.fromType;
  }

  /**
   * Returns the type to which the conversion goes.
   * 
   * @return the type to which the conversion goes.
   */
  public Class<D> getToType() {
    return this.toType;
  }

  /**
   * Returns the converter key that has the source and destination types switched compared to this instance.
   * 
   * @return The new converter key with source and destination type switched.
   */
  @SuppressWarnings("unchecked")
  public ConverterKey<D, C> reverse() {
    return (ConverterKey<D, C>) (isIdentityConversion() ? this : new ConverterKey<D, C>(this.toType, this.fromType));
  }

  /**
   * Returns whether the source and destination types are equal.
   * 
   * @return A <code>Boolean</code> that is <code>true</code> when the source and destination types are equal.
   * @since 2.0
   */
  public boolean isIdentityConversion() {
    return this.fromType.equals(this.toType);
  }

  /**
   * Returns whether either source or target type is <code>Object</code>.
   * 
   * @return A <code>Boolean</code> that is <code>true</code> when either source or target type is <code>Object</code>.
   * @since 2.0
   */
  public boolean isAnyObjectType() {
    return this.fromType == Object.class || this.toType == Object.class;
  }

  /**
   * Returns whether either source or target type is <code>String</code>.
   * 
   * @return A <code>Boolean</code> that is <code>true</code> when either source or target type is <code>String</code>.
   * @since 2.0
   */
  public boolean isAnyStringType() {
    return this.fromType == String.class || this.toType == String.class;
  }

  /**
   * Implements the {@link Object#equals(java.lang.Object)} method, using both types.
   */
  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (o instanceof ConverterKey) {
      ConverterKey other = (ConverterKey) o;
      return new EqualsBuilder().append(this.fromType, other.fromType).append(this.toType, other.toType).isEquals();
    }
    return false;
  }

  /**
   * Implements the {@link Object#hashCode()} method, using both types.
   */
  @Override
  public int hashCode() {
    return 5 * this.fromType.hashCode() + 7 * this.toType.hashCode();
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(this.fromType == null ? "null" : this.fromType.getSimpleName());
    sb.append("->");
    sb.append(this.toType == null ? "null" : this.toType.getSimpleName());
    return sb.toString();
  }
}
