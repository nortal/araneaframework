/*
 * JsfWidget.java
 *
 * Created on 20 November 2006, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jsf.test.base;

import java.io.IOException;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import jsf.test.core.JSFContext;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 *
 * @author taimo
 */
public class JsfWidget extends BaseUIWidget {
    String resolvedViewSelector;
    
    /** Creates a new instance of JsfWidget */
    public JsfWidget(String viewSelector) {
        resolvedViewSelector = viewSelector;
    }
    
    protected void init() throws Exception {
        Logger log = java.util.logging.Logger.getLogger("JsfWidget");
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
        
        // XXX: should give dummy outputdata?
        getJSFContext().initFacesContext(input, getOutputData());
        
        return getJSFContext().getFacesContext();
    }

    public JspContext getJspContext() {
        return (JspContext) getEnvironment().getEntry(JspContext.class);
    }
    
    public JSFContext getJSFContext() {
        return (JSFContext) getEnvironment().getEntry(JSFContext.class);
    }
    
    public class JsfRequestWrapper extends HttpServletRequestWrapper {
        private String viewSelector;
        private HttpServletRequest wrapped;
        
        public JsfRequestWrapper(HttpServletRequest request, String viewSelector) {
            super(request);
            this.wrapped = request;
            
            this.viewSelector = viewSelector;
        }

        public String getPathInfo() {
            return null;
        }

        public String getServletPath() {
            return viewSelector;
        }

        public RequestDispatcher getRequestDispatcher(String path) {
            return new AlwaysIncludingRequestDispatcherWrapper(wrapped.getRequestDispatcher(path));
            //return wrapped.getRequestDispatcher(path);
        }
        
    }
    
    public class AlwaysIncludingRequestDispatcherWrapper implements RequestDispatcher {
        private RequestDispatcher wrapped;
        
        public AlwaysIncludingRequestDispatcherWrapper(RequestDispatcher wrapped) {
            this.wrapped = wrapped;
        }
        
        public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
            wrapped.include(request, response);
        }

        public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
            wrapped.include(request, response);
        }
    }
    
    public class JsfResponseWrapper extends HttpServletResponseWrapper {
        public JsfResponseWrapper(HttpServletResponse response) {
            super(response);
        }
    }
}
