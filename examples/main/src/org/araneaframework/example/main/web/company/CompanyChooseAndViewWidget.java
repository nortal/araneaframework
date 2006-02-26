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

package org.araneaframework.example.main.web.company;

import org.apache.log4j.Logger;
import org.araneaframework.example.main.BaseWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.template.framework.context.PassthroughCallContextHandler;

public class CompanyChooseAndViewWidget extends BaseWidget {
	
	private static final Logger log = Logger.getLogger(CompanyChooseAndViewWidget.class);
	
  protected void init() throws Exception {
    super.init();
    log.debug("TemplateCompanyChooseAndViewWidget init called");
    
    getFlowCtx().start(new CompanyListWidget(false), null, new FlowContext.Handler() {
			public void onFinish(Object returnValue) throws Exception {
				Long id = (Long) returnValue;
		    getFlowCtx().start(new CompanyViewWidget(id), null, new PassthroughCallContextHandler(getFlowCtx()));
      }
      public void onCancel() throws Exception {
		    getFlowCtx().cancel();
      }
    });
  }
}
