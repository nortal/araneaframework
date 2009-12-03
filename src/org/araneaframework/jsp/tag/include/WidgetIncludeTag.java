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

import java.io.PrintWriter;

import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspException;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.http.JspContext;
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
 *  description = "The JSP specified by the path given in <i>page</i> is included as the widget with id specified in <i>id</i>."
 */
public class WidgetIncludeTag extends BaseIncludeTag {

  protected String widgetId;

  protected String page;

  public WidgetIncludeTag() {
    this.widgetId = null;
    this.page = null;
  }

  @Override
  protected int doEndTag(Writer out) throws Exception {
    ApplicationWidget widget = JspWidgetUtil.traverseToSubWidget(getContextWidget(), this.widgetId);

    WidgetContextTag widgetContextTag = new WidgetContextTag();
    registerSubtag(widgetContextTag);
    widgetContextTag.setId(this.widgetId);
    executeStartSubtag(widgetContextTag);
    OutputData output = getOutputData();

    try {
      if (this.page == null) {
        hideGlobalContextEntries(this.pageContext);

        // Let's wrap the response so that the rendered widgets would write to the given stream.
        HttpServletResponse response = ServletUtil.getResponse(output);
        output.extend(HttpServletResponse.class, new WidgetResponseWrapper(response, out));

        // Render the specified widget:
        widget._getWidget().render(output);

        // Restore the previous HttpServletResponse instance, the wrapper is not needed anymore.
        output.extend(HttpServletResponse.class, response);
      } else {
        JspContext config = getEnvironment().requireEntry(JspContext.class);
        JspUtil.include(this.pageContext, config.getJspPath() + "/" + this.page);
      }
    } finally {
      restoreGlobalContextEntries(this.pageContext);
      executeEndTagAndUnregister(widgetContextTag);
    }
    super.doEndTag(out);
    return EVAL_PAGE;
  }

  /*
   * Tag attributes
   */

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Widget id."
   */
  public void setId(String widgetId) throws JspException {
    this.widgetId = evaluateNotNull("widgetId", widgetId, String.class);
  }

  /**
   * @jsp.attribute type = "java.lang.String" required = "false" description = "Path to JSP."
   */
  public void setPage(String page) {
    this.page = evaluate("page", page, String.class);
  }

  /**
   * A temporary wrapper to make widgets write to the given writer. This work-around makes <code>out.flush()</code>,
   * which may throw an exception, unnecessary
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 2.0
   */
  @SuppressWarnings("deprecation")
  private class WidgetResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream stream;
    private PrintWriter writer;

    public WidgetResponseWrapper(HttpServletResponse response, Writer out) {
      super(response);
      this.stream = new WidgetOutputStream(out);
      this.writer = new PrintWriter(out);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
      return this.stream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
      return this.writer;
    }

    /**
     * A temporary wrapper to make widgets write to the given writer. This work-around makes <code>out.flush()</code>,
     * which may throw an exception, unnecessary
     * 
     * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
     * @since 2.0
     */
    private class WidgetOutputStream extends ServletOutputStream {

      private Writer out;

      public WidgetOutputStream(Writer out) {
        this.out = out;
      }

      @Override
      public void write(int b) throws IOException { // IMPLEMENTED
        this.out.write(b);
      }

      @Override
      public void print(String str) throws IOException {
        this.out.write(str);
      }
      @Override
      public void print(boolean b) throws IOException {
        this.out.write(Boolean.toString(b));
      }
      @Override
      public void print(char c) throws IOException {
        this.out.write(c);
      }
      @Override
      public void print(int i) throws IOException {
        this.out.write(i);
      }
      @Override
      public void print(long l) throws IOException {
        this.out.write(Long.toString(l));
      }
      @Override
      public void print(float f) throws IOException {
        this.out.write(Float.toString(f));
      }
      @Override
      public void print(double d) throws IOException {
        this.out.write(Double.toString(d));
      }
      @Override
      public void println() throws IOException {
        this.out.write('\n');
      }
      @Override
      public void println(String str) throws IOException {
        this.out.write(str);
        println();
      }
      @Override
      public void println(boolean b) throws IOException {
        println(Boolean.toString(b));
      }
      @Override
      public void println(char c) throws IOException {
        println(Character.toString(c));
      }
      @Override
      public void println(int i) throws IOException {
        println(Integer.toString(i));
      }
      @Override
      public void println(long l) throws IOException {
        println(Long.toString(l));
      }
      @Override
      public void println(float l) throws IOException {
        println(Float.toString(l));
      }
      @Override
      public void println(double l) throws IOException {
        println(Double.toString(l));
      }
    }
  }
}
