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

package org.araneaframework.http.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Service;
import org.araneaframework.core.BaseComponent;
import org.araneaframework.core.BaseEnvironment;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.ServletServiceAdapterComponent;

/**
 * <p>
 * Creates a StandardServletInputData and StandardServletOutputData from the
 * HttpServletRequest and HttpServletResponse respectively and routes the
 * request to the child services using a null Path.
 * </p>
 * <p>
 * Since <emphasis>1.0.3</emphasis> this adapter makes {@link OutputData} and 
 * {@link InputData} accessible from {@link Component}'s {@link Environment}:
 * 
 * <p>
 * <code>
 *   InputData input = (InputData)getEnvironment().getEntry(InputData.class);<br>
 *   OutputData input = (OutputData)getEnvironment().getEntry(OutputData.class);
 * </code>
 * </p> 
 * 
 * which allows access to request from {@link BaseComponent}'s initialization
 * callback&mdash;<code>init()</code>.
 * </p>
 *
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletServiceAdapterComponent extends BaseComponent
implements ServletServiceAdapterComponent {

	private Service childService;

	private static final ThreadLocal localInput = new ThreadLocal();
	private static final ThreadLocal localOutput = new ThreadLocal();

	protected void init() throws Exception {
		childService._getComponent().init(new BaseEnvironment() {

			public Object getEntry(Object key) {
				if (InputData.class.equals(key))
					return localInput.get();
				if (OutputData.class.equals(key))
					return localOutput.get();
				return getEnvironment().getEntry(key);
			}  
		});
	}

	public void setChildService(Service service) {
		childService = service;
	}

	protected void destroy() throws Exception {
		childService._getComponent().destroy();
	}

	public void service(HttpServletRequest request, HttpServletResponse response) {
		HttpInputData input = new StandardServletInputData(request);
		localInput.set(input);
		HttpOutputData output = new StandardServletOutputData(request,
				response);
		localOutput.set(output);

		try {
			request.setAttribute(InputData.INPUT_DATA_KEY, input);
			request.setAttribute(OutputData.OUTPUT_DATA_KEY, output);

			childService._getService().action(null, input, output);
		}
		finally {
			localInput.set(null);
			localOutput.set(null);
		}
	}
}