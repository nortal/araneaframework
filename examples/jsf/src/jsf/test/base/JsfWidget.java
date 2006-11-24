/*
 * JsfWidget.java
 *
 * Created on 20 November 2006, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jsf.test.base;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import jsf.test.core.JSFContext;
import jsf.test.core.JsfRequestWrapper;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfWidget extends BaseUIWidget {
	private static final Logger log = org.apache.log4j.Logger.getLogger(JsfWidget.class);
	
    String resolvedViewSelector;
    
    /** Creates a new instance of JsfWidget */
    public JsfWidget(String viewSelector) {
        resolvedViewSelector = viewSelector;
    }
    
    protected void init() throws Exception {
        resolvedViewSelector = resolveJspName(getJspContext(), resolvedViewSelector);
        log.info("resolved view " + resolvedViewSelector);
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
        FacesContext facesContext = getJSFContext().getFacesContext();
        if (facesContext == null)
            facesContext = initFacesContext();

        getJSFContext().getLifecycle().execute(facesContext);
        getJSFContext().getLifecycle().render(facesContext);

        getJSFContext().releaseFacesContext();
    }
    
    protected FacesContext initFacesContext() {
        InputData input = getInputData();
        HttpServletRequest request = ServletUtil.getRequest(input);
        
        //decorate the request
        ServletUtil.setRequest(input, new JsfRequestWrapper(request, resolvedViewSelector));
        
        // XXX: should give dummy outputdata
        getJSFContext().initFacesContext(input, getOutputData());
        
        return getJSFContext().getFacesContext();
    }

    public JspContext getJspContext() {
        return (JspContext) getEnvironment().getEntry(JspContext.class);
    }
    
    public JSFContext getJSFContext() {
        return (JSFContext) getEnvironment().getEntry(JSFContext.class);
    }
}
