package org.araneaframework.uilib.core;

import org.araneaframework.Message;
import org.araneaframework.Widget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.support.PopupWindowProperties;

public class PopupFlowPseudoWidget {
  protected Widget widget;
  protected Widget opener;
  protected Message msg;
  protected PopupWindowContext popupContext;
  protected PopupWindowProperties properties; 
  PopupFlowContainerWidget popupContainer;
  
  protected FlowContext.Configurator configurator;
  protected FlowContext.Handler handler;
	
  public PopupFlowPseudoWidget(Widget widget, PopupWindowProperties properties, MessageFactory messageFactory, PopupWindowContext popupContext, Widget opener) {    this.opener = opener;
    this.opener = opener;
	this.msg = messageFactory.buildMessage(widget);
    this.widget = widget;
    this.properties = properties;
    this.popupContext = popupContext;
    popupContainer = new PopupFlowContainerWidget(this);
  }

  public interface MessageFactory {
    public Message buildMessage(Widget rootWidget);
  }
  
  public Widget getWidget() {
    return widget;
  }
  
  public Message getMessage() {
    return msg;
  }
  
  public PopupWindowContext getPopupContext() {
    return popupContext;
  }
  
  public Widget getOpener() {
    return opener;
  }
  
  public PopupFlowContainerWidget getPopupFlowContainerWidget() {
    return popupContainer;
  }
  
  public PopupWindowProperties getPopupWindowProperties() {
    return properties;
  }
  
  public void setConfigurator(FlowContext.Configurator configurator) {
    this.configurator = configurator;
  }
  
  public void setHandler(FlowContext.Handler handler) {
    this.handler = handler;
  }

  public FlowContext.Configurator getConfigurator() {
    return configurator;
  }

  public FlowContext.Handler getHandler() {
    return handler;
  }
}
