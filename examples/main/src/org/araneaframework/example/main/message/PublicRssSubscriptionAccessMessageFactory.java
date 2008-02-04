package org.araneaframework.example.main.message;

import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.SeriesMessage;
import org.araneaframework.example.main.web.course.RSSFeedReaderWidget;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.framework.MountContext.MessageFactory;
import org.araneaframework.http.core.StandardServletInputData;

public class PublicRssSubscriptionAccessMessageFactory implements MessageFactory {
  public Message buildMessage(String url, final String suffix, InputData input, OutputData output) {
    return new SeriesMessage(new Message[] {
      new LoginMessage(),
      new BroadcastMessage() {
        private static final long serialVersionUID = 1L;

        protected void execute(Component component) throws Exception {
          StandardServletInputData inputData =  (StandardServletInputData) component.getEnvironment().getEntry(InputData.class);
          String subscriber = (String) inputData.getGlobalData().get("rssSubscriber");
          if (component instanceof MenuWidget) {
            System.out.println("--------------------------------------------");
            System.out.println("Beware -- for user " + subscriber);
            System.out.println("--------------------------------------------");
            ((MenuWidget) component).start(new RSSFeedReaderWidget(true, subscriber));
            
          }
        }
      }
    });
  }
}
