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

package org.araneaframework.http.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.lang.math.IntRange;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.util.ServletUtil;

/**
 * Provides better Aranea support for handling redirects that are called by sub-widgets of this filter. The idea is that
 * this filter wraps the response object and when a sub-widget invokes redirect, the response wrapper remembers it.
 * Using that information, this filter knows whether sub-widgets need to be rendered (redirected=false) or not
 * (redirected=true).
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardRedirectingFilterWidget extends BaseFilterWidget {

  @Override
  protected void update(InputData input) throws Exception {
    ResponseWrapper wrapper = new ResponseWrapper(ServletUtil.getResponse(getOutputData()));
    ServletUtil.setResponse(getOutputData(), wrapper);
    getOutputData().extend(ResponseWrapper.class, wrapper);
    super.update(input);
  }

  @Override
  protected void render(OutputData output) throws Exception {
    try {
      ResponseWrapper wrapper = output.narrow(ResponseWrapper.class);
      if (!wrapper.canRender()) {
        return;
      }
    } catch (NoSuchNarrowableException e) {
      // Fine
    }

    super.render(output);
  }

  @SuppressWarnings("deprecation")
  public static class ResponseWrapper extends HttpServletResponseWrapper {

    private int status;

    public ResponseWrapper(HttpServletResponse response) {
      super(response);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
      this.status = SC_MOVED_TEMPORARILY;
      super.sendRedirect(location);
    }

    @Override
    public void setStatus(int sc) {
      this.status = sc;
      super.setStatus(sc);
    }

    /**
     * Sets the status of the response. The method is deprecated in JEE 5.0.
     * 
     * @deprecated To set a status code use {@link #setStatus(int)}, to send an error with a description use
     *             {@link #sendError(int, String)} . Sets the status code and message for this response.
     */
    @Override
    @Deprecated
    public void setStatus(int sc, String sm) {
      this.status = sc;
      super.setStatus(sc, sm);
    }

    @Override
    public void sendError(int sc) throws IOException {
      this.status = sc;
      super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
      this.status = sc;
      super.sendError(sc, msg);
    }

    public boolean canRender() {
      int s = this.status;
      return !new IntRange(300, 399).containsInteger(s) && !new IntRange(500, 599).containsInteger(s);
    }
  }
}
