package org.araneaframework.framework.confirmation;

import java.io.Serializable;

/**
 * Represents confirmation button, has some code, text (shown to user, should be localized), attribute leavePage
 * (indicates whether button confirms leaving page, if false then user stays on the same page). Button click listener
 * could be registered, but not mandatory, gives possibility to perform custom action on button click event.
 * 
 * @author Maksim Boiko
 */
public class ConfirmationButton implements Serializable {
  private String code;
  private String text;
  protected boolean leavePage;
  private transient ClickListener clickListener;

  public ConfirmationButton(String text) {
    this.text = text;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public String getText() {
    return text;
  }

  public boolean canLeavePage() {
    return leavePage;
  }

  public void addListener(ClickListener clickListener) {
    this.clickListener = clickListener;
  }

  public ClickListener getClickListener() {
    return clickListener;
  }

  /**
   * Listens user button push events.
   * 
   * @author Maksim Boiko <mailto:max@webmedia.ee>
   */
  public interface ClickListener extends Serializable {
    public void onClick();
  }
}
