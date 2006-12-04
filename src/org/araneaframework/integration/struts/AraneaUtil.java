package org.araneaframework.integration.struts;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.EventListener;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.MountContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.springframework.beans.factory.BeanFactory;

public class AraneaUtil {
  private StrutsWidget parent;
  
  private AraneaUtil(StrutsWidget parent) {
    this.parent = parent;
  }

  public static boolean present(HttpServletRequest req) {
    OutputData output = ServletUtil.getOutputData(req);
    if (output == null) return false;
    
    StrutsWidget parent = (StrutsWidget) output.getAttribute(StrutsWidget.STRUTS_WIDGET_KEY);
    if (parent == null) return false;
    
    return true;
  }
  
  public static AraneaUtil get(HttpServletRequest req) {
    OutputData output = ServletUtil.getOutputData(req);
    StrutsWidget parent = (StrutsWidget) output.getAttribute(StrutsWidget.STRUTS_WIDGET_KEY);
    return new AraneaUtil(parent);
  }

  public void addEventListener(Object eventId, EventListener listener) {
    parent.addEventListener(eventId, listener);
  }

  public void addWidget(Object key, Widget child) {
    parent.addWidget(key, child);
    child._getWidget().process();
  }

  public void clearEventlisteners(Object eventId) {
    parent.clearEventlisteners(eventId);
  }

  public void disableWidget(Object key) {
    parent.disableWidget(key);
  }

  public void enableWidget(Object key) {
    parent.enableWidget(key);
  }

  public Map getChildren() {
    return parent.getChildren();
  }

  public Environment getEnvironment() {
    return parent.getEnvironment();
  }

  public Widget getWidget(Object key) {
    return parent.getWidget(key);
  }

  public ConfigurationContext getConfiguration() {
    return (ConfigurationContext) getEnvironment().requireEntry(ConfigurationContext.class);
  }
  
  public FlowContext getFlowCtx() {
    return (FlowContext) getEnvironment().requireEntry(FlowContext.class);
  }
  
  public MessageContext getMessageCtx() {
    return (MessageContext) getEnvironment().requireEntry(MessageContext.class);
  }
  
  public LocalizationContext getL10nCtx() {
    return (LocalizationContext) getEnvironment().requireEntry(LocalizationContext.class);
  }
  
  public MountContext getMountCtx() {
    return (MountContext) getEnvironment().requireEntry(MountContext.class);
  }
  
  public BeanFactory getBeanFactory() {
    return (BeanFactory) getEnvironment().requireEntry(BeanFactory.class);
  }
}
