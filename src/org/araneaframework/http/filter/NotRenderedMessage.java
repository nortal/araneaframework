
package org.araneaframework.http.filter;

import org.araneaframework.Component;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.framework.core.RenderStateAware;

/**
 * A message that can be propagated to sub-components so that all of them that implement {@link RenderStateAware} would
 * be set to *not rendered* state.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class NotRenderedMessage extends BroadcastMessage {

  /**
   * Since <code>NotRenderedMessage</code> does not have any state, the message should be retrieved from this instance.
   */
  public static final NotRenderedMessage INSTANCE = new NotRenderedMessage();

  private NotRenderedMessage() {
    super(RenderStateAware.class);
  }

  @Override
  protected void execute(Component component) throws Exception {
    ((RenderStateAware) component)._setRendered(false);
  }
}
