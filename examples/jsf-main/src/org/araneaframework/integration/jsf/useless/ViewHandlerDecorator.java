
package org.araneaframework.integration.jsf.useless;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

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

    public String calculateCharacterEncoding(FacesContext context) {
        return viewHandler.calculateCharacterEncoding(context);
    }

    public Locale calculateLocale(FacesContext context) {
        return viewHandler.calculateLocale(context);
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
}
