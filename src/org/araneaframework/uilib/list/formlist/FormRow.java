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

package org.araneaframework.uilib.list.formlist;

import java.io.Serializable;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Represents one editable row. 
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class FormRow implements Serializable {
	
  protected Object rowKey;
  protected Object row;
	protected FormWidget rowForm;
	protected String rowFormId;
	protected boolean open;		
	
	public FormRow(Object rowKey, Object row, String rowFormId, FormWidget rowForm, boolean open) {
		this.rowKey = rowKey;
		this.row = row;
		this.rowForm = rowForm;
		this.rowFormId = rowFormId;
		this.open = open;
	}
	
	public void open() {
		this.open = true;
	}
	
	public void close() {
		this.open = false;
	}
	
	/**
	 * Returns editable row form.
	 * @return editable row form.
	 */
	public FormWidget getRowForm() {
		return rowForm;
	}
	
	/**
	 * Returns editable row key.
	 * @return editable row key.
	 */
	public Object getRowKey() {
		return rowKey;
	}
	
	/**
	 * Returns whether the row is open.
	 * @return whether the row is open.
	 */
	public boolean isOpen() {
		return open;
	}
	
	/**
	 * Sets whether the row is open.
	 * @param open whether the row is open.
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	/**
	 * Returns editable row form id.
	 * @return editable row form id.
	 */
	public String getRowFormId() {
		return rowFormId;
	}
	
	/**
	 * Returns assosiated list row object.
	 * @return assosiated list row object.
	 */
	public Object getRow() {
		return this.row;
	}
	
	/**
	 * Sets assosiated list row object.
	 */
	public void setRow(Object row) {
		this.row = row;
	}
	
	/**
	 * Returns editable row view model.
	 * @return editable row view model.
	 */
	public ViewModel getViewModel() {
		return new ViewModel();
	}
	
	public class ViewModel {
		protected FormWidget rowForm;
		protected String rowFormId;
		protected Boolean open;		
		
		public ViewModel() {
			this.rowForm = FormRow.this.rowForm;
			this.rowFormId = FormRow.this.rowFormId; 
			this.open = new Boolean(FormRow.this.open);
		}
		
		/**
		 * Returns editable row form view model.
		 * @return editable row form view model.
		 */
		public FormWidget getRowForm() {
			return this.rowForm;
		}
		
		/**
		 * Sets whether the row is open.
		 * @param open whether the row is open.
		 */		
		public Boolean getOpen() {
			return this.open;
		}
		
		/**
		 * Returns editable row form id.
		 * @return editable row form id.
		 */
		public String getRowFormId() {
			return rowFormId;
		}	
	}	

}	
