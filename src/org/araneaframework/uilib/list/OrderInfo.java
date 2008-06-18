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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents the ordering information supplied by user in a series
 * of UI interactions.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class OrderInfo implements Serializable {

	protected List<OrderInfoField> fields = new ArrayList<OrderInfoField>();

	/**
	 * Returns the ordering fields.
	 * 
	 * @return the ordering fields.
	 */
	public List<OrderInfoField> getFields() {
		return this.fields;
	}

	/**
	 * Clears ordering fields.
	 */
	public void clearFields() {
		this.fields.clear();
	}

	/**
	 * Adds an ordering field.
	 * 
	 * @param field
	 *            an ordering field.
	 */
	public void addField(OrderInfoField field) {
		this.fields.add(field);
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
	 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
	 */
	public class ViewModel implements Serializable {
		private List<OrderInfoField.ViewModel> fields = new ArrayList<OrderInfoField.ViewModel>();

		/**
		 * Takes a snapshot of outer class state.
		 */
		public ViewModel() {
			for (OrderInfoField orderInfoField : OrderInfo.this.fields) {
				this.fields.add(orderInfoField.getViewModel());
			}
		}

		/**
		 * Returns the ordering fields.
		 * 
		 * @return the ordering fields.
		 */
		public List<OrderInfoField.ViewModel> getFields() {
			return this.fields;
		}
	}
	
	@Override
  public String toString() {
		StringBuffer sb = new StringBuffer("OrderInfo (");
		for (Iterator<OrderInfoField> i = this.fields.iterator(); i.hasNext();) {
			OrderInfoField field = i.next();
			sb.append(field.toString());
			if (i.hasNext()) {
				sb.append("; ");				
			}
		}
		sb.append(")");
		return sb.toString();	
	}
}
