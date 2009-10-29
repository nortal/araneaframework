/*
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
 */

package org.araneaframework.uilib.list.structure;

import java.io.Serializable;


/**
 * Base implementation of the {@link ListField}.
 */
public class ListField implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * ListColumn Id.
	 */
	protected String id;

	/**
	 * ListColumn label.
	 */
	protected String label;

	/**
	 * Constructs a new <code>ListColumn</code> with <code>Id</code> and
	 * <code>Label</code>.
	 * 
	 * @param id
	 *            ListColumn id.
	 * @param label
	 *            ListColumn label.
	 */
	public ListField(String id, String label) {
		this.id = id;
		this.label = label;
	}

	/**
	 * Constructs a new <code>ListColumn</code> without <code>Label</code>.
	 * 
	 * @param id
	 *            ListColumn id.
	 */
	public ListField(String id) {
		this(id, null);
	}

	/**
	 * Returns the Id of this <code>ListColumn</code>.
	 * 
	 * @return the Id of this <code>ListColumn</code>.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Returns the label of this <code>ListColumn</code>.
	 * 
	 * @return the label of this <code>ListColumn</code>.
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * Saves the new label of this <code>ListColumn</code>.
	 * 
	 * @param label
	 *            the new label of this <code>ListColumn</code>.
	 */
	public void setLabel(String label) {
		this.label = label;
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
			this.label = ListField.this.label;
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
