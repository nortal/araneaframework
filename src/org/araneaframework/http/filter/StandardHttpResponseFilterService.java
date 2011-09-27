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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.exception.AraneaRuntimeException;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.ServletUtil;

/**
 * A filter which sets all the necessary headers of the response. One can set properties to customize sent headers or
 * cookies.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see #setCacheable(boolean)
 * @see #setCacheHoldingTime(long)
 * @see #setContentType(String)
 * @see #setCookies(Map)
 * @see #setHeaders(Map)
 */
public class StandardHttpResponseFilterService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(StandardHttpResponseFilterService.class);

  private String contentType = "text/html; charset=UTF-8";

  private boolean cacheable;

  private long cacheHoldingTime = 3600000;

  private Map<String, String> cookies = new HashMap<String, String>();

  private Map<String, String> headers = new HashMap<String, String>();

  /**
   * Sets if the response is cacheable or not. By default it is not cacheable.
   */
  public void setCacheable(boolean cacheable) {
    this.cacheable = cacheable;
  }

  /**
   * Sets the content type of the response. This is a required field of the response.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Constructs cookies from the key, value pair in the map of the cookies and sets them.
   * 
   * @param cookies
   */
  public void setCookies(Map<String, String> cookies) {
    this.cookies = cookies;
  }

  /**
   * Sets the headers of the response from the map headers. The key of the map is the name of the header and the value
   * is the corresponding value.
   */
  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  /**
   * Sets the cache-control's max-age parameter, value is in milliseconds.
   */
  public void setCacheHoldingTime(long cacheHoldingTime) {
    this.cacheHoldingTime = cacheHoldingTime;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);

    if (this.contentType == null) {
      throw new AraneaRuntimeException("Content type not set!");
    }

    response.setContentType(this.contentType);
    LOG.trace("response.characterEncoding: " + response.getCharacterEncoding());

    int idx = this.contentType.indexOf(';');
    if (idx > 0) {
      // the encoding is set, looking for the charset...
      idx = this.contentType.indexOf("charset=", idx + 1);

      if (idx > 0) {
        String encoding = this.contentType.substring(idx + 8);

        if (!encoding.equals(response.getCharacterEncoding())) {

          // the encoding didn't change, applying hack...
          ServletResponse r = ((ServletResponseWrapper) response).getResponse();

          // detecting Weblogic...
          Method method = null;
          try {
            method = r.getClass().getMethod("getDelegate", (Class<?>) null);
          } catch (NoSuchMethodException e) {}

          if (method != null) {
            if (LOG.isDebugEnabled()) {
              LOG.debug("Applying Weblogic hack to set the response character encoding...");
            }

            HttpServletResponse delegate = (HttpServletResponse) method.invoke(r, (Class<?>) null);
            if (delegate != null) {
              delegate.setContentType(this.contentType);
            }
          }

          if (LOG.isWarnEnabled() && !encoding.equals(response.getCharacterEncoding())) {
            LOG.warn("Unable to change the character encoding.");
          }
        }
      }
    }

    if (!this.cacheable) {
      response.setHeader("Pragma", "no-cache");
      response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
      response.setDateHeader("Expires", 1);
    } else {
      response.setHeader("Cache-Control", "private, max-age=" + this.cacheHoldingTime / 1000);
      response.setDateHeader("Expires", System.currentTimeMillis() + this.cacheHoldingTime);
    }

    for (Map.Entry<String, String> entry : this.headers.entrySet()) {
      response.setHeader(entry.getKey(), entry.getValue());
    }

    for (Map.Entry<String, String> entry : this.cookies.entrySet()) {
      response.addCookie(new Cookie(entry.getKey(), entry.getValue()));
    }

    getChildService()._getService().action(path, input, output);
  }
}
