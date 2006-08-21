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

package org.araneaframework.uilib.list;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;

/**
 * This class represents the ordering information supplied by user in a series
 * of UI interactions.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class OrderInfo implements Serializable {
	
	/** Ascending order */
	public static final Boolean ASCENDING = Boolean.FALSE;
	/** Descending order */
	public static final Boolean DESCENDING = Boolean.TRUE;
	
	private LinkedHashMap fields = new LinkedHashMap();
	
	/**
	 * Adds an ordering field.
	 * 
	 * @param fieldId
	 *            an ordering field Id.
	 * @param direction
	 *            the ordering direction
	 *            ({@link #ASCENDING} or {@value #DESCENDING}).
	 */
	public void addField(String fieldId, Boolean direction) {
		Validate.notNull(fieldId, "No field Id specified");
		Validate.notNull(direction, "No direction specified");
		Validate.isTrue(!fields.containsKey(fieldId), "Duplicate fields are not allowed");
		
		this.fields.put(fieldId, direction);
	}

	/**
	 * Returns the ordering fields.
	 * 
	 * @return <code>Map&lt;String,Boolean&gt;</code> the ordering fields.
	 */
	public LinkedHashMap getFields() {
		return this.fields;
	}
	
	/**
	 * Clears ordering fields.
	 */
	public void clear() {
		this.fields.clear();
	}
	
	/**
	 * Returns view model.
	 * 
	 * @return view model.
	 */
	public ViewModel getViewModel() {
		return new ViewModel();
	}

	/**
	 * View model.
	 * 
	 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
	 */
	public class ViewModel {
		private LinkedHashMap fields;

		/**
		 * Takes a snapshot of outer class state.
		 */
		public ViewModel() {
			this.fields = OrderInfo.this.fields;
		}

		/**
		 * Returns the ordering fields.
		 * 
		 * @return the ordering fields.
		 */
		public LinkedHashMap getFields() {
			return this.fields;
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("OrderInfo: ");
		for (Iterator i = this.fields.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Entry) i.next();			
			sb.append(entry.getKey().toString());			
			sb.append(ASCENDING.equals(entry.getValue()) ? " ASC" : " DESC");
			if (i.hasNext()) {
				sb.append("; ");				
			}
		}
		return sb.toString();	
	}
}
