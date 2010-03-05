/*
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
 */

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
 * A helper class for providing roll-back and commit functionality on an OutputData. If something has been written to the
 * OutputData, <code>commit()</code> will flush it, forcing any buffered output to be written out.
 * <code>rollback()</code> will discard the contents of the buffer.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 * TODO: rewrite as HttpOutputData wrapper
 */
public class AtomicResponseHelper {

  private AtomicResponseWrapper atomicWrapper;

  public AtomicResponseHelper(OutputData outputData) {
    Assert.isInstanceOfParam(HttpOutputData.class, outputData, "outputData");

    this.atomicWrapper = new AtomicResponseWrapper(ServletUtil.getResponse(outputData));
    ServletUtil.setResponse(outputData, this.atomicWrapper);
  }

  /**
   * Wraps a HttpServletResponse to make it possible of resetting and committing it.
   */
  @SuppressWarnings("deprecation")
  private static class AtomicResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream out;

    private PrintWriter writerOut;

    public AtomicResponseWrapper(HttpServletResponse response) {
      super(response);
      resetStream();
    }

    private void resetStream() {
      this.out = new AraneaServletOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
      if (this.out == null) {
        return getResponse().getOutputStream();
      }
      return this.out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
      if (this.out == null) {
        return getResponse().getWriter();
      }

      if (this.writerOut == null) {
        if (getResponse().getCharacterEncoding() != null) {
          this.writerOut = new PrintWriter(new OutputStreamWriter(this.out, getResponse().getCharacterEncoding()));
        } else {
          this.writerOut = new PrintWriter(new OutputStreamWriter(this.out));
        }
      }

      return this.writerOut;
    }

    /**
     * If the output has not been committed yet, commits it.
     * 
     * @throws AraneaRuntimeException if output has been committed already.
     */
    public void commit() throws IOException {
      if (this.out == null) {
        throw new IllegalStateException("Cannot commit buffer - response is already committed");
      }

      if (this.writerOut != null) {
        this.writerOut.flush();
      }
      this.out.flush();

      byte[] data = ((AraneaServletOutputStream) this.out).getData();

      getResponse().getOutputStream().write(data);
      getResponse().getOutputStream().flush();

      this.out = null;
    }

    /**
     * If the output has not been committed yet, clears the content of the underlying buffer in the response without
     * clearing headers or status code.
     */
    public void rollback() {
      if (this.out == null) {
        throw new IllegalStateException("Cannot reset buffer - response is already committed");
      }
     // getResponse().reset();
      resetStream();
      this.writerOut = null;
    }

    public byte[] getData() throws Exception {
      return ((AraneaServletOutputStream) this.out).getData();
    }
  }

  private static class AraneaServletOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream out;

    private AraneaServletOutputStream() {
      this.out = new ByteArrayOutputStream(20480);
    }

    @Override
    public void write(int b) {
      this.out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
      this.out.write(b);
    }

    @Override
    public void write(byte[] b, int offset, int len) {
      this.out.write(b, offset, len);
    }

    @Override
    public void flush() throws IOException {
      this.out.flush();
    }

    public byte[] getData() {
      return this.out.toByteArray();
    }
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  public void commit() throws Exception {
    this.atomicWrapper.commit();
  }

  public void rollback() throws Exception {
    this.atomicWrapper.rollback();
  }

  public byte[] getData() throws Exception {
    return this.atomicWrapper.getData();
  }
}
