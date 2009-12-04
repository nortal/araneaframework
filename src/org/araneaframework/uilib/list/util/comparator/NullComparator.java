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
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Comparator that compares <code>null</code> values and uses another not-null <code>Comparator</code> in other cases.
 */
@SuppressWarnings("unchecked")
public class NullComparator implements Comparator<Object>, Serializable {

  private Comparator notNullComparator;

  private boolean nullFirst;

  public NullComparator(Comparator notNullComparator, boolean nullFirst) {
    this.notNullComparator = notNullComparator;
    this.nullFirst = nullFirst;
  }

  public Comparator getNotNullComparator() {
    return this.notNullComparator;
  }

  public int compare(Object o1, Object o2) {
    Comparator<Object> comparator = null;
    if (this.nullFirst) {
      comparator = ComparatorUtils.nullHighComparator(this.notNullComparator);
    } else {
      comparator = ComparatorUtils.nullLowComparator(this.notNullComparator);
    }
    return comparator.compare(o1, o2);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof NullComparator == false) {
      return false;
    } else if (this == obj) {
      return true;
    }

    NullComparator rhs = (NullComparator) obj;
    return this.nullFirst == rhs.nullFirst && this.notNullComparator.equals(rhs.notNullComparator);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(20070327, 1229).append(this.nullFirst).append(this.notNullComparator).toHashCode();
  }
}
