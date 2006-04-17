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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.EnvironmentAwareServiceFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardPopupFilterWidget extends BaseFilterWidget implements PopupWindowContext {
	private static final Logger log = Logger.getLogger(StandardPopupFilterWidget.class);
	
	/** Maps of popups where keys are service IDs(==popup IDs) and values 
	 * <code>StandardPopupFilterWidget.PopupServiceInfo</code>. Used for rendering popups.*/ 
	protected Map popups = new HashMap();
	/** Used to keep track of popups that have been opened from thread and not yet explicitly closed. */
	protected Map allPopups = new HashMap();
	
	/** The factory used to create Spring bean which instance will be created as new session thread. */
	protected EnvironmentAwareServiceFactory serviceFactory;
	
	public EnvironmentAwareServiceFactory getServiceFactory() {
		return serviceFactory;
	}

	public void setServiceFactory(EnvironmentAwareServiceFactory serviceFactory) {
		this.serviceFactory = serviceFactory;
	}

	protected void init() throws Exception {
		Map entries = new HashMap();
		entries.put(PopupWindowContext.class, this);
		childWidget._getComponent().init(new StandardEnvironment(getChildWidgetEnvironment(), entries));
	}
	
	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties, org.araneaframework.Message)
	 */
	public String open(String idPrefix, PopupWindowProperties properties, Message startMessage) throws Exception {
		return open(idPrefix, properties, startMessage, PopupWindowContext.THREAD_POPUP);
	}
	
	public String open(String idPrefix, PopupWindowProperties properties, Message startMessage, int serviceType) throws Exception {
		if (!isLegalServiceType(serviceType))
			throw new IllegalArgumentException("Specified service type value was found to be illegal.");
		
		// append random suffix for requested service id
		String rndString = RandomStringUtils.random(8, false, true);
		String id = (idPrefix != null) ? new StringBuffer(idPrefix).append(rndString).toString() : rndString;
		
		String topServiceId = isThreadServiceType(serviceType) ? (String) getTopServiceCtx().getCurrentId() : id;
		String threadServiceId = isThreadServiceType(serviceType) ? (String) id : null;
		
		Service service = serviceFactory.buildService(getEnvironment());
		startPopupService(id, service, serviceType);
		
		if (startMessage != null)
			startMessage.send(null, service);
		
		//add new, not yet opened popup to popup map
		popups.put(id, new PopupServiceInfo(topServiceId, threadServiceId, properties));
		allPopups.put(id, popups.get(id));
		
		log.debug("Popup service with identifier '" + id + "' was created.");
		return id;
	}
	
	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties)
	 */
	public String open(String id, PopupWindowProperties properties) {
		return open(id, properties, PopupWindowContext.THREAD_POPUP);
	}
	
	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties, int)
	 */
	public String open(String id, PopupWindowProperties properties, int serviceType) {
		if (!isLegalServiceType(serviceType))
			throw new IllegalArgumentException("Specified service type value was found to be illegal.");

		TopServiceContext topServiceCtx = getTopServiceCtx();
		String topServiceId = isThreadServiceType(serviceType) ? (String) topServiceCtx.getCurrentId() : id;
		String threadServiceId = isThreadServiceType(serviceType) ? id : null;
		
		popups.put(id, new PopupServiceInfo(topServiceId, threadServiceId, properties));
		allPopups.put(id, popups.get(id));
		
		log.debug("Popup service with identifier '" + id + "' was registered.");
		return id;
	}
	
	protected void startPopupService(String id, Service service, int serviceType) throws Exception {
		if (isThreadServiceType(serviceType))
			getThreadCtx().addService(id, service);
		else if (isTopServiceType(serviceType))
			getTopServiceCtx().addService(id, service);
	}
	
	public boolean closeThreadService(String id) throws Exception {
		if (!(allPopups.containsKey(id) && ((PopupServiceInfo)allPopups.get(id)).getThreadId() != null)) {
			//XXX throw some exception - or better not?
			log.warn("Attempt to close non-owned, unopened or already closed thread popup +'" + id + "'.");
			return false;
		}

		getThreadCtx().close(id);
		allPopups.remove(id);

		if (log.isDebugEnabled())
			log.debug("Thread popup with identifier '" + id + "' was closed");
		return true;
	}
	
	public boolean closeTopService(String id) throws Exception {
		if (!(allPopups.containsKey(id) && ((PopupServiceInfo)allPopups.get(id)).getThreadId() == null)) {
			//XXX throw some exception - or better not?
			log.warn("Attempt to close non-owned, unopened or already top service popup +'" + id + "'.");
			return false;
		}
		
		getTopServiceCtx().close(id);
		allPopups.remove(id);
		
		if (log.isDebugEnabled())
			log.debug("Thread popup with identifier '" + id + "' was closed");

		return true;
	}
	
	protected final boolean isThreadServiceType(int serviceType) {
		return (serviceType == PopupWindowContext.THREAD_POPUP);
	}
	
	protected final boolean isTopServiceType(int serviceType) {
		return (serviceType == PopupWindowContext.APPLICATION_POPUP);
	}
	
	protected boolean isLegalServiceType(int serviceType) {
		return isThreadServiceType(serviceType) || isTopServiceType(serviceType);
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		super.action(path, input, output);
	}
	
	protected void event(Path path, InputData input) throws Exception {
		ThreadContext threadCtx = getThreadCtx();
		HttpServletRequest request = ((ServletInputData) input).getRequest();
		
		if (request.getParameter(POPUPS_CLOSE_KEY) != null) {
			if (log.isDebugEnabled())
				log.debug("Should be closing myself now, threadId  = '" + threadCtx.getCurrentId() + "'" );
			threadCtx.close(threadCtx.getCurrentId());
		} 
		else {
			super.event(path, input);
		}
	}
	
	protected void render(OutputData output) throws Exception {
		output.pushAttribute(POPUPS_KEY, popups);
		
		try {
			super.render(output);
		}
		finally {
			output.popAttribute(POPUPS_KEY);
		}
		
		popups = new HashMap();
	}
	
	public TopServiceContext getTopServiceCtx() {
		return ((TopServiceContext)getEnvironment().getEntry(TopServiceContext.class));
	}
	
	public ThreadContext getThreadCtx() {
		return ((ThreadContext)getEnvironment().getEntry(ThreadContext.class));
	}
	
	public class PopupServiceInfo implements Serializable {
		private String topServiceId;
		private String threadId;
		private PopupWindowProperties popupProperties;
		
		public PopupServiceInfo(String topServiceId, String threadId, PopupWindowProperties popupProperties) {
			this.topServiceId = topServiceId;
			this.threadId = threadId;
			this.popupProperties = popupProperties;
		}

		public PopupWindowProperties getPopupProperties() {
			return popupProperties;
		}

		public String getThreadId() {
			return threadId;
		}

		public String getTopServiceId() {
			return topServiceId;
		}
	}
}
