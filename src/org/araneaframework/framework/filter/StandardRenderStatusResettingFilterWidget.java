package org.araneaframework.framework.filter;

import org.araneaframework.OutputData;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.filter.NotRenderedMessage;

/**
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
