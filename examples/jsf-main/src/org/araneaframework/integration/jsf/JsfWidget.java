package org.araneaframework.integration.jsf;

import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.AraneaJsfRequestWrapper;
import org.araneaframework.integration.jsf.core.AraneaJsfResponseWrapper;
import org.araneaframework.integration.jsf.core.AraneaJsfStateManagerWrapper;
import org.araneaframework.integration.jsf.core.JSFContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfWidget extends BaseApplicationWidget {
	private static final Logger log = org.apache.log4j.Logger.getLogger(JsfWidget.class);
    protected String viewSelector;
    
    protected boolean justInited;
    
    protected Object state;
    
    /** Creates a new instance of JsfWidget */
    public JsfWidget(String viewSelector) {
        this.viewSelector = viewSelector;
    }
    
    protected void init() throws Exception {
    	if (log.isDebugEnabled())
    		log.debug("JSF view from '" + viewSelector + "'");
    	addEventListener("endFlow", new ProxyEventListener(this));
    	justInited = true;
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
        StateManager mng = facesContext.getApplication().getStateManager();
        
//        if (state != null) {
//        	AraneaJsfStateManagerWrapper manager = (AraneaJsfStateManagerWrapper) mng;
//        	UIViewRoot root = 
//        		manager.restoreView(facesContext, getViewSelector(), getJSFContext().getApplication().getViewHandler().calculateRenderKitId(facesContext));
//
//        	manager.restoreComponentState(facesContext, root, getJSFContext().getApplication().getViewHandler().calculateRenderKitId(facesContext));
//        }

        getJSFContext().getLifecycle().execute(facesContext);
        getJSFContext().getLifecycle().render(facesContext);

        if (!facesContext.getViewRoot().getViewId().equals(viewSelector))
        	log.debug("ViewSelector changed from '" + viewSelector + "' to '" + facesContext.getViewRoot().getViewId()+ "'");

        //state = mng.saveView(facesContext);

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
    
    protected void wrapJsfRequest(InputData input) {
    	HttpServletRequest request;
    	if (!justInited) {
    		try {
    			request = (HttpServletRequest) input.narrow(JSFRequest.class);
    		} catch (NoSuchNarrowableException e) {
    			// then it does not really matter;
    			request = ServletUtil.getRequest(input);
    		}
    	}
    	else {
    		justInited = false;
    		request = ServletUtil.getRequest(input);
    	}

    	ServletUtil.setRequest(input, new AraneaJsfRequestWrapper(request, viewSelector));
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
    		ServletUtil.setRequest(input, (HttpServletRequest) req.getRequest());
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
    
    public void handleEventEndFlow(String param) {
    	((FlowContext)getEnvironment().getEntry(FlowContext.class)).finish(param);
    }
}
