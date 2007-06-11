package org.araneaframework.http.filter;

import org.araneaframework.Component;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.framework.core.Renderable;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class NotRenderedMessage extends BroadcastMessage {
	public static final NotRenderedMessage INSTANCE = new NotRenderedMessage();
	
	protected void execute(Component component) throws Exception {
		if (component instanceof Renderable) {
			((Renderable) component).notRendered();
		}
	}
}