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

import org.apache.commons.lang.ClassUtils;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * This class defines the <code>Map</code> key, that is used to find a converter between data held in
 * {@link org.araneaframework.uilib.form.Control} and corresponding {@link org.araneaframework.uilib.form.Data}.
 * 
 * @see org.araneaframework.uilib.form.converter.ConverterFactory
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
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

  public ConverterKey<D, C> reverse() {
    return new ConverterKey<D, C>(toType, fromType);
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
