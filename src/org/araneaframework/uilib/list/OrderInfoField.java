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

/**
 * This class represents information about the ordering of one list column
 * supplied by user during UI interaction.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class OrderInfoField implements Serializable {

	protected String id;

	protected boolean ascending;

	/**
	 * Creates the class initializing its parameters.
	 * 
	 * @param id
	 *            order field id.
	 * @param ascending
	 *            whether ordering is ascending.
	 */
	public OrderInfoField(String id, boolean ascending) {
		this.id = id;
		this.ascending = ascending;
	}

	/**
	 * Returns whether ordering is ascending.
	 * 
	 * @return whether ordering is ascending.
	 */
	public boolean isAscending() {
		return this.ascending;
	}

	/**
	 * Sets whether ordering is ascending.
	 * 
	 * @param ascending
	 *            whether ordering is ascending.
	 */
	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return this.id;
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
	public class ViewModel {
		private String id;

		private boolean ascending;

		/**
		 * Takes a snapshot of outer class state.
		 */
		public ViewModel() {
			this.id = OrderInfoField.this.id;
			this.ascending = OrderInfoField.this.ascending;
		}

		/**
		 * Returns whether ordering is ascending.
		 * 
		 * @return whether ordering is ascending.
		 */
		public boolean isAscending() {
			return this.ascending;
		}

		/**
		 * @return Returns the id.
		 */
		public String getId() {
			return this.id;
		}
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("OrderInfoField (");
		sb.append("Id: ");
		sb.append(getId());
		sb.append("; ");
		sb.append("Ascending: ");
		sb.append(isAscending());
		sb.append(")");
		return sb.toString();	
	}

}
