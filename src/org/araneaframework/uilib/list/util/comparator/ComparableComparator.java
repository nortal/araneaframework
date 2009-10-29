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

/**
 * Not-null comparator that compares <code>Comparable</code> objects by their own (@see
 * java.lang.Comparable#compareTo(java.lang.Object)) method.
 */
public class ComparableComparator<T extends Comparable<T>> implements Comparator<T>, Serializable {

  @SuppressWarnings("unchecked")
  public static final ComparableComparator INSTANCE = new ComparableComparator();

  private ComparableComparator() {}

  public int compare(T o1, T o2) {
    return o1.compareTo(o2);
  }

  @Override
  public boolean equals(Object obj) {
    return ComparableComparator.class.equals(obj.getClass());
  }

  @Override
  public int hashCode() {
    return 703271500;
  }
}
