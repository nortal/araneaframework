package org.araneaframework.example.main.message;

import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.SeriesMessage;
import org.araneaframework.example.main.release.demos.ModalDialogDemoWidget;
import org.araneaframework.example.main.web.menu.MenuWidget;
import org.araneaframework.framework.MountContext.MessageFactory;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ExampleOverlayDemoMessageFactory implements MessageFactory {

  private static final long serialVersionUID = 1L;

  public Message buildMessage(String url, final String suffix, InputData input,
      OutputData output) {
    return new OverlayBroadcastMessage();
  }

  private class OverlayBroadcastMessage extends BroadcastMessage {

    private static final long serialVersionUID = 1L;

    protected void execute(Component component) throws Exception {
      if (component instanceof MenuWidget) {
        ((MenuWidget) component).start(new ModalDialogDemoWidget());
      }
    }

  }

}
