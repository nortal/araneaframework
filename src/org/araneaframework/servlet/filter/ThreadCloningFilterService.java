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

package org.araneaframework.servlet.filter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Service;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.core.StandardNonInitializableRelocatableService;
import org.araneaframework.core.StandardRelocatableServiceDecorator;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.ThreadCloningContext;
import org.araneaframework.servlet.filter.StandardPopupFilterWidget.StandardPopupServiceInfo;
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class ThreadCloningFilterService extends BaseFilterService implements ThreadCloningContext {
	private static final Logger log = Logger.getLogger(ThreadCloningFilterService.class);

	public ThreadCloningFilterService(Service childService) {
		super(childService);
	}

	public void setChildService(Service childService) {
		super.setChildService(new StandardRelocatableServiceDecorator(childService));
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		if (!cloningRequested(input)) {
			super.action(path, input, output);
			return;
		}
		
		ThreadContext threadCtx = getThreadServiceCtx();
		TopServiceContext topCtx = getTopServiceCtx();

		if (log.isDebugEnabled())
			log.debug("Attempting to clone current thread ('" + threadCtx.getCurrentId() + "')");

		// get the service to clone
		Relocatable.RelocatableService service = (RelocatableService) childService;
		Environment env = service._getRelocatable().getCurrentEnvironment();
		service._getRelocatable().overrideEnvironment(null);

        XStream xstream = new XStream(new DomDriver());
        RelocatableService t = (RelocatableService) xstream.fromXML(xstream.toXML(service));
        ThreadCloningFilterService cloneService = new ThreadCloningFilterService(t);

		// now how to add this?
        String cloneServiceId = RandomStringUtils.randomAlphabetic(12);
        threadCtx.addService(cloneServiceId, cloneService);

        StandardPopupServiceInfo threadInfo = 
            new StandardPopupServiceInfo((String)getTopServiceCtx().getCurrentId(), cloneServiceId, (PopupWindowProperties) null, getRequestURL());

        ((ServletOutputData) getCurrentOutput()).getResponse().sendRedirect(threadInfo.toURL());
	    
	    service._getRelocatable().overrideEnvironment(env);
	}

	protected void init() throws Exception {
		super.init();
	}
	
	protected boolean cloningRequested(InputData input) throws Exception {
		return input.getGlobalData().get(ThreadCloningContext.CLONING_REQUEST_KEY) != null;
	}
	
	protected String getRequestURL() {
		return ((HttpServletRequest)((ServletInputData)getCurrentInput()).getRequest()).getRequestURL().toString();
	}

	protected ThreadContext getThreadServiceCtx() {
		return ((ThreadContext)getEnvironment().requireEntry(ThreadContext.class));
	}

	protected TopServiceContext getTopServiceCtx() {
		return ((TopServiceContext)getEnvironment().requireEntry(TopServiceContext.class));
	}
}
