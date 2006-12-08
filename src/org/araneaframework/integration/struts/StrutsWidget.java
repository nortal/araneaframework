package org.araneaframework.integration.struts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.ServletUtil;

public class StrutsWidget extends BaseApplicationWidget {  
  private static final Logger log = Logger.getLogger(StrutsWidget.class);
  
  public static final String STRUTS_WIDGET_KEY = "org.araneaframework.integration.struts.StrutsWidget"; 
  
  private String strutsURI;
  private List renderers = new ArrayList();
  
  private transient StrutsResponse strutsResponse; 
  
  public StrutsWidget(String strutsURI) {
    this.strutsURI = strutsURI;
  }
  
  protected void init() throws Exception {
    super.init();
    
    addEventListener("include", new IncludeEventListener());
        
    renderPhaseOne(getInputData(), getOutputData());
  }
  
  private class IncludeEventListener implements org.araneaframework.core.EventListener {
    public void processEvent(Object eventId, InputData input) throws Exception {
      renderPhaseOne(input, getOutputData());
    }
  }
  
  
  
  
  
  
  
  
  
  
  
  private void renderPhaseOne(InputData input, OutputData output)  throws Exception {    
    renderers.clear();
    
    Path is = getInputData().getScope();
    Path os = getOutputData().getScope();
    
    input.restoreScope(getScope().toPath());
    output.restoreScope(getScope().toPath());
    
    try {    
      HttpServletResponse res = ServletUtil.getResponse(output);
      HttpServletRequest req = ServletUtil.getRequest(input);

      strutsResponse = new StrutsResponse(res);
      ServletUtil.setResponse(output, strutsResponse);      

      StrutsRequest strutsRequest = new StrutsRequest(req);
      ServletUtil.setRequest(input, strutsRequest); 

      try {
        strutsRequest.setAttribute(AraneaStrutsFilter.ARANEA_INCLUDE, Boolean.TRUE);

        String strutsServletPath = 
          (String) strutsRequest.getAttribute(AraneaStrutsFilter.ORIGINAL_SERVLET_PATH);
        if (strutsServletPath != null)
          strutsURI = strutsServletPath;

        output.pushAttribute(STRUTS_WIDGET_KEY, this);

        strutsRequest.getRequestDispatcher(strutsURI).include(strutsRequest, strutsResponse);     

        byte[] bytes = strutsResponse.getData();      
        renderers.add(new ByteRenderer(bytes));     
      }
      finally {
        output.popAttribute(STRUTS_WIDGET_KEY);

        ServletUtil.setResponse(getOutputData(), res);   
        ServletUtil.setRequest(input, req);

        strutsResponse = null;
      }
    }
    finally {
      output.restoreScope(os);
      input.restoreScope(is);
    }
  }
  
  private byte[] renderPhaseTwo(InputData input, OutputData output)  throws Exception {
    HttpServletResponse res = ServletUtil.getResponse(output);
    HttpServletRequest req = ServletUtil.getRequest(input);
    
    StrutsResponse strutsResponse = new StrutsResponse(res);
    ServletUtil.setResponse(output, strutsResponse);      
    
    StrutsRequest strutsRequest = new StrutsRequest(req);
    ServletUtil.setRequest(input, strutsRequest); 
    
    try {
      strutsRequest.setAttribute(AraneaStrutsFilter.ARANEA_INCLUDE, Boolean.TRUE);
         
      String strutsServletPath = 
        (String) strutsRequest.getAttribute(AraneaStrutsFilter.ORIGINAL_SERVLET_PATH);
      if (strutsServletPath != null)
        strutsURI = strutsServletPath;
      
      output.pushAttribute(STRUTS_WIDGET_KEY, this);

      for (Iterator i = renderers.iterator(); i.hasNext();) {
        Renderer renderer = (Renderer) i.next();
        renderer.render(output); 
      }
      
      return StrutsPostProcesserUtil.postProcess(strutsResponse.getData(), input, output);      
    }
    finally {
      output.popAttribute(STRUTS_WIDGET_KEY);
      
      ServletUtil.setResponse(getOutputData(), res);   
      ServletUtil.setRequest(input, req);
    }
  }
  
  public void renderWidget(String widgetId, OutputData output) throws Exception {
    byte[] bytes = strutsResponse.getData();
    
    renderers.add(new ByteRenderer(bytes));
    renderers.add(new WidgetRenderer(widgetId));    
  }

  protected void render(OutputData output) throws Exception {    
    byte[] renderResult = renderPhaseTwo(getInputData(), output);
    ((HttpOutputData) output).getOutputStream().write(renderResult);
    ((HttpOutputData) output).getOutputStream().flush();
        
  }
  
  
  
  
  
  
  
  
  
  
  
  
  private interface Renderer extends Serializable {
    void render(OutputData output) throws Exception;
  }
  
  private class ByteRenderer implements Renderer {
    private byte[] bytes;
    
    public ByteRenderer(byte[] bytes) {
      this.bytes = bytes;
    }

    public void render(OutputData output) throws Exception {
      ((HttpOutputData) output).getOutputStream().write(bytes);
      ((HttpOutputData) output).getOutputStream().flush();
    }
  }
  
  private class WidgetRenderer implements Renderer {
    private String widgetId;
            
    public WidgetRenderer(String widgetId) {
      this.widgetId = widgetId;
    }

    public void render(OutputData output) throws Exception {
      if (StrutsWidget.this.getWidget(widgetId) == null) {
        log.warn("Widget '" + widgetId + "' was not found under Struts widget '" + output.getScope() + "'.");
        return;
      }
      
      output.pushScope(widgetId);
      
      try {                
        StrutsWidget.this.getWidget(widgetId)._getWidget().render(output);                
        ((HttpOutputData) output).getWriter().flush();
      }
      finally {
        output.popScope();
      }
    }
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
      
      byte[] result = ((AraneaServletOutputStream) out).getData();
      
      ((AraneaServletOutputStream) out).reset();
      
      return result;
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
      reset();
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
    
    public void reset() {
      out = new ByteArrayOutputStream(20480);
    }
        
    public byte[] getData() {
      return out.toByteArray();
    }
  }
}
