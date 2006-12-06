package org.araneaframework.integration.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.AraneaJsfRequestWrapper;
import org.araneaframework.integration.jsf.core.AraneaJsfResponseWrapper;
import org.araneaframework.integration.jsf.core.JSFContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfWidget extends BaseApplicationWidget {
	private static final Logger log = org.apache.log4j.Logger.getLogger(JsfWidget.class);
    protected String viewSelector;
    
    /** Creates a new instance of JsfWidget */
    public JsfWidget(String viewSelector) {
        this.viewSelector = viewSelector;
    }
    
    protected void init() throws Exception {
    	if (log.isDebugEnabled())
    		log.debug("JSF view from '" + viewSelector + "'");
    }

    protected void handleUpdate(InputData input) throws Exception {        
        
    }

    protected void event(Path path, InputData input) throws Exception {
        super.event(path, input);
    }

    protected void process() throws Exception {
        super.process();
    }

    protected void render(OutputData output) throws Exception {
        FacesContext facesContext = initFacesContext();

        getJSFContext().getLifecycle().execute(facesContext);
        getJSFContext().getLifecycle().render(facesContext);

        if (!facesContext.getViewRoot().getViewId().equals(viewSelector))
        	log.debug("ViewSelector changed from '" + viewSelector + "' to '" + facesContext.getViewRoot().getViewId()+ "'");
        
        getJSFContext().destroyFacesContext(facesContext);
        
        // restore request and response
        restoreRequest(getInputData());
        restoreResponse(getOutputData());
    }
    
    protected FacesContext initFacesContext() {
        InputData input = getInputData();
        OutputData output = getOutputData();

        // wrap the request
        wrapJsfRequest(input);
        wrapJsfResponse(output);

        // XXX: should give dummy outputdata
        return getJSFContext().initFacesContext(input, output);
    }
    
    protected HttpServletRequest wrapJsfRequest(InputData input) {
    	HttpServletRequest request = ServletUtil.getRequest(input);
    	ServletUtil.setRequest(input, new AraneaJsfRequestWrapper(request, viewSelector));
    	return ServletUtil.getRequest(input);
    }
    
    protected HttpServletResponse wrapJsfResponse(OutputData output) {
    	HttpServletResponse response = ServletUtil.getResponse(output);
    	ServletUtil.setResponse(output,new AraneaJsfResponseWrapper(response));
    	return ServletUtil.getResponse(output);
    }
    
    protected void restoreRequest(InputData input) {
    	HttpServletRequest request = ServletUtil.getRequest(input);
    	if (request instanceof AraneaJsfRequestWrapper) {
    		AraneaJsfRequestWrapper req = (AraneaJsfRequestWrapper) request;
    		ServletUtil.setRequest(input, req.getOriginalRequest());
    	}
    }
    
    protected void restoreResponse(OutputData input) {
    	//XXX: should do?
    }

    public JspContext getJspContext() {
        return (JspContext) getEnvironment().getEntry(JspContext.class);
    }
    
    public JSFContext getJSFContext() {
        return (JSFContext) getEnvironment().getEntry(JSFContext.class);
    }
    
    public String getViewSelector() {
    	return viewSelector;
    }
    
    public void handleEventSubmit() {
    	log.debug("Handling submit ");
    }
}
