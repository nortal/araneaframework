/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package org.araneaframework.http.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.araneaframework.OutputData;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.http.HttpOutputData;

/**
 * A helper class for providing rollback and commit functionality on an OutputData.
 * If something has been written to the OutputData, <code>commit()</code> will flush
 * it, forcing any buffered output to be written out. <code>rollback()</code> will
 * discard the contents of the buffer.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 * TODO: rewrite as HttpOutputData wrapper
 */
public class AtomicResponseHelper {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private ResponseWrapper atomicWrapper;
  
  public AtomicResponseHelper(OutputData outputData) {    
    Assert.isInstanceOfParam(HttpOutputData.class, outputData, "outputData");
    
    atomicWrapper = new ResponseWrapper(ServletUtil.getResponse(outputData));
    ServletUtil.setResponse(outputData, atomicWrapper);
  }
  
  
  //*******************************************************************
  // PRIVATE CLASSES
  //*******************************************************************
  /**
   * Wraps a HttpServletResponse to make it possible of resetting and commiting it.
   */
  private static class ResponseWrapper extends HttpServletResponseWrapper {
    private ServletOutputStream out;
    private PrintWriter writerOut;

    public ResponseWrapper(HttpServletResponse arg0) {
      super(arg0);
  
      resetStream();
    }
    

    private void resetStream() {
      out = new AraneaServletOutputStream();
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
        if (getResponse().getCharacterEncoding() != null) { 
          writerOut = new PrintWriter(new OutputStreamWriter(out, getResponse().getCharacterEncoding()));
        }
        else {
          writerOut = new PrintWriter(new OutputStreamWriter(out));
        } 
      }
      
      return writerOut;
    }
    
    /**
     * If the output has not been commited yet, commits it.
     * @throws AraneaRuntimeException if output has been commited already. 
     */
    public void commit() throws IOException {
      if (out == null)
        throw new IllegalStateException("Cannot commit buffer - response is already committed");
      
      if (writerOut != null)
        writerOut.flush();
      out.flush();
      
      byte[] data = ((AraneaServletOutputStream) out).getData();
      
      getResponse().getOutputStream().write(data);
      getResponse().getOutputStream().flush();
      
      out = null;
    }
    
    /**
     * If the output has not been commited yet, clears the content of the underlying
     * buffer in the response without clearing headers or status code.
     */
    public void rollback() {
      if (out == null)
        throw new IllegalStateException("Cannot reset buffer - response is already committed");
      
      resetStream();
      writerOut = null;
    }    
    
    public byte[] getData() throws Exception {
  	  return ((AraneaServletOutputStream) out).getData();
    }
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
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************  
  
  public void commit() throws Exception {
    atomicWrapper.commit();
  }
  
  public void rollback() throws Exception {
    atomicWrapper.rollback();
  }
  
  public byte[] getData() throws Exception {
	return atomicWrapper.getData();
  }
}
