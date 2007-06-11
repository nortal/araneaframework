package org.araneaframework.http.filter;

import org.araneaframework.OutputData;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class RenderStatusResettingFilterWidget extends BaseFilterWidget {
	protected NotRenderedMessage notRenderedMessage = NotRenderedMessage.INSTANCE;
	
	protected void render(OutputData output) throws Exception {
		notRenderedMessage.send(null, this.childWidget);
		super.render(output);
	}
}
