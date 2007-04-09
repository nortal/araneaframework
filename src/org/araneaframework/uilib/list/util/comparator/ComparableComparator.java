/**
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
**/

package org.araneaframework.uilib.list.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Not-null comparator that compares <code>Comparable</code> objects by their
 * own (@see java.lang.Comparable#compareTo(java.lang.Object)) method.
 */
public class ComparableComparator implements Comparator, Serializable {
	public static final ComparableComparator INSTANCE = new ComparableComparator();

	private ComparableComparator() {}
	
	public int compare(Object o1, Object o2) {
		return ((Comparable) o1).compareTo(o2);
	}
	
	public boolean equals(Object obj) {
		return ComparableComparator.class.equals(obj.getClass());
	}

	public int hashCode() {
		return 703271500;
	}
}
