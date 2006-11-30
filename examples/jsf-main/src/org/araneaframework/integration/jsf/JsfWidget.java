package org.araneaframework.integration.jsf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.JSFContext;
import org.araneaframework.integration.jsf.core.JsfRequestWrapper;
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
        FacesContext facesContext = initFacesContext();

        getJSFContext().getLifecycle().execute(facesContext);
        getJSFContext().getLifecycle().render(facesContext);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ResponseStream stream = createResponseStream(baos);
        //ResponseWriter writer = 
        
        facesContext.setResponseStream(stream);
        //facesContext.setResponseWriter(responseWriter);

        getJSFContext().destroyFacesContext(facesContext);
    }
    
    protected FacesContext initFacesContext() {
        InputData input = getInputData();
        OutputData output = getOutputData();
        HttpServletRequest request = ServletUtil.getRequest(input);
        HttpServletResponse response = ServletUtil.getResponse(output);
        
        // wrap the request and response
        ServletUtil.setRequest(input, new JsfRequestWrapper(request, resolvedViewSelector));
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
    	return resolvedViewSelector;
    }
    
//    public ResponseWriter createResponseWriter(final OutputStream out, String charEncoding) throws UnsupportedEncodingException {
//    	final PrintWriter writer = null;
//    	if (charEncoding != null) {
//    		writer = new PrintWriter(new OutputStreamWriter(out, charEncoding));
//    	} else {
//    		writer = new PrintWriter(new OutputStreamWriter(out));
//    	}
//
//    	return new ResponseWriterWrapper() {
//			protected ResponseWriter getWrapped() {
//				return writer;
//			}
//    	};
    		
////    	//		if (getResponse().getCharacterEncoding() != null) 
//			result = new PrintWriter(new OutputStreamWriter(out, getResponse().getCharacterEncoding()));
//		else 
//			result = new PrintWriter(new OutputStreamWriter(out));}
//    }
    
    public ResponseStream createResponseStream(OutputStream out) {
        final OutputStream output = out;
        return new ResponseStream() {
            public void write(int b) throws IOException {
            	log.debug("writing b");
                output.write(b);
            }


            public void write(byte b[]) throws IOException {
            	log.debug("writing b[]");
                output.write(b);
            }


            public void write(byte b[], int off, int len) throws IOException {
            	log.debug("writing b[], int off, int len");
                output.write(b, off, len);
            }


            public void flush() throws IOException {
            	log.debug("flushing");
                output.flush();
            }


            public void close() throws IOException {
            	log.debug("closing");
                output.close();
            }
        };
    }
}
