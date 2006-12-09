
package org.araneaframework.integration.jsf.core;

import java.io.IOException;
import java.util.Locale;
import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.log4j.Logger;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.filter.StandardJspFilterService;
import org.araneaframework.integration.jsf.util.AraneaFacesContextUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class AraneaViewHandlerWrapper extends ViewHandlerWrapper {
	private static final Logger log =  Logger.getLogger(AraneaViewHandlerWrapper.class);
    private ViewHandler viewHandler;
    
    protected ViewHandler getWrapped() {
		return viewHandler;
	}

	/** Creates a new instance of ViewHandlerDecorator */
    public AraneaViewHandlerWrapper(ViewHandler viewHandler) {
    	Assert.notNullParam(this, viewHandler, "viewHandler");
        this.viewHandler = viewHandler;
    }

    public String calculateCharacterEncoding(FacesContext facesContext) {
    	JspContext jspContext = getAraneaJspContext(facesContext);
    	if (jspContext != null) {
    		// XXX: unsafe cast and bad dependency
    		return ((StandardJspFilterService.JspConfiguration) jspContext).getSubmitCharset();
    	}
        return super.calculateCharacterEncoding(facesContext);
    }

    public Locale calculateLocale(FacesContext context) {
        return getAraneaLocalizationContext(context).getLocale();
    }
    
    public void initView(FacesContext context) throws FacesException {
		super.initView(context);
	}

    //  XXX: now this is WTF
	protected String generateViewId(FacesContext context) {
		String result = null;
    	if (context.getViewRoot() == null) {
    		Object request = context.getExternalContext().getRequest();
    		while ((!(request instanceof AraneaJsfRequestWrapper)) && (request instanceof HttpServletRequestWrapper)) {
    			request = ((HttpServletRequestWrapper) request).getRequest();
    		}
    		if (request instanceof AraneaJsfRequestWrapper)
    			result = ((AraneaJsfRequestWrapper)request).getViewSelector();
    	}
    	return result;
	}
    
    public UIViewRoot createView(FacesContext context, String viewId) {
    	if (log.isDebugEnabled())
    		log.debug("View is being created from '" + viewId + "'");

        return super.createView(context, context.getViewRoot() == null ? generateViewId(context) : viewId);
    }

    public UIViewRoot restoreView(FacesContext context, String viewId) {
    	if (log.isDebugEnabled())
    		log.debug("View is being restored from '" + viewId + "'");

        return super.restoreView(context, context.getViewRoot() == null ? generateViewId(context) : viewId);
    }
    
    public void renderView(FacesContext context, UIViewRoot viewToRender) throws IOException, FacesException {
    	if (log.isDebugEnabled())
    		log.debug("View is being rendered.");
		super.renderView(context, viewToRender);
	}

	public LocalizationContext getAraneaLocalizationContext(FacesContext facesContext) {
    	return AraneaFacesContextUtil.getLocalizationContext(facesContext);
    }
	
	public JspContext getAraneaJspContext(FacesContext facesContext) {
		return AraneaFacesContextUtil.getJspContext(facesContext);
	}
}
