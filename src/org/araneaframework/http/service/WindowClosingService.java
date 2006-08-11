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
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.http.ServletInputData;
import org.araneaframework.http.ServletOutputData;
import org.araneaframework.http.util.FileImportUtil;

/**
 * Service that returns response that closes browser window that made the 
 * request; and if possible, reloads the opener of that window.
 *
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class WindowClosingService extends BaseService {
	protected String script = 
		"reloadParentWindow();" +
		"closeWindow(50);";

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		HttpServletResponse response = ((ServletOutputData) output).getResponse();
		
		String scriptSrc = FileImportUtil.getImportString("js/aranea/aranea-popups.js", ((ServletInputData) input).getRequest() ,((ServletOutputData) output).getResponse());
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
