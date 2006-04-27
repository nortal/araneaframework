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

import org.apache.log4j.Logger;

/**
 * Not-null comparator that compares <code>Comparable</code> objects by their
 * own (@see java.lang.Comparable#compareTo(java.lang.Object)) method.
 */
public class ComparableComparator implements Comparator, Serializable {
	private static final Logger log = Logger.getLogger(ComparableComparator.class);
	
	public static final ComparableComparator INSTANCE = new ComparableComparator();

	public int compare(Object o1, Object o2) {
		if (log.isDebugEnabled()) {
			log.debug("Comparing objects " + o1 + " (" + o1.getClass().getName() + ") and " + o2 + " (" + o2.getClass().getName() + ")");			
		}
		return ((Comparable) o1).compareTo(o2);
	}
}
