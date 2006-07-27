package org.araneaframework.servlet.message;

import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.framework.router.StandardSessionServiceRouterService;
import org.araneaframework.servlet.router.StandardServletSessionRouterService;

public class SessionMessage extends BroadcastMessage {
  private Message load;
  private InputData input;
  private OutputData output;
      
  public SessionMessage(Message load, InputData input, OutputData output) {
    this.load = load;
    this.input = input;
    this.output = output;
  }
  
  protected void execute(Component component) throws Exception {
    if (component instanceof StandardServletSessionRouterService) {
      ((StandardServletSessionRouterService) component).propagate(load, input, output);
    }
      
  }

}
