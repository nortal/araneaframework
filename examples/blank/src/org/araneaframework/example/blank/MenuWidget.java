/**
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.example.blank;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.OutputData;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.BaseMenuWidget;
import org.araneaframework.uilib.core.MenuItem;

public class MenuWidget extends BaseMenuWidget {
	public MenuWidget() throws Exception {
		super(null);
	}

	protected MenuItem buildMenu() throws Exception {
		MenuItem result = new MenuItem();
		result.addMenuItem(new MenuItem("#Blank1", BlankWidget.class));
		result.addMenuItem(new MenuItem("#Blank2", BlankWidget.class));
		result.addMenuItem(new MenuItem("#Exceptions happen", ExceptionWidget.class));

		return result;
	}

	protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
	    if (ExceptionUtils.getRootCause(e) != null) {
	        putViewDataOnce("rootStackTrace", 
	            ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
	      }        
	      putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e)); 
	      
	      ServletUtil.include("/WEB-INF/jsp/menuError.jsp", getEnvironment(), output);		
	}
}
