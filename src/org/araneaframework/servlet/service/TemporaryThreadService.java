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

package org.araneaframework.servlet.service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.servlet.ServletOutputData;

/**
 * When server side thread is closed, client sometimes cannot be made aware
 * of that and may make a request to it. In these cases, temporary replacement
 * service can be started that upon receiving request notifies client with
 * some predetermined method that this thread is already dead and can also 
 * provide client with clues for further action. After temporary thread has 
 * notified client of its morbid state, it performs final suicide.
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class TemporaryThreadService extends BaseService {
	protected void action(Path path, InputData input, OutputData output) throws Exception {
		HttpServletResponse response = ((ServletOutputData) output).getResponse();
		
		String responseStr = "<html><body>ASD</body></html>";
		
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
