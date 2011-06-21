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
 * Not-null comparator that compares <code>Boolean</code> values.
 */
public class BooleanComparator implements Comparator<Boolean>, Serializable {

  /**
   * An instance of {@link BooleanComparator} where <code>true</code> values are ordered before <code>false</code>
   * values. Using instance variables is the only way to use a {@link BooleanComparator}.
   */
  public static final Comparator<Boolean> TRUE_FIRST = new BooleanComparator(true);

  /**
   * An instance of {@link BooleanComparator} where <code>false</code> values are ordered before <code>true</code>
   * values. Using instance variables is the only way to use a {@link BooleanComparator}.
   */
  public static final Comparator<Boolean> FALSE_FIRST = new BooleanComparator(false);

  private boolean trueFirst;

  private BooleanComparator(boolean trueFirst) {
    this.trueFirst = trueFirst;
  }

  public int compare(Boolean o1, Boolean o2) {
    int trueFirst = this.trueFirst ? -1 : 1;
    return trueFirst * o1.compareTo(o2);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BooleanComparator && ((BooleanComparator) obj).trueFirst == this.trueFirst;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(20070327, 1455).append(this.trueFirst).toHashCode();
  }
}
