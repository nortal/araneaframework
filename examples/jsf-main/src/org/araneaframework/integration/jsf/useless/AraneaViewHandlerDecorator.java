
package org.araneaframework.integration.jsf.useless;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.integration.jsf.util.AraneaFacesContextUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class AraneaViewHandlerDecorator extends ViewHandlerWrapper {
	private static final Logger log =  Logger.getLogger(AraneaViewHandlerDecorator.class);
    private ViewHandler viewHandler;
    
    protected ViewHandler getWrapped() {
		return viewHandler;
	}

	/** Creates a new instance of ViewHandlerDecorator */
    public AraneaViewHandlerDecorator(ViewHandler viewHandler) {
    	Assert.notNullParam(this, viewHandler, "viewHandler");
        this.viewHandler = viewHandler;
    }


    // this should use our own too
    public String calculateCharacterEncoding(FacesContext context) {
        return viewHandler.calculateCharacterEncoding(context);
    }

    public Locale calculateLocale(FacesContext context) {
        return getAraneaLocalizationContext(context).getLocale();
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
    	if (log.isDebugEnabled())
    		log.debug("View is being created from '" + viewId + "'");
        return super.createView(context, viewId);
    }
    
    public UIViewRoot restoreView(FacesContext context, String viewId) {
    	if (log.isDebugEnabled())
    		log.debug("View is being restored from '" + viewId + "'");
        return super.restoreView(context, viewId);
    }
    
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
    	if (log.isDebugEnabled())
    		log.debug("View is being rendered.");
		super.renderView(context, viewToRender);
	}

	public LocalizationContext getAraneaLocalizationContext(FacesContext facesContext) {
    	return AraneaFacesContextUtil.getLocalizationContext(facesContext);
    }
}
