package org.araneaframework.integration.jsf.core;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class AraneaJsfRequestWrapper extends HttpServletRequestWrapper {
	private static final Logger log = org.apache.log4j.Logger.getLogger(AraneaJsfRequestWrapper.class);
	
	private static final String ARANEA_JSF_REQUEST_WRAPPER = "org.araneaframework.integration.jsf.core.AraneaJsfRequestWrapper";

	private Map attributes;

    private String viewSelector;

    public AraneaJsfRequestWrapper(HttpServletRequest request, String viewSelector) {
        super(request);
        this.viewSelector = viewSelector;
        setAttribute(ARANEA_JSF_REQUEST_WRAPPER, this);
    }

    public String getPathInfo() {
        return null;
    }

    public String getServletPath() {
        return viewSelector;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return new AraneaJsfRequestDispatcherWrapper(getRequest().getRequestDispatcher(path));
        //return wrapped.getRequestDispatcher(path);
    }

	public void setAttribute(String name, Object value) {
		if (attributes == null)
			attributes = new HashMap();

		if (log.isDebugEnabled())
			log.debug("setAttribute(name='" + name + "', value=" + 
				value instanceof String ? "'" + value + "'" : ObjectUtils.identityToString(value) +");");

		if (value == null)
			removeAttribute(name);
		else
			attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		Object result = attributes != null ? attributes.get(name) : null;
		if (result == null)
			result = super.getAttribute(name);
		return result;
	}

	public Enumeration getAttributeNames() {
		final Enumeration warped = super.getAttributeNames();
		
		if (attributes == null || attributes.isEmpty())
			return warped;

		final Enumeration regular = Collections.enumeration(attributes.keySet());
		
		return new Enumeration() {
			public boolean hasMoreElements() {
				return (warped.hasMoreElements() || regular.hasMoreElements());
			}

			public Object nextElement() {
				if (warped.hasMoreElements())
					return warped.nextElement();
				return regular.nextElement();
			}
		};
	}

	public void removeAttribute(String name) {
		super.removeAttribute(name);
		if (attributes != null)
			attributes.remove(name);
	}
	
	public HttpServletRequest getOriginalRequest() {
		if (getRequest() instanceof AraneaJsfRequestWrapper)
			return ((AraneaJsfRequestWrapper)getRequest()).getOriginalRequest();
		return (HttpServletRequest)getRequest();
	}
}