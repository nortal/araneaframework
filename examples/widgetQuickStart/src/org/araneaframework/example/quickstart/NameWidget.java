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

package org.araneaframework.example.quickstart;

import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.uilib.core.StandardPresentationWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Jevgeni kabanov (ekabanov@webmedia.ee)
 */
public class NameWidget extends StandardPresentationWidget {

	protected void init() throws Exception {
    setViewSelector("name");
    
    addGlobalEventListener(new ProxyEventListener(this));
    
    FormWidget nameForm = new FormWidget();
    nameForm.addElement("name", "#Name", 
        new TextControl(), new StringData(), true);
    addWidget("nameForm", nameForm);
	}
  
  public void handleEventHello() throws Exception {
    getFlowCtx().replace(new HelloWidget(), null);
  }  
}
