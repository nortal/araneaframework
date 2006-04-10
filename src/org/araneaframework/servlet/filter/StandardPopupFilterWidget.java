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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.support.PopupWindowProperties;
import org.springframework.beans.factory.BeanFactory;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardPopupFilterWidget extends BaseFilterWidget implements PopupWindowContext {
	private static final Logger log = Logger.getLogger(StandardPopupFilterWidget.class);
	
	/** keys for accessing the popup maps from viewmodels */
	public static final String POPUPS_KEY = "popupWindows";
	
	/** closing key for popups, if thread receives response containing that key, it dies. */
	public static final String POPUPS_CLOSE_KEY = "popupClose";
	
	/** Maps of popups where keys are service IDs(==popup IDs) and values popup window attributes. */ 
	protected Map popups = new HashMap();
	
	/** Holds properties for all types of popupwindows */
	protected Map popupProperties = new HashMap();
	
	private String topBean;
	
	/** Sets the name of Spring bean which instance will be created as new session thread. */
	public void setTopBean(String topBean) {
		this.topBean = topBean;
	}
	
	public String getTopBean() {
		return topBean;
	}
	
	protected void init() throws Exception {
		Map entries = new HashMap();
		entries.put(PopupWindowContext.class, this);
		childWidget._getComponent().init(new StandardEnvironment(getChildWidgetEnvironment(), entries));
	}
	
	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties, org.araneaframework.Message)
	 */
	public String open(String id, PopupWindowProperties properties, Message startMessage) throws Exception {
		ThreadContext currentThreadCtx = ((ThreadContext)getEnvironment().getEntry(ThreadContext.class));
		
		// append random suffix for requested service id
		String rndString = RandomStringUtils.random(8, false, true);
		id = (id != null) ? new StringBuffer(id).append(rndString).toString() : rndString;
		
		BeanFactory factory = (BeanFactory) getEnvironment().getEntry(BeanFactory.class);
		Service service = (Service) factory.getBean(topBean);
		currentThreadCtx.addService(id, service);
		
		if (startMessage != null)
			startMessage.send(null, service);
		
		//add new, not yet opened popup to popup and popup properties maps
		popups.put(id, properties.toString()); 
		popupProperties.put(id, properties);
		
		log.debug("Popup service with identifier '" + id + "' was created.");
		return id;
	}
	
	/** 
	 * @see org.araneaframework.servlet.PopupWindowContext#open(java.lang.String, org.araneaframework.servlet.support.PopupWindowProperties)
	 */
	public String open(String id, PopupWindowProperties properties) {
		popups.put(id, properties.toString());
		popupProperties.put(id, properties);
		
		log.debug("Popup service with identifier '" + id + "' was registered.");
		return id;
	}
	
	public void close(String id) throws Exception {
		ThreadContext currentThreadCtx = ((ThreadContext)getEnvironment().getEntry(ThreadContext.class));
		
		if (!popupProperties.containsKey(id) || popupProperties.get(id) == null) {
			//XXX throw some exception - or better not?
			log.warn("Attempt to close non-owned, unopened or already closed popup +'" + id + "'.");
		} else {
			currentThreadCtx.close(id);
			popups.remove(id);
			
			popupProperties.remove(id);
			if (log.isDebugEnabled())
				log.debug("Popup with identifier '" + id + "' was closed");
		} 
	}
	
	protected void action(Path path, InputData input, OutputData output) throws Exception {
		super.action(path, input, output);
	}
	
	protected void event(Path path, InputData input) throws Exception {
		ThreadContext currentThreadCtx = ((ThreadContext)getEnvironment().getEntry(ThreadContext.class));
		HttpServletRequest request = ((ServletInputData) input).getRequest();
		
		if (request.getParameter(POPUPS_CLOSE_KEY) != null) {
			if (log.isDebugEnabled())
				log.debug("Should be closing myself now, threadId  = '" + currentThreadCtx.getCurrentId() + "'" );
			currentThreadCtx.close(currentThreadCtx.getCurrentId());
		} 
		else {
			super.event(path, input);
		}
	}
	
	protected void render(OutputData output) throws Exception {
		output.pushAttribute(POPUPS_KEY, popups);
		popups = new HashMap();
		super.render(output);
	}
}
