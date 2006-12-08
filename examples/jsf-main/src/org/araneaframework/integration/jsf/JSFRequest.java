package org.araneaframework.integration.jsf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class JSFRequest extends HttpServletRequestWrapper {
	public JSFRequest(HttpServletRequest request) {
		super(request);
	}
}