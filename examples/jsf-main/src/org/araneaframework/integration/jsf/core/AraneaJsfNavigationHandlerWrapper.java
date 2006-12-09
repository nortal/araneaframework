package org.araneaframework.integration.jsf.core;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

public class AraneaJsfNavigationHandlerWrapper extends NavigationHandler {
	private static final Logger log = Logger.getLogger(AraneaJsfNavigationHandlerWrapper.class);
	protected NavigationHandler wrapped;

	public AraneaJsfNavigationHandlerWrapper(NavigationHandler superHandler) {
		this.wrapped = superHandler;
	}
	
	public void handleNavigation(FacesContext facesContext, String fromAction, String outcome) {
		if (log.isDebugEnabled())
			log.debug("Handling navigation from '" + fromAction + "' with outcome '" + outcome + "'");
		wrapped.handleNavigation(facesContext, fromAction, outcome);
	}
}
