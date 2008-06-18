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

import org.araneaframework.core.Assert;
import org.araneaframework.uilib.form.formlist.BeanFormListWidget;
import org.araneaframework.uilib.form.formlist.FormRowHandler;
import org.araneaframework.uilib.form.formlist.model.ListWidgetFormListModel;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class EditableBeanListWidget<T> extends BeanListWidget<T> {

	protected BeanFormListWidget<T> formList;

	//*********************************************************************
	//* CONSTRUCTORS  
	//*********************************************************************	

	public EditableBeanListWidget(FormRowHandler rowHandler, Class<T> beanClass) {
		super(beanClass);
    
    formList = new BeanFormListWidget<T>(rowHandler, new ListWidgetFormListModel(this), beanClass);
	}

	//*********************************************************************
	//* PUBLIC METHODS
	//*********************************************************************		
  
	/**
	 * Returns the editable row manager.
	 * @return the editable row manager.
	 */
	public BeanFormListWidget<T> getFormList() {
		return formList;
	}

	public void setFormRowHandler(FormRowHandler rowHandler) {
		formList.setFormRowHandler(rowHandler);	
	}

	//*********************************************************************
	//* WIDGET METHODS
	//*********************************************************************  	

	@Override
  protected void init() throws Exception {
		super.init();

    Assert.notNull(formList, "You must provide a form row handler to the editable list!");
    
		addWidget("formList", formList);
	}
}
