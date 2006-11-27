package org.araneaframework.integration.jsf.core;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfRequestDispatcherWrapper implements RequestDispatcher {
	private static final Logger log = Logger.getLogger(JsfRequestDispatcherWrapper.class);
    private RequestDispatcher wrapped;
    
    public JsfRequestDispatcherWrapper(RequestDispatcher wrapped) {
        this.wrapped = wrapped;
    }
    
    public void include(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        wrapped.include(request, response);
    }

    public void forward(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        wrapped.include(request, response);
    }
}
