package org.araneaframework.framework.filter;

import org.araneaframework.OutputData;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.filter.NotRenderedMessage;

/**
 * This filter resets all {@link org.araneaframework.framework.core.RenderStateAware} components
 * render state to unrendered prior to calling render on its children.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @since 1.1
 */
public class StandardRenderStatusResettingFilterWidget extends BaseFilterWidget {
  protected void render(OutputData output) throws Exception {
    NotRenderedMessage.INSTANCE.send(null, this.childWidget);
    super.render(output);
  }
}
