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
 * Comparator that compares <code>null</code> values and uses another not-null
 * <code>Comparator</code> in other cases.
 */
public class NullComparator implements Comparator, Serializable {

	private static final long serialVersionUID = 1L;

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
		if (o1 == null && o2 == null) {
			return 0;
		}
		if (o1 == null) {
			// o1 == null && o2 != null
			return this.nullFirst ? -1 : 1;
		}
		if (o2 == null) {
			// o1 != null && o2 == null
			return this.nullFirst ? 1 : -1;
		}
		return this.notNullComparator.compare(o1, o2);
	}
}
