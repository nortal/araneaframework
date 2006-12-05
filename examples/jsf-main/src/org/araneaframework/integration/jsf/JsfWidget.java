package org.araneaframework.integration.jsf;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.JSFContext;
import org.araneaframework.integration.jsf.core.JsfRequestWrapper;

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
    }
    
    protected FacesContext initFacesContext() {
        InputData input = getInputData();
        OutputData output = getOutputData();
        HttpServletRequest request = ServletUtil.getRequest(input);
        //HttpServletResponse response = ServletUtil.getResponse(output);
        
        // wrap the request
        ServletUtil.setRequest(input, new JsfRequestWrapper(request, viewSelector));
        //ServletUtil.setResponse(output,new JsfResponseWrapper(response));
        
        // XXX: should give dummy outputdata
        return getJSFContext().initFacesContext(input, output);
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
