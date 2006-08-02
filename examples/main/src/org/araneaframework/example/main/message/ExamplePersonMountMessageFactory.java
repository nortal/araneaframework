package org.araneaframework.example.main.message;

import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.MessageSeries;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.example.main.web.person.PersonAddEditWidget;
import org.araneaframework.framework.MountContext.MessageFactory;

public class ExamplePersonMountMessageFactory implements MessageFactory {
  public Message buildMessage(String url, final String suffix, InputData input, OutputData output) {
    return new MessageSeries(new Message[] {
        new LoginMessage(),
        new BroadcastMessage() {
          protected void execute(Component component) throws Exception {
            if (component instanceof MenuWidget) {
              ((MenuWidget) component).start(new PersonAddEditWidget(new Long(suffix)), null, null);
            }
          }
        }
    });
  }

}
