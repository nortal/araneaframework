package org.araneaframework.example.main.message;

import org.araneaframework.Message;
import org.araneaframework.core.MessageSeries;

public class LoginAndMenuSelectMessage extends MessageSeries {

  public LoginAndMenuSelectMessage(String menuPath) {
    super(new Message[] {
        new LoginMessage(), 
        new MenuSelectMessage(menuPath)});
  }

}
