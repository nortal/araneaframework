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

package org.araneaframework.jsp.tag.include;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspException;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.AtomicResponseHelper;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.tag.context.WidgetContextTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;

/**
 * Widget include tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *  name = "widgetInclude"
 *  body-content = "JSP"
 *  description = "The JSP specified by the path given in <i>page</i> is included as the widget with ID specified in <i>id</i>."
 */
public class WidgetIncludeTag extends BaseIncludeTag {

  protected String widgetId;

  protected String page;

  @Override
  protected int doStartTag(Writer out) throws Exception {
    ApplicationWidget widget = JspWidgetUtil.traverseToSubWidget(getContextWidget(), this.widgetId);

    WidgetContextTag widgetContextTag = registerSubtag(new WidgetContextTag());
    widgetContextTag.setId(this.widgetId);
    executeStartSubtag(widgetContextTag);

    OutputData output = getOutputData();

    try {
      if (this.page == null) {
        hideGlobalContextEntries(this.pageContext);

        // The provided writer might be a wrapper of the writer returned by ServletUtil.getResponse(output).getWriter().
        // We must use this writer (out) instead, because it may contain some necessary custom logic for rendering JSPs.

        // 1. Let's wrap the response so that the rendered widgets would write to the given stream (out).
        HttpServletResponse response = ServletUtil.getResponse(output);
        WidgetResponseWrapper responseWrapper = new WidgetResponseWrapper(response, out);

        output.extend(HttpServletResponse.class, responseWrapper);

        // 2. Render the specified widget:
        widget._getWidget().render(output);
        responseWrapper.flushBuffer();

        // 3. Restore the previous HttpServletResponse instance, the wrapper is not needed anymore.
        output.extend(HttpServletResponse.class, response);
      } else {
        JspContext config = getEnvironment().requireEntry(JspContext.class);
        JspUtil.include(this.pageContext, config.getJspPath() + "/" + this.page);
      }
    } finally {
      restoreGlobalContextEntries(this.pageContext);
      executeEndTagAndUnregister(widgetContextTag);
    }

    return super.doStartTag(out);
  }

  /*
   * Tag attributes
   */

  /**
   * @jsp.attribute type = "java.lang.String" required = "true" description = "Widget ID."
   */
  public void setId(String widgetId) throws JspException {
    this.widgetId = evaluateNotNull("widgetId", widgetId, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Path to JSP. When omitted, widget will be used to resolve the view."
   */
  public void setPage(String page) {
    this.page = evaluate("page", page, String.class);
  }

  /**
   * A temporary wrapper to make widgets write to the given JSP writer. To make use of it, the {@link OutputData} should
   * refer to this response wrapper.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  @SuppressWarnings("deprecation")
  private static class WidgetResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream stream;

    private PrintWriter writer;

    private String encoding;

    public WidgetResponseWrapper(HttpServletResponse response, Writer out) {
      super(response);
      this.encoding = response.getCharacterEncoding();
      this.stream = new ServletContentOutputStream(out, this.encoding);
      this.writer = new PrintWriter(out, true);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
      return this.stream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
      return this.writer;
    }

    @Override
    public String getCharacterEncoding() {
      return this.encoding;
    }

    @Override
    public void flushBuffer() throws IOException {
      this.stream.flush();
      super.flushBuffer();
    }

    /**
     * A temporary stream wrapper to make widgets write to the given JSP writer. Although no output stream should be
     * used when rendering JSPs, it is still provided because {@link AtomicResponseHelper} depends on it. This class
     * works together with {@link WidgetResponseWrapper} to use the same encoding for gathering data, but in the end the
     * JSP writer encoding will matter.
     * 
     * @author Martti Tamm (martti@araneaframework.org)
     * @since 2.0
     */
    private static class ServletContentOutputStream extends ServletOutputStream {

      private Writer out;

      private ByteArrayOutputStream bufferedOut = new ByteArrayOutputStream();

      private String encoding;

      public ServletContentOutputStream(Writer out, String encoding) {
        this.out = out;
        this.encoding = encoding;
      }

      @Override
      public void write(int b) throws IOException {
        this.bufferedOut.write(b);
      }

      @Override
      public void flush() throws IOException {
        if (this.encoding != null) {
          this.out.write(new String(this.bufferedOut.toByteArray(), this.encoding));
        } else {
          this.out.write(new String(this.bufferedOut.toByteArray()));
        }
        this.bufferedOut.reset();
      }
    }
  }
}
