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

	private Map attributes;

    private String viewSelector;

    public AraneaJsfRequestWrapper(HttpServletRequest request, String viewSelector) {
        super(request);
        this.viewSelector = viewSelector;
    }

    public String getPathInfo() {
        return null;
    }

    public String getServletPath() {
        return viewSelector;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return new JsfRequestDispatcherWrapper(getRequest().getRequestDispatcher(path));
        //return wrapped.getRequestDispatcher(path);
    }

	public void setAttribute(String name, Object value) {
		if (attributes == null)
			attributes = new HashMap();

		log.debug("setAttribute(name='" + name + "', value=" + 
				value instanceof String ? "'" + value + "'" : ObjectUtils.identityToString(value) +");");

		if (value == null)
			removeAttribute(name);
		else
			attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		Object result = super.getAttribute(name);
		if (result == null)
			result = attributes != null ? attributes.get(name) : null;
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