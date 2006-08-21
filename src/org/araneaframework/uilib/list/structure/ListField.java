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

package org.araneaframework.uilib.list.structure;

import java.io.Serializable;

import org.apache.commons.lang.Validate;


/**
 * Lists field (column).
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class ListField implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Field Id.
	 */
	protected final String id;

	/**
	 * Field label Id.
	 */
	protected final String labelId;

	/**
	 * Field type.
	 */
	protected final Class type;	

	/**
	 * Creates new {@link ListField}.
	 * 
	 * @param id
	 *            field Id.
	 * @param labelId
	 *            field label Id.
	 * @param type
	 *            field type.
	 */
	public ListField(String id, String labelId, Class type) {
		Validate.notNull(id, "No id specified");		
		this.id = id;
		this.labelId = labelId;
		this.type = type != null ? type : Object.class;
	}

	/**
	 * Returns the Id.
	 * 
	 * @return the Id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Returns the label Id.
	 * 
	 * @return the label Id.
	 */
	public String getLabelId() {
		return this.labelId;
	}

	/**
	 * Returns the field type.
	 * 
	 * @return the field type.
	 */
	public Class getType() {
		return type;
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
	 * View Model.
	 */
	public class ViewModel implements Serializable {

		private static final long serialVersionUID = 1L;

		private String id;
		private String label;

		/**
		 * Takes a snapshot of outer class state.
		 */
		protected ViewModel() {
			this.id = ListField.this.id;
			this.label = ListField.this.labelId;
		}

		/**
		 * @return Returns the id.
		 */
		public String getId() {
			return id;
		}

		/**
		 * @return Returns the label.
		 */
		public String getLabel() {
			return label;
		}
	}
}
