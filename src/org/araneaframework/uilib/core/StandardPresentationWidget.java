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

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.servlet.JspContext;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.util.ServletUtil;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * Base widget class, has an id, can have children widgets and can process general widget events.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardPresentationWidget extends StandardWidget {
  private String viewSelector;  

  protected void setViewSelector(String viewSelector) {
    this.viewSelector = viewSelector;
  }  
  
  public ConfigurationContext getConfiguration() {
    return (ConfigurationContext) getEnvironment().getEntry(ConfigurationContext.class);
  }
  
  protected FlowContext getFlowCtx() {
    return (FlowContext) getEnvironment().getEntry(FlowContext.class);
  }
  
  protected MessageContext getMessageCtx() {
    return (MessageContext) getEnvironment().getEntry(MessageContext.class);
  }
  
  protected LocalizationContext getL10nCtx() {
    return (LocalizationContext) getEnvironment().getEntry(LocalizationContext.class);
  }
  
  protected String t(String key) {
    return getL10nCtx().localize(key);
  } 
  
  protected void render(OutputData output) throws Exception {
    if (viewSelector == null)
      throw new RuntimeException("Widget '" + getClass().getName() + "' does not have a view selector!"); 
    
    JspContext jspCtx = (JspContext) getEnvironment().requireEntry(JspContext.class);
    
    String jsp = jspCtx.getJspPath() + "/" + viewSelector + ".jsp";
    ServletUtil.include(jsp, getEnvironment(), (ServletOutputData) output);
  }
  
  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }
  
  protected class ComponentImpl extends StandardWidget.ComponentImpl {
    public synchronized void init(Environment env) throws Exception {
      super.init(env);
      
      addGlobalEventListener(new ProxyEventListener(this));
    }
  }
}
