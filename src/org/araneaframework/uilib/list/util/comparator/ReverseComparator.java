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
 * Comparator that reverses another <code>Comparator</code> by switching the
 * order of two comparable objects.
 */
public class ReverseComparator implements Comparator, Serializable {

	protected Comparator comparator;

	public ReverseComparator(Comparator comparator) {
		if (comparator == null) {
			throw new RuntimeException("Comparator must be provided");
		}
		this.comparator = comparator;
	}

	public int compare(Object o1, Object o2) {
		return this.comparator.compare(o2, o1);
	}

}
