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

package org.araneaframework.uilib.form.formlist;

import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Editable rows widget that is used to handle simultenous editing of multiple forms with same structure.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class BeanFormListWidget<T> extends BaseFormListWidget {
	//*******************************************************************
	// FIELDS
	//*******************************************************************	

	protected Class<T> beanClass;

	//*******************************************************************
	// CONSTRUCTORS
	//*******************************************************************		
  
  /**
   * @param rowHandler row handler.
   * @param beanClass Class of beans in form
   */
  public BeanFormListWidget(FormRowHandler rowHandler, Class<T> beanClass) {
    super(rowHandler);
    this.beanClass = beanClass;
  }  
  
	/**
	 * @param rowHandler row handler.
	 * @param beanClass Class of beans in form
	 */
	public BeanFormListWidget(FormRowHandler rowHandler, FormListModel model, Class<T> beanClass) {
		super(rowHandler, model);
		this.beanClass = beanClass;
	}
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************   
  
  public Class<T> getBeanClass() {
    return this.beanClass;
  }

	//*******************************************************************
	// PROTECTED METHODS
	//*******************************************************************		

	@Override
  protected FormWidget buildAddForm(){
		return new BeanFormWidget<T>(beanClass);
	}
}
