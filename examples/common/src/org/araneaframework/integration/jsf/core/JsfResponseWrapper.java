package org.araneaframework.integration.jsf.core;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.log4j.Logger;
import org.araneaframework.http.support.ByteArrayServletOutputStream;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfResponseWrapper extends HttpServletResponseWrapper {
	private static final Logger log = Logger.getLogger(JsfResponseWrapper.class);
	
    private ServletOutputStream out;
    private PrintWriter writerOut;
	
    public JsfResponseWrapper(HttpServletResponse response) {
        super(response);

        out = new ByteArrayServletOutputStream(10*1024);
    }
    
	public void flushBuffer() throws IOException {
		super.flushBuffer();
	}

	public int getBufferSize() {
		return ((ByteArrayServletOutputStream)out).size();
	}

	public ServletOutputStream getOutputStream() throws IOException {
		if (out == null)
			return getResponse().getOutputStream();

		return out;
	}

	public PrintWriter getWriter() throws IOException {
		if (out == null)
			return getResponse().getWriter();

		if (writerOut == null) {
			writerOut = constructWriter();
		}

		return writerOut;
	}

	// XXX: just disallow?? by spec this should reset headers too 
	public void reset() {
		throw new IllegalStateException(getClass().getName() + ".reset() called.");
	}

	public void resetBuffer() {
		out = new ByteArrayServletOutputStream();
		writerOut = null;
	}

	public byte[] getData() throws Exception {
		return ((ByteArrayServletOutputStream) out).getData();
	}

	public HttpServletResponse getOriginalResponse() {
		if (getResponse() instanceof JsfResponseWrapper)
			return ((JsfResponseWrapper)getResponse()).getOriginalResponse();
		return (HttpServletResponse)getResponse();
	}
	
	protected PrintWriter constructWriter() throws UnsupportedEncodingException {
		PrintWriter result = null;
		if (getResponse().getCharacterEncoding() != null) 
			result = new PrintWriter(new OutputStreamWriter(out, getResponse().getCharacterEncoding()));
		else 
			result = new PrintWriter(new OutputStreamWriter(out));

		return result;
	}
}