package org.araneaframework.integration.struts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.regexp.RESyntaxException;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.ServletUtil;

public class StrutsWidget extends BaseApplicationWidget {  
  private boolean rerender = true;
  
  private String strutsURI;
  private byte[] renderResult;
  
  public StrutsWidget(String strutsURI) {
    this.strutsURI = strutsURI;
  }
  
  protected void init() throws Exception {
    super.init();
    
    addEventListener("include", new IncludeEventListener());
  }
  
  private byte[] renderInclude(InputData input, OutputData output)  throws Exception {
    HttpServletResponse res = ServletUtil.getResponse(getOutputData());
    HttpServletRequest req = ServletUtil.getRequest(input);
    
    StrutsResponse strutsResponse = new StrutsResponse(res);
    ServletUtil.setResponse(getOutputData(), strutsResponse);      
    
    StrutsRequest strutsRequest = new StrutsRequest(req);
    ServletUtil.setRequest(input, strutsRequest); 
    
    strutsRequest.setAttribute(AraneaStrutsFilter.ARANEA_INCLUDE, Boolean.TRUE);
   
    String strutsServletPath = 
      (String) strutsRequest.getAttribute(AraneaStrutsFilter.ORIGINAL_SERVLET_PATH);
    if (strutsServletPath != null)
      strutsURI = strutsServletPath;
    
    try {
      strutsRequest.getRequestDispatcher(strutsURI).include(strutsRequest, strutsResponse);
    }
    finally {
      ServletUtil.setResponse(getOutputData(), res);   
      ServletUtil.setRequest(input, req);
    }
    
    return StrutsPostProcesserUtil.postProcess(strutsResponse.getData(), input, output);
  }
  
  private class IncludeEventListener implements org.araneaframework.core.EventListener {
    public void processEvent(Object eventId, InputData input) throws Exception {
      rerender = true;
    }
  }

  protected void render(OutputData output) throws Exception {
    if (rerender) {
      renderResult = renderInclude(getInputData(), output);
      rerender = false;
    }
    
    if (renderResult != null)
      ((HttpOutputData) output).getOutputStream().write(renderResult);
    ((HttpOutputData) output).getOutputStream().flush();
  }
  
  private class StrutsResponse extends  HttpServletResponseWrapper {
    private ServletOutputStream out;
    private PrintWriter writerOut;        
    
    public StrutsResponse(HttpServletResponse arg0) throws UnsupportedEncodingException {
      super(arg0);
  
      out = new AraneaServletOutputStream();
      writerOut = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));   
    } 
    
    public ServletOutputStream getOutputStream() throws IOException {
      return out;
    }
    
    public PrintWriter getWriter() throws IOException {
      return writerOut;
    }    
    
    public String encodeURL(String arg0) {
      return super.encodeURL(encodeStrutsURL(arg0));
    }
   
    public String encodeRedirectURL(String arg0) {
      return super.encodeRedirectURL(encodeStrutsURL(arg0));
    }       
    
    public byte[] getData() throws Exception {
      if (writerOut != null)
        writerOut.flush();
      out.flush();
      
      return ((AraneaServletOutputStream) out).getData();
    }
  }
  
  private class StrutsRequest extends HttpServletRequestWrapper {
    public StrutsRequest(HttpServletRequest request) {
      super(request);
    }
    
    public String getPathInfo() {
      return (String) getAttribute(AraneaStrutsFilter.ORIGINAL_PATH_INFO);
    }
  }
  
  private String encodeStrutsURL(String baseURL) {
    Assert.notNull(baseURL);
    
    StringBuffer url = new StringBuffer(baseURL);
    
    TopServiceContext topCtx = 
      (TopServiceContext) getEnvironment().requireEntry(TopServiceContext.class);
    ThreadContext threadCtx = 
      (ThreadContext) getEnvironment().requireEntry(ThreadContext.class);
    
    Assert.isTrue(getOutputData() != null || getInputData() != null, "Either input- or output data should be not null");
    
    AraneaStrutsInfo asInfo = new AraneaStrutsInfo(
        (String) ((HttpInputData) getInputData()).getContainerPath(), 
        (String) topCtx.getCurrentId(), 
        (String) threadCtx.getCurrentId(), 
        "".equals(getOutputData().getScope().toString()) ? getInputData().getScope().toString() : getOutputData().getScope().toString());
    
    url.append(baseURL.contains("?") ? "&" : "?");
    
    url.append(asInfo.encode());
    return url.toString();
  }
  
  private static class AraneaServletOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream out;
    
    private AraneaServletOutputStream() {
      out = new ByteArrayOutputStream(20480);
    }
    
    public void write(int b) throws IOException {
      out.write(b);
    }
    public void write(byte[] b) throws IOException {
      out.write(b);
    }
    public void write(byte[] b, int offset, int len) throws IOException{
       out.write(b, offset, len);
    }
    public void flush() throws IOException{
      out.flush();
    }
        
    public byte[] getData() {
      return out.toByteArray();
    }
  }
}
