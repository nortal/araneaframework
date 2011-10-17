package org.araneaframework.framework.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * Service which blocks framework render phase if corresponding flag exists in request.
 * @author Maksim Boiko (max@webmedia.ee)
 */
public class RenderBlockingWidgetAdapterService extends BaseFilterWidget {
  private static final Log LOG = LogFactory.getLog(RenderBlockingWidgetAdapterService.class);
  public static final String BLOCKING_RENDER_FLAG = "blockRender";

  @Override
  protected void render(OutputData output) throws Exception {
    if(!hasBlockingRenderFlag(getInputData())) {
      super.render(output);
    }
    else {
      LOG.debug("Render phase is blocked.");
    }
  }
  
  protected boolean hasBlockingRenderFlag(InputData input) {
    return input.getGlobalData().get(RenderBlockingWidgetAdapterService.BLOCKING_RENDER_FLAG) != null;
  }
}
