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

package org.araneaframework.uilib.list.util.comparator;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Comparator that reverses another <code>Comparator</code> by switching the order of two comparable objects.
 */
public class ReverseComparator<T> implements Comparator<T>, Serializable {

  protected Comparator<T> comparator;

  public ReverseComparator(Comparator<T> comparator) {
    if (comparator == null) {
      throw new RuntimeException("Comparator must be provided");
    }
    this.comparator = comparator;
  }

  public int compare(T o1, T o2) {
    return this.comparator.compare(o2, o1);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ReverseComparator)) {
      return false;
    } else if (this == obj) {
      return true;
    }

    ReverseComparator<T> rhs = (ReverseComparator<T>) obj;
    return this.comparator.equals(rhs.comparator);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(20070327, 1239).append(this.comparator).toHashCode();
  }
}
