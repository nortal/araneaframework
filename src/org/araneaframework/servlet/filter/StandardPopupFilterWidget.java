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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.EnvironmentAwareServiceFactory;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.framework.router.StandardTopServiceRouterService;
import org.araneaframework.servlet.PopupServiceInfo;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.support.PopupWindowProperties;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardPopupFilterWidget extends BaseFilterWidget implements PopupWindowContext {
	private static final Logger log = Logger.getLogger(StandardPopupFilterWidget.class);
	
	/** Default context class for popup services */
	protected Class defaultContext = ThreadContext.class;
	
	/** Maps of popups where keys are service IDs(==popup IDs) and values 
	 * <code>StandardPopupFilterWidget.PopupServiceInfo</code>. Used for rendering popups.*/ 
	protected Map popups = new HashMap();
	/** Used to keep track of popups that have been opened from thread and not yet explicitly closed. */
	protected Map allPopups = new HashMap();

	/** Map of &lt;Class serviceContextClass, EnvironmentAwareServiceFactory factory &gt;. 
	 *  Factories are used to create new popup services in context of serviceContextClass. */
	protected Map serviceFactoryMap;
	
	public void setServiceFactoryMap(Map serviceFactories) throws Exception {
		this.serviceFactoryMap = new HashMap();
		for (Iterator i = serviceFactories.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry)i.next();
			this.serviceFactoryMap.put((Class)Class.forName((String)entry.getKey()), entry.getValue());
		}
	}
	
	protected Environment getChildWidgetEnvironment() {
		return new StandardEnvironment(super.getChildWidgetEnvironment(), PopupWindowContext.class, this);
	}

	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties, org.araneaframework.Message)
	 */
	public String open(String idPrefix, PopupWindowProperties properties, Message startMessage) throws Exception {
		return open(idPrefix, properties, startMessage, defaultContext);
	}
	
	public String open(String idPrefix, PopupWindowProperties properties, Message startMessage, Class serviceContext) throws Exception {
		// append random suffix for requested service id
		String rndString = getRandomServiceId();
		String id = (idPrefix != null) ? new StringBuffer(idPrefix).append(rndString).toString() : rndString;
		
		String topServiceId = isThreadServiceType(serviceContext) ? (String) getTopServiceCtx().getCurrentId() : id;
		String threadServiceId = isThreadServiceType(serviceContext) ? (String) id : null;
		
		Service service = ((EnvironmentAwareServiceFactory)serviceFactoryMap.get(serviceContext)).buildService(getEnvironment());
		startPopupService(id, service, serviceContext);

		if (startMessage != null)
			startMessage.send(null, service);
		
		//add new, not yet opened popup to popup map
		popups.put(id, new StandardPopupServiceInfo(topServiceId, threadServiceId, properties));
		allPopups.put(id, popups.get(id));
		
		log.debug("Popup service with identifier '" + id + "' was created.");
		return id;
	}
	
	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties)
	 */
	public String open(String idPrefix, Service service, PopupWindowProperties properties) throws Exception {
		return open(idPrefix, service, properties, defaultContext);
	}
	
	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties, int)
	 */
	public String open(String idPrefix, Service service, PopupWindowProperties properties, Class serviceContext) throws Exception {
		String rndString = getRandomServiceId();
		String id = (idPrefix != null) ? new StringBuffer(idPrefix).append(rndString).toString() : rndString;

		TopServiceContext topServiceCtx = getTopServiceCtx();
		String topServiceId = isThreadServiceType(serviceContext) ? (String) topServiceCtx.getCurrentId() : id;
		String threadServiceId = isThreadServiceType(serviceContext) ? id : null;
		
		startPopupService(id, service, serviceContext);
		popups.put(id, new StandardPopupServiceInfo(topServiceId, threadServiceId, properties));
		allPopups.put(id, popups.get(id));
		
		log.debug("Popup service with identifier '" + id + "' was registered.");
		return id;
	}
	
	protected void startPopupService(String id, Service service, Class serviceContext) throws Exception {
		getServiceCtx(serviceContext).addService(id, service);
	}
	
	public boolean close(String id, Class serviceContext) throws Exception {
		if (!allPopups.containsKey(id)) {
			log.warn("Attempt to close non-owned, unopened or already closed popup service with ID +'" + id + "'.");
			return false;
		}

		getServiceCtx(serviceContext).close(id);
		allPopups.remove(id);

		if (log.isDebugEnabled())
			log.debug("Popup service with identifier '" + id + "' was closed");
		return true;
	}

	protected final boolean isThreadServiceType(Class serviceType) {
		return (serviceType.equals(ThreadContext.class));
	}
	
	protected void action(Path path, InputData input, OutputData output) throws Exception {
		super.action(path, input, output);
	}
	
	protected void event(Path path, InputData input) throws Exception {
		// TODO: somehow get the whatever context this service is associated with and kill it without mercy if
		// popup closing key is found.
		super.event(path, input);
	}
	
	/** 
	 * Popups are rendered by pushing <code>Map &lt;String serviceId, PopupServiceInfo serviceInfo&gt;</code>
	 * into output under the key {@link org.araneaframework.servlet.PopupWindowContext}.POPUPS_KEY
	 */
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
	
	protected String getRandomServiceId() {
		return RandomStringUtils.random(8, false, true);
	}
	
	public ManagedServiceContext getServiceCtx(Class contextClass) {
		return (ManagedServiceContext)(getEnvironment().requireEntry(contextClass));
	}
	
	public TopServiceContext getTopServiceCtx() {
		return ((TopServiceContext)getEnvironment().requireEntry(TopServiceContext.class));
	}
	
	public ThreadContext getThreadCtx() {
		return ((ThreadContext)getEnvironment().requireEntry(ThreadContext.class));
	}
	
	public class StandardPopupServiceInfo implements PopupServiceInfo {
		private String topServiceId;
		private String threadId;
		private PopupWindowProperties popupProperties;
		
		public StandardPopupServiceInfo(String topServiceId, String threadId, PopupWindowProperties popupProperties) {
			this.topServiceId = topServiceId;
			this.threadId = threadId;
			this.popupProperties = popupProperties;
		}

		public PopupWindowProperties getPopupProperties() {
			return popupProperties;
		}

		public String toURLParams() {
			StringBuffer urlSuffix = new StringBuffer(StandardTopServiceRouterService.TOP_SERVICE_KEY + "=").append(topServiceId);
			if (threadId != null) {
		      urlSuffix.append("&" + StandardThreadServiceRouterService.THREAD_SERVICE_KEY + "="); 
		      urlSuffix.append(threadId);
			}
			return urlSuffix.toString();
		}

		public Map getServiceInfo() {
			Map map = new HashMap();
			map.put(StandardTopServiceRouterService.TOP_SERVICE_KEY, topServiceId);
			map.put(StandardThreadServiceRouterService.THREAD_SERVICE_KEY, threadId);
			return map;
		}
	}
}
