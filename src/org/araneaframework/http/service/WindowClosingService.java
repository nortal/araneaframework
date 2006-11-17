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

package org.araneaframework.http.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseService;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.filter.StandardPopupFilterWidget.StandardPopupServiceInfo;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.http.util.ServletUtil;

/**
 * Service that returns response that closes browser window that made the 
 * request; and if possible, reloads the opener of that window.
 *
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class WindowClosingService extends BaseService {
	private Environment closableComponentEnv;
	
	public WindowClosingService(Environment closableComponentEnv) {
		this.closableComponentEnv = closableComponentEnv;
	}
	
	protected void action(Path path, InputData input, OutputData output) throws Exception {
		HttpServletResponse response = ServletUtil.getResponse(output);
		
		PopupWindowContext popupCtx = ((PopupWindowContext)closableComponentEnv.getEntry(PopupWindowContext.class));
		BaseApplicationWidget opener = null;
		if (popupCtx != null)
			opener = (BaseApplicationWidget) popupCtx.getOpener();
		
		StandardPopupServiceInfo serviceInfo = null;
		if (opener != null) {
			String threadId = (String) ((ThreadContext) opener.getEnvironment().getEntry(ThreadContext.class)).getCurrentId();
			String topserviceId = (String) ((TopServiceContext) opener.getEnvironment().getEntry(TopServiceContext.class)).getCurrentId();
			String url = ((HttpOutputData)getInputData().getOutputData()).encodeURL(((HttpInputData)getInputData()).getContainerURL());
			serviceInfo = new StandardPopupServiceInfo(topserviceId, threadId, null, url);
		}
		
		String script;
		if (serviceInfo != null) {
			script = 
				"reloadParentWindow('" + serviceInfo.toURL() + "');" +
				"closeWindow(50);";
		} else {
			script = "closeWindow(50);";
		}

		String scriptSrc = FileImportUtil.getImportString("js/aranea/aranea-popups.js", input);
		String responseStr = 
			"<html>" +
			  "<head>" +
			    "<script type=\"text/javascript\" src=\"" + scriptSrc + "\"></script>" +
			  "</head>" +
			  "<body>" + 
			    "<script type=\"text/javascript\">"+ script +"</script>" +
			  "</body>" +
			"</html>";

		byte[] rsp = responseStr.getBytes(); 
		
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		byteOutputStream.write(rsp);
		
		response.setContentType("text/html");
		response.setContentLength(byteOutputStream.size());
		
		OutputStream out = response.getOutputStream();
		byteOutputStream.writeTo(out);
		out.flush();

		ManagedServiceContext mngCtx = (ManagedServiceContext) getEnvironment().getEntry(ManagedServiceContext.class);
		mngCtx.close(mngCtx.getCurrentId());
	}
}
