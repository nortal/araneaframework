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

package org.araneaframework.backend.list.helper.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * Composite implementation of {@link Fields}
 * that combine the results of its children.
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public class ConcatFields implements Fields {
	
	private List children = new ArrayList();
	
	/**
	 * Adds an instance of {@link Fields} which results are used.
	 */
	public void add(Fields fields) {
		children.add(fields);
	}

	public Collection getNames() {
		Collection result = new ArrayList();
		for (Iterator it = children.iterator(); it.hasNext();) {
			Fields child = (Fields) it.next(); 
			result.addAll(child.getNames());
		}
		return result;
	}

}
