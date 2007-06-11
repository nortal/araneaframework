package org.araneaframework.http.filter;

import org.araneaframework.Component;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.framework.core.RenderStateAware;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @since 1.1
 */
public class NotRenderedMessage extends BroadcastMessage {
  public static final NotRenderedMessage INSTANCE = new NotRenderedMessage();
  
  protected void execute(Component component) throws Exception {
    if (component instanceof RenderStateAware) {
      ((RenderStateAware) component).setRendered(false);    }
  }
}