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

package org.araneaframework.uilib.core;

import javax.servlet.jsp.PageContext;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.Scope;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.MountContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.springframework.beans.factory.BeanFactory;

/**
 * This widget represents the usual custom application widget that is rendered using 
 * JSP tags. It assumes to be connected with a JSP page and allows setting its view selector.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class BaseUIWidget extends BaseApplicationWidget {

  protected String viewSelector;  

  /**
   * Sets the view selector for this widget, should be path to <code>jsp</code> file
   * without <code>jsp</code> extension.
   * 
   * @param viewSelector path to <code>jsp</code> file, without file extension 
   */
  protected void setViewSelector(String viewSelector) {
    this.viewSelector = viewSelector;
  }  
  
  public ConfigurationContext getConfiguration() {
    return (ConfigurationContext) getEnvironment().requireEntry(ConfigurationContext.class);
  }
  
  protected FlowContext getFlowCtx() {
    return (FlowContext) getEnvironment().requireEntry(FlowContext.class);
  }
  
  protected MessageContext getMessageCtx() {
    return (MessageContext) getEnvironment().requireEntry(MessageContext.class);
  }
  
  protected LocalizationContext getL10nCtx() {
    return (LocalizationContext) getEnvironment().requireEntry(LocalizationContext.class);
  }
  
  protected MountContext getMountCtx() {
    return (MountContext) getEnvironment().requireEntry(MountContext.class);
  }
  
  protected BeanFactory getBeanFactory() {
    return (BeanFactory) getEnvironment().requireEntry(BeanFactory.class);
  }
  
  /**
   * Translates the message under the given key, 
   * with help from widget's current {@link org.araneaframework.framework.LocalizationContext}.
   * 
   * @param key 
   * @return message under the key translated into language corresponding to current <code>Locale</code>
   */
  protected String t(String key) {
    return getL10nCtx().localize(key);
  } 
  
  /** 
   * Renders widget to <code>output</code> using the defined <code>viewSelector</code>.
   */
  protected void render(OutputData output) throws Exception {
    if (viewSelector == null)
      throw new RuntimeException("Widget '" + getClass().getName() + "' does not have a view selector!"); 
    
    JspContext jspCtx = (JspContext) getEnvironment().requireEntry(JspContext.class);
    
    String jsp = resolveJspName(jspCtx, viewSelector);
    ServletUtil.include(jsp, this, output);
  }
  
  protected String resolveJspName(JspContext jspCtx, String viewSelector) {
    return jspCtx.getJspPath() + "/" + viewSelector + jspCtx.getJspExtension();
  }
  
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }
  
  protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {
    public synchronized void init(Scope scope, Environment env) {
      setGlobalEventListener(new ProxyEventListener(BaseUIWidget.this));
	
      super.init(scope, env);
    }
  }

}
