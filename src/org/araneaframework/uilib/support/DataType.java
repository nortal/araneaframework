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
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * The data definition object. Before this implementation a string representation was used instead for defining data
 * types. Since Java 1.5 provided <i>generics</i>, it also became important to represent data types in way that makes
 * good use of this new feature in Java.
 * <p>
 * The data definition basically basically supports defining classes with a single parameter (e.g.
 * <code>String.class</code>, <code>String[].class</code>) or with a collection class to represent the first parameter
 * as a member of the collection class (e.g. <code>(String.class, List.class)</code>). The latter syntax was necessary
 * because <i>generics</i> does not let one define a collection class with collection item type (e.g.
 * <code>List&lt;String&gt;.class</code>).
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
@SuppressWarnings("unchecked")
public class DataType implements Serializable, Cloneable {

  /**
   * Represents the {@link String} type.
   */
  public static final DataType STRING_TYPE = new DataType(String.class);

  private Class<?> type;

  private Class<? extends Collection> collectionType;

  public DataType(Class<?> type) {
    this(null, type);
  }

  public DataType(Class<? extends Collection> collectionType, Class<?> type) {
    this.collectionType = collectionType;
    this.type = type;
  }

  public boolean isList() {
    return this.collectionType != null && this.collectionType == List.class;
  }

  public boolean isSerializable() {
    return this.collectionType == null && this.type.isInstance(Serializable.class);
  }

  public Class<?> getType() {
    return this.type;
  }

  public Class<? extends Collection> getCollectionType() {
    return this.collectionType;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    if (this.collectionType != null) {
      sb.append(this.collectionType.getSimpleName()).append("<").append(this.type.getSimpleName()).append(">");
    } else {
      sb.append(this.type.getSimpleName());
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof DataType) {
      DataType o = (DataType) obj;
      return new EqualsBuilder().append(this.type, o.type).append(this.collectionType, o.collectionType).isEquals();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 963;
    hash += this.type == null ? 5892 : this.type.hashCode();
    hash += this.collectionType == null ? 68 : this.collectionType.hashCode();
    return hash;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
