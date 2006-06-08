package org.araneaframework.uilib.core;

import org.araneaframework.Message;
import org.araneaframework.Widget;

public class PopupFlowWidget extends StandardPresentationWidget {
  Message msg;
	
  public PopupFlowWidget(Widget widget, MessageFactory messageFactory) {
    
  }

  public interface MessageFactory {
    public Message buildMessage(Widget rootWidget);
  }
}
