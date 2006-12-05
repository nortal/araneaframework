
package org.araneaframework.integration.jsf.useless;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import org.araneaframework.OutputData;
import org.araneaframework.framework.LocalizationContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ViewHandlerDecorator extends ViewHandler {
    ViewHandler viewHandler;
    
    /** Creates a new instance of ViewHandlerDecorator */
    public ViewHandlerDecorator(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }

    public void writeState(FacesContext context) throws IOException {
        viewHandler.writeState(context);
    }
    // this should use our own
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
        return viewHandler.createView(context, viewId);
    }

    public String getActionURL(FacesContext context, String viewId) {
        return viewHandler.getActionURL(context, viewId);
    }

    public String getResourceURL(FacesContext context, String path) {
        return viewHandler.getResourceURL(context, path);
    }

    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return viewHandler.restoreView(context, viewId);
    }

    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
        viewHandler.renderView(context, viewToRender);
    }
    
    public LocalizationContext getLocalizationContext(FacesContext context) {
    	Map requestAttributes = context.getExternalContext().getRequestMap();
    	OutputData output = (OutputData) requestAttributes.get(OutputData.OUTPUT_DATA_KEY);
    	LocalizationContext locCtx = (LocalizationContext) output.getAttribute(LocalizationContext.LOCALIZATION_CONTEXT_KEY);
    	return locCtx;
    }
}
