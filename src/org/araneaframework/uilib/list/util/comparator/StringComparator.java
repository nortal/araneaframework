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

import java.util.Comparator;
import java.util.Locale;

/**
 * General interface for <code>Comparator</code> that compares
 * <code>String</code> objects.
 */
public interface StringComparator extends Comparator {
	/**
	 * Returns <code>true</code> if this <code>Comparator</code> is case
	 * insensitive.
	 * 
	 * @return <code>true</code> if this <code>Comparator</code> is case
	 *         insensitive.
	 */
	boolean getIgnoreCase();

	/**
	 * Returns the <code>Locale</code> of this <code>Comparator</code>.
	 * 
	 * @return the <code>Locale</code> of this <code>Comparator</code>.
	 */
	Locale getLocale();
}
