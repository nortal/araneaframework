package org.araneaframework.example.main.message;

import org.araneaframework.Message;
import org.araneaframework.core.SeriesMessage;

public class LoginAndMenuSelectMessage extends SeriesMessage {

  private static final long serialVersionUID = 1L;

  public LoginAndMenuSelectMessage(String menuPath) {
    super(new Message[] {
        new LoginMessage(), 
        new MenuSelectMessage(menuPath)});
  }

}
