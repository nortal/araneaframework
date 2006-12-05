
package org.araneaframework.integration.jsf.useless;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import org.apache.log4j.Logger;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.ServletUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ViewHandlerDecorator extends ViewHandler {
	private static final Logger log =  Logger.getLogger(ViewHandlerDecorator.class);
    private ViewHandler viewHandler;
    
    /** Creates a new instance of ViewHandlerDecorator */
    public ViewHandlerDecorator(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }

    public void writeState(FacesContext context) throws IOException {
        viewHandler.writeState(context);
    }
    // this should use our own too
    public String calculateCharacterEncoding(FacesContext context) {
        return viewHandler.calculateCharacterEncoding(context);
    }

    public Locale calculateLocale(FacesContext context) {
        return getLocalizationContext(context).getLocale();
    }

    public String calculateRenderKitId(FacesContext context) {
        return viewHandler.calculateRenderKitId(context);
    }

    public void initView(FacesContext context) throws FacesException {
        viewHandler.initView(context);
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
    	if (log.isDebugEnabled())
    		log.debug("View is being created from '" + viewId + "'");
        return viewHandler.createView(context, viewId);
    }
    
    public UIViewRoot restoreView(FacesContext context, String viewId) {
    	if (log.isDebugEnabled())
    		log.debug("View is being restored from '" + viewId + "'");
        return viewHandler.restoreView(context, viewId);
    }

    public String getActionURL(FacesContext context, String viewId) {
        return viewHandler.getActionURL(context, viewId);
    }

    public String getResourceURL(FacesContext context, String path) {
        return viewHandler.getResourceURL(context, path);
    }

    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        viewHandler.renderView(context, viewToRender);
    }
    
    public LocalizationContext getLocalizationContext(FacesContext context) {
    	HttpOutputData output = ServletUtil.getOutputData((ServletRequest)context.getExternalContext().getRequest());
    	LocalizationContext locCtx = (LocalizationContext) output.getAttribute(LocalizationContext.LOCALIZATION_CONTEXT_KEY);
    	return locCtx;
    }
}
