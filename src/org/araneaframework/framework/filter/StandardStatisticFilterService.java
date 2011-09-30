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

package org.araneaframework.framework.filter;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.ServletUtil;

/**
 * A filter service that logs with INFO-level the time it takes for the child service to complete its action method
 * (serving the request). The logging statement can have a message set via <code>setMessage()</code>.
 * <p>
 * Since version 2.0, this service also supports expression in the set message that will be replaced with data from
 * request. Available expressions are stored as constants in this class. The message should contain at least the
 * {@link #EXPR_TIME} expression to log the elapsed time.
 * 
 * @author Toomas Römer (toomas@araneaframework.org)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class StandardStatisticFilterService extends BaseFilterService {

  private static final Log LOG = LogFactory.getLog(StandardStatisticFilterService.class);

  /**
   * A place-holder in message that, when met, will be replaced with {@link HttpServletRequest#getMethod()}.
   * 
   * @since 2.0
   */
  public static final String EXPR_METHOD = "${METHOD}";

  /**
   * A place-holder in message that, when met, will be replaced with {@link HttpServletRequest#getContextPath()}.
   * 
   * @since 2.0
   */
  public static final String EXPR_CONTEXT = "${CONTEXT}";

  /**
   * A place-holder in message that, when met, will be replaced with {@link HttpServletRequest#getServletPath()}.
   * 
   * @since 2.0
   */
  public static final String EXPR_SERVLET = "${SERVLET}";

  /**
   * A place-holder in message that, when met, will be replaced with {@link HttpServletRequest#getPathTranslated()}.
   * 
   * @since 2.0
   */
  public static final String EXPR_PATH = "${PATH_INFO}";

  /**
   * A place-holder in message that, when met, will be replaced with {@link HttpServletRequest#getQueryString()}. When
   * the query string is not empty, a question-mark will be prepended to query string.
   * 
   * @since 2.0
   */
  public static final String EXPR_QUERY = "${QUERY}";

  /**
   * A place-holder in message that, when met, will be replaced with the request processing time in milliseconds.
   * 
   * @since 2.0
   */
  public static final String EXPR_TIME = "${TIME}";

  private String message;

  private int threshold = -1;

  /**
   * Sets the log message that will be displayed when logging the request duration. The message may contain expressions
   * that are defined as constants of this class.
   * 
   * @param message The message to render about the request duration.
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Sets the threshold for duration so that only longer durations would be logged. Default threshold is -1 meaning that
   * all requests will be logged.
   * 
   * @param threshold The duration threshold so that only longer requests will be logged.
   */
  public void setThreshold(int threshold) {
    this.threshold = threshold;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    long start = System.currentTimeMillis();
    getChildService()._getService().action(path, input, output);
    long duration = System.currentTimeMillis() - start;

    if (this.message != null && this.threshold < duration && LOG.isInfoEnabled()) {
      LOG.info(evaluateMessage(this.message, ServletUtil.getRequest(input), duration));
    }
  }

  /**
   * Evaluates the message and replaces expressions with data from request. This method may throw a
   * {@link NullPointerException} when either <code>msg</code> or <code>request</code> is <code>null</code>.
   * 
   * @param msg The original message that should contain expressions (technically does not have to).
   * @param request The request object that is used to replace expressions with data.
   * @param time The time in milliseconds to replace the {@link #EXPR_TIME} expression.
   * @return The evaluated message.
   */
  private static String evaluateMessage(String msg, HttpServletRequest request, long time) {
    if (msg.contains(EXPR_METHOD)) {
      msg = msg.replace(EXPR_METHOD, request.getMethod());
    }
    if (msg.contains(EXPR_CONTEXT)) {
      msg = msg.replace(EXPR_CONTEXT, request.getContextPath());
    }
    if (msg.contains(EXPR_SERVLET)) {
      msg = msg.replace(EXPR_SERVLET, request.getServletPath());
    }
    if (msg.contains(EXPR_PATH)) {
      msg = msg.replace(EXPR_PATH, StringUtils.defaultString(request.getPathInfo()));
    }
    if (msg.contains(EXPR_QUERY)) {
      msg = msg.replace(EXPR_QUERY, request.getQueryString() == null ? "" : "?" + request.getQueryString());
    }
    if (msg.contains(EXPR_TIME)) {
      msg = msg.replace(EXPR_TIME, Long.toString(time));
    }
    return msg;
  }
}
