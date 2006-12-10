package org.araneaframework.integration.jsf;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.AraneaJsfRequestWrapper;
import org.araneaframework.integration.jsf.core.AraneaJsfResponseWrapper;
import org.araneaframework.integration.jsf.core.AraneaJsfStateManagerWrapper;
import org.araneaframework.integration.jsf.core.JSFContext;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfWidget extends BaseApplicationWidget {
	private static final Logger log = org.apache.log4j.Logger.getLogger(JsfWidget.class);
    protected String viewSelector;
    
    protected boolean justInited;
    
    protected Object savedViewRoot;
    
    private transient FacesContext fcontext;
    
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
    	fcontext = initFacesContext();
    	 getJSFContext().getLifecycle().execute(fcontext);
    	
        super.event(path, input);
    }

    protected void process() throws Exception {
        super.process();
    }

    protected void render(OutputData output) throws Exception {
    	String s = null;
    	HttpServletResponse response = null;
    	try {
    		response = ServletUtil.getResponse(output);
    		
		    FacesContext facesContext = fcontext;
		    if (facesContext == null) {
		    	facesContext = initFacesContext();
		    	 getJSFContext().getLifecycle().execute(facesContext);
		    }
		    StateManager mng = facesContext.getApplication().getStateManager();
		    
		    boolean doesit = facesContext.getExternalContext().getRequestParameterMap().containsKey(ResponseStateManager.VIEW_STATE_PARAM);
		    
		    // XXX: just experimenting
		    if (savedViewRoot  != null) {
		    	AraneaJsfStateManagerWrapper manager = (AraneaJsfStateManagerWrapper) mng;
		//        	UIViewRoot root = 
		//        		manager.restoreView(facesContext, getViewSelector(), getJSFContext().getApplication().getViewHandler().calculateRenderKitId(facesContext));
		
		    	//manager.restoreComponentState(facesContext, root, getJSFContext().getApplication().getViewHandler().calculateRenderKitId(facesContext));
		    }
		
		  
		    getJSFContext().getLifecycle().render(facesContext);
		
		    // navigationhandler has kicked in
		    if (!facesContext.getViewRoot().getViewId().equals(viewSelector)) {
		    	if (log.isDebugEnabled())
		    		log.debug("ViewSelector changed from '" + viewSelector + "' to '" + facesContext.getViewRoot().getViewId()+ "'");
		    	viewSelector = facesContext.getViewRoot().getViewId();
		    }
		    
		    savedViewRoot = facesContext.getViewRoot().saveState(facesContext);
		
		    //state = mng.saveView(facesContext);
		
		    getJSFContext().destroyFacesContext(facesContext);
		    
		    // peek the response
		    MockHttpServletResponse r = (MockHttpServletResponse) ((AraneaJsfResponseWrapper) ServletUtil.getResponse(getOutputData())).getResponse();
		     s = r.getContentAsString();
		    
		    justInited = false;
		    //XXX: not strict enpohyhg
		    s= s.replaceAll("\"javax.faces.ViewState\"", "\""+ getWidgetStateParam() + "\"");
    	} finally {
            restoreRequest(getInputData());
            restoreResponse(output, response);
    	}

        // restore request and response

        
        ServletUtil.getResponse(getOutputData()).getWriter().write(s);
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
//    	if (!justInited) {
//    		try {
//    			request = (HttpServletRequest) input.narrow(JSFRequest.class);
//    		} catch (NoSuchNarrowableException e) {
//    			// then it does not really matter;
//    			request = ServletUtil.getRequest(input);
//    		}
//    	}
//    	else {
//    		justInited = false;
//    		request = ServletUtil.getRequest(input);
//    	}
    	ServletUtil.setRequest(input, new AraneaJsfRequestWrapper(ServletUtil.getRequest(input), this));
    }
    
    
    
    
    //HttpServletResponse resp;
    protected HttpServletResponse wrapJsfResponse(OutputData output) {
    	ServletUtil.setResponse(output,new AraneaJsfResponseWrapper(new MockHttpServletResponse()));
    	return ServletUtil.getResponse(output);
    }
    
    protected void restoreRequest(InputData input) {
    	HttpServletRequest request = ServletUtil.getRequest(input);
    	if (request instanceof AraneaJsfRequestWrapper) {
    		AraneaJsfRequestWrapper req = (AraneaJsfRequestWrapper) request;
    		ServletUtil.setRequest(input, req.getOriginalRequest());
    	}
    }
    
    protected void restoreResponse(OutputData output, HttpServletResponse response) {
    	ServletUtil.setResponse(output,response);
//    	resp = null;
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
    
    public boolean isInitializingRequest() {
    	return justInited;
    }
    
    public String getWidgetStateParam() {
    	return getOutputData().getScope() + "-JSFSTATE";
    }
    
    public void handleEventEndFlow(String param) {
    	((FlowContext)getEnvironment().getEntry(FlowContext.class)).finish(param);
    }
}
