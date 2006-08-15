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

package org.araneaframework.example.main.web.sample;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.AndConstraint;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.constraint.OrConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * @since {since}
 */
public class FormComplexConstraintDemoWidget extends TemplateBaseWidget {
  private static final Logger log = Logger.getLogger(FormComplexConstraintDemoWidget.class);
  private FormWidget searchForm;
  
  protected void init() throws Exception {
    super.init();
	  
	setViewSelector("sample/searchForm");
	
    searchForm = new FormWidget();

    //Adding form controls
    searchForm.addElement("clientFirstName", "#Client first name", new TextControl(), new StringData(), false);
    searchForm.addElement("clientLastName", "#Client last name", new TextControl(), new StringData(), false);

    searchForm.addElement("clientPersonalId", "#Client personal id", new TextControl(), new StringData(), false);
    
    searchForm.addElement("clientAddressTown", "#Town", new TextControl(), new StringData(), false);
    searchForm.addElement("clientAddressStreet", "#Street", new TextControl(), new StringData(), false);
    searchForm.addElement("clientAddressHouse", "#House", new TextControl(), new StringData(), false);       
    
    searchForm.addElement("search", "#Search", new ButtonControl(), null, false);

    //
    //Complex constraint
    //
    
    //First searching scenario
    AndConstraint clientNameConstraint = new AndConstraint();
    clientNameConstraint.addConstraint(new NotEmptyConstraint(searchForm.getElementByFullName("clientFirstName")));
    clientNameConstraint.addConstraint(new NotEmptyConstraint(searchForm.getElementByFullName("clientLastName")));
    
    //Second searching scenario
    NotEmptyConstraint clientPersonalIdConstraint = new NotEmptyConstraint(searchForm.getElementByFullName("clientPersonalId"));
    
    //Third searching scenario
    AndConstraint clientAddressConstraint = new AndConstraint();
    clientAddressConstraint.addConstraint(new NotEmptyConstraint(searchForm.getElementByFullName("clientAddressTown")));
    clientAddressConstraint.addConstraint(new NotEmptyConstraint(searchForm.getElementByFullName("clientAddressStreet")));
    clientAddressConstraint.addConstraint(new NotEmptyConstraint(searchForm.getElementByFullName("clientAddressHouse")));
    
    //Combining scenarios
    OrConstraint searchConstraint = new OrConstraint();  
    searchConstraint.addConstraint(clientPersonalIdConstraint);
    searchConstraint.addConstraint(clientNameConstraint);
    searchConstraint.addConstraint(clientAddressConstraint);
    
    //Setting custom error message
    searchConstraint.setCustomErrorMessage("Not enough data! Please fill in either (client first and last name) or (client personal id) or (client town, street and number)");
    
    //Setting constraint
    searchForm.setConstraint(searchConstraint);
    
    //Putting the widget
    addWidget("searchForm", searchForm);    
  }
  
  public void handleEventSearch() throws Exception {
    if (searchForm.convertAndValidate()) {      
      getMessageCtx().showMessage(MessageContext.INFO_TYPE, "Search allowed!");
    }
  }
  
  public void handleEventReturn(String eventParameter) throws Exception {
	  log.debug("UiEvent 'return' received!");
	  getFlowCtx().cancel();
  }	
}
