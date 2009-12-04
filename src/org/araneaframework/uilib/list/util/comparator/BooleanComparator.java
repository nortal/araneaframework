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

  public static final Comparator<Boolean> TRUE_FIRST = new BooleanComparator(true);

  public static final Comparator<Boolean> FALSE_FIRST = new BooleanComparator(false);

  private boolean trueFirst;

  private BooleanComparator(boolean trueFirst) {
    this.trueFirst = trueFirst;
  }

  public int compare(Boolean o1, Boolean o2) {
    if (o1 == o2) {
      return 0;
    }
    if (this.trueFirst) {
      return o1 ? -1 : 1;
    }
    return o1 ? 1 : -1;
  }

  @Override
  public boolean equals(Object obj) {
    if (BooleanComparator.class == obj.getClass()) {
      return ((BooleanComparator) obj).trueFirst == this.trueFirst;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(20070327, 1455).append(this.trueFirst).toHashCode();
  }
}
