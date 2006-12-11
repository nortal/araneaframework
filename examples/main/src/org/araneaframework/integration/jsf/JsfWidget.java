package org.araneaframework.integration.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.AraneaJsfRequestWrapper;
import org.araneaframework.integration.jsf.core.AraneaJsfResponseWrapper;
import org.araneaframework.integration.jsf.core.JSFContext;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfWidget extends BaseApplicationWidget {
	private static final Logger log = org.apache.log4j.Logger.getLogger(JsfWidget.class);
    protected String viewSelector;
    protected boolean justInited;
    protected boolean doNotRender;
    
    protected transient FacesContext facesContext;
    protected transient HttpServletResponse response = null;
    
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
    
//	protected void destroy() throws Exception {
//		getJSFContext().destroyFacesContext(getCurrentFacesContext());
//		facesContext = null;
//		restoreResponse(getOutputData(), response);
//		response = null;
//	}

	protected void handleWidgetException(Exception e) throws Exception {
		restoreRequest(getInputData());
		restoreResponse(getOutputData(), response);
		destroyFacesContext();
		response = null;
		super.handleWidgetException(e);
	}

//	protected void handleUpdate(InputData input) throws Exception {
//		facesContext = initFacesContext();
//		getJSFContext().getLifecycle().execute(facesContext);
//		if (!facesContext.getViewRoot().getViewId().equals(viewSelector)) {
//			viewSelector = facesContext.getViewRoot().getViewId();
//		}
//    }
	

    protected void render(OutputData output) throws Exception {
    	if (doNotRender)
    		return;
    	
    	String s = null;
    	try {
    		response = ServletUtil.getResponse(output);
        ServletUtil.getRequest(getInputData()).setAttribute("widget", this);
    		
		    facesContext = getCurrentFacesContext();
		    if (facesContext == null) {
		    	facesContext = initFacesContext();
		    	getJSFContext().getLifecycle().execute(facesContext);
		    }
		    // XXX: just experimenting
//		    if (savedViewRoot  != null) {
		    	//AraneaJsfStateManagerWrapper manager = (AraneaJsfStateManagerWrapper) facesContext.getApplication().getStateManager();
		//        	UIViewRoot root = 
		//        		manager.restoreView(facesContext, getViewSelector(), getJSFContext().getApplication().getViewHandler().calculateRenderKitId(facesContext));
		
		    	//manager.restoreComponentState(facesContext, root, getJSFContext().getApplication().getViewHandler().calculateRenderKitId(facesContext));
//		    }
		
		  
		    getJSFContext().getLifecycle().render(facesContext);
		
		    // navigationhandler has kicked in
		    if (!facesContext.getViewRoot().getViewId().equals(viewSelector)) {
		    	if (log.isDebugEnabled())
		    		log.debug("ViewSelector changed from '" + viewSelector + "' to '" + facesContext.getViewRoot().getViewId()+ "'");
		    	viewSelector = facesContext.getViewRoot().getViewId();
		    }
		    
		    //savedViewRoot = facesContext.getViewRoot().saveState(facesContext);
		
		    destroyFacesContext();
		    
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

        wrapJsfRequest(input);
        wrapJsfResponse(output);

        return getJSFContext().initFacesContext(input, output);
    }
    
    protected void destroyFacesContext() {
    	FacesContext context = getCurrentFacesContext();
    	if (context != null)
    		getJSFContext().destroyFacesContext(context);
    	facesContext = null;
    }

    protected void wrapJsfRequest(InputData input) {
    	ServletUtil.setRequest(input, new AraneaJsfRequestWrapper(ServletUtil.getRequest(input), this));
    }
    
    protected HttpServletResponse wrapJsfResponse(OutputData output) {
    	this.response = ServletUtil.getResponse(output);
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
    	if (response != null)
    		ServletUtil.setResponse(output,response);
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
    	return getScope() + "-JSFSTATE";
    }
    
    public void closeFacesContext() {
      doNotRender = true;

      ServletUtil.getRequest(getInputData()).setAttribute("widget", this);

      facesContext = initFacesContext();
      getJSFContext().getLifecycle().execute(facesContext);

      restoreRequest(getInputData());
      restoreResponse(getOutputData(), response);

      destroyFacesContext();
    }
    
    protected FacesContext getCurrentFacesContext() {
    	return this.facesContext;
    }
    
    protected FlowContext getFlowCtx() {
      return (FlowContext) getEnvironment().requireEntry(FlowContext.class);
    }
}
