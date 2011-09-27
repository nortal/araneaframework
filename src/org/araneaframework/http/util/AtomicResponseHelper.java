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
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.io.IOUtils;
import org.araneaframework.OutputData;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpOutputData;

/**
 * A helper class for providing roll-back and commit functionality on an OutputData. If something has been written to
 * the OutputData, <code>commit()</code> will flush it, forcing any buffered output to be written out.
 * <code>rollback()</code> will discard the contents of the buffer.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class AtomicResponseHelper {

  private final AtomicResponseWrapper atomicWrapper;

  private final OutputData output;

  public AtomicResponseHelper(OutputData outputData) {
    Assert.isInstanceOfParam(HttpOutputData.class, outputData, "outputData");

    this.output = outputData;
    this.atomicWrapper = new AtomicResponseWrapper(ServletUtil.getResponse(outputData));
    ServletUtil.setResponse(outputData, this.atomicWrapper);
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  public void commit() {
    this.atomicWrapper.commit();

    // This helper cannot be used anymore. Let's restore the previous one:
    ServletUtil.setResponse(this.output, (HttpServletResponse) this.atomicWrapper.getResponse());
  }

  public void rollback() {
    this.atomicWrapper.rollback();
  }

  /**
   * Returns the data that is collected by this output stream at this moment.
   * 
   * @return The collected data in raw format.
   */
  public byte[] getData() {
    return this.atomicWrapper.getData();
  }

  /**
   * Returns the data as string that is collected by this output stream at this moment. The encoding will be taken from
   * {@link ServletResponse#getCharacterEncoding()}. When the latter is <code>null</code>, this method will fail with an
   * exception.
   * 
   * @return The collected data as <code>String</code> that is encoded as {@link ServletResponse#getCharacterEncoding()}
   *         .
   * @since 2.0
   */
  public String getStringData() {
    return this.atomicWrapper.getStringData();
  }

  /**
   * Wraps a HttpServletResponse to make it possible of resetting and committing it.
   */
  @SuppressWarnings("deprecation")
  private static class AtomicResponseWrapper extends HttpServletResponseWrapper {

    private AraneaServletOutputStream out;

    private PrintWriter outWriter;

    public AtomicResponseWrapper(HttpServletResponse response) {
      super(response);

      try {
        // Important: we retrieve the output stream now, because there may be issues when retrieving it later,
        // especially when rendering JSPs because the standard expects that getOutputStream() is not called after
        // getWriter(). To simplify handling the writer and output stream, we instantiate both here, so that the writer
        // would automatically write to our stream, and that our stream would write to the bound servlet output stream,
        // when commit is ordered.

        this.out = new AraneaServletOutputStream(response.getOutputStream());
      } catch (IOException e) {
        ExceptionUtil.uncheckException("Unexpected exception when retrieving servlet response output stream.", e);
      }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
      return this.out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
      if (this.outWriter == null) {
        if (getResponse().getCharacterEncoding() != null) {
          this.outWriter = new PrintWriter(new OutputStreamWriter(getOut(), getResponse().getCharacterEncoding()));
        } else {
          this.outWriter = new PrintWriter(new OutputStreamWriter(getOut()));
        }
      }
      return this.outWriter;
    }

    /**
     * This method is used internally. Usually when we work with the <code>out</code> stream, we want its data to be
     * up-to-date. Therefore, we flush the writer each time.
     * 
     * @return The current output stream.
     */
    protected AraneaServletOutputStream getOut() {
      if (this.out == null) {
        throw new IllegalStateException("Cannot retrieve output stream - response is already committed.");
      }

      if (this.outWriter != null) {
        this.outWriter.flush();
      }

      return this.out;
    }

    /**
     * If the output has not been committed yet, commits it.
     * 
     * @throws AraneaRuntimeException if output has been committed already.
     */
    public void commit() {
      getOut().commit();

      // Once committed, this helper cannot be used anymore. This is intentional.
      this.outWriter = null;
      this.out = null;
    }

    /**
     * If the output has not been committed yet, clears the content of the underlying buffer in the response without
     * clearing headers or status code.
     */
    public void rollback() {
      getOut().rollback();
    }

    /**
     * Returns the data that is collected by this output stream at this moment.
     * 
     * @return The collected data in raw format.
     */
    public byte[] getData() {
      return getOut().getData();
    }

    /**
     * Returns the data as string that is collected by this output stream at this moment. The encoding will be taken
     * from {@link ServletResponse#getCharacterEncoding()}. When the latter is <code>null</code>, this method will fail
     * with an exception.
     * 
     * @return The collected data as <code>String</code> that is encoded as
     *         {@link ServletResponse#getCharacterEncoding()}.
     * @since 2.0
     */
    public String getStringData() {
      try {
        return new String(getData(), getResponse().getCharacterEncoding());
      } catch (UnsupportedEncodingException e) {
        ExceptionUtil.uncheckException("Problem while converting bytes into string. "
            + "Make sure that response character encoding is correct!", e);
        return null; // Not reached.
      }
    }

    @Override
    public String toString() {
      return "AtomicResponseWrapper for response object:\n" + getResponse();
    }
  }

  private static class AraneaServletOutputStream extends ServletOutputStream {

    private ByteArrayOutputStream bufferedOut = new ByteArrayOutputStream(20480);

    private ServletOutputStream targetOut;

    private AraneaServletOutputStream(ServletOutputStream targetOut) {
      this.targetOut = targetOut;
    }

    @Override
    public void write(int b) {
      this.bufferedOut.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
      this.bufferedOut.write(b);
    }

    @Override
    public void write(byte[] b, int offset, int len) {
      this.bufferedOut.write(b, offset, len);
    }

    @Override
    public void flush() throws IOException {
      this.bufferedOut.flush();
    }

    public void rollback() {
      this.bufferedOut.reset();
    }

    public void commit() {
      try {
        this.bufferedOut.writeTo(this.targetOut);
        // This is a bit of hack, but it let's sub-implementations know that we have completed writing data for now:
        this.targetOut.flush();
      } catch (IOException e) {
        ExceptionUtil.uncheckException("Unexpected exception when flushing data to the target output stream.", e);
      } finally {
        IOUtils.closeQuietly(this.bufferedOut);
        this.bufferedOut = null;
        this.targetOut = null;
      }
    }

    public byte[] getData() {
      try {
        this.bufferedOut.flush();
        return this.bufferedOut.toByteArray();
      } catch (IOException e) {
        ExceptionUtil.uncheckException("Unexpected exception when flushing data.", e);
      }
      return null;
    }
  }
}
