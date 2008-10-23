/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

package org.araneaframework.framework.container;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.AtomicResponseHelper;
import org.araneaframework.http.util.EnvironmentUtil;

/**
 * This flow container widget also handles any exceptions that may occur during
 * any child widget processing. Any sub-class will have to provide
 * implementation for the
 * <code>renderExceptionHandler(OutputData, Exception)</code> method, that
 * allows custom handling for given exception. To define the page that renders
 * the exception to the user, one can use following solution:
 * 
 * <pre><code>
 * ServletUtil.include(&quot;/WEB-INF/jsp/error.jsp&quot;, this, output);
 * </code></pre>
 * 
 * The page would be rendered as any other page inside this container.
 * <p>
 * Also note that this widget declares three events in its <code>init()</code>
 * method to let users handle the situation:
 * <ul>
 * <li>retry - retries to process the last event;</li>
 * <li>cancel - cancels the last event;</li>
 * <li>reset - resets the entire flow context of this container widget.</li>
 * </ul>
 * If you wish to use these events, make sure that when you override the
 * <code>init()</code> method, also include the <code>super.init();</code>
 * line to that method.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class ExceptionHandlingFlowContainerWidget
  extends StandardFlowContainerWidget {

  private static final Log log = LogFactory
      .getLog(ExceptionHandlingFlowContainerWidget.class);

  /**
   * The exception that occurs is stored in this variable.
   */
  protected Exception exception;

  /**
   * Initializes the flow container.
   */
  public ExceptionHandlingFlowContainerWidget() {
    super();
  }

  /**
   * Initializes the flow container, and specifies the <code>topWidget</code>
   * as its parent.
   * 
   * @param topWidget a parent widget.
   */
  public ExceptionHandlingFlowContainerWidget(Widget topWidget) {
    super(topWidget);
  }

  /**
   * Initializes the widget and declares three events to let users handle the
   * situation:
   * <ul>
   * <li>retry - retries to process the last event;</li>
   * <li>cancel - cancels the last event;</li>
   * <li>reset - resets the entire flow context of this container widget.</li>
   * </ul>
   * If you wish to use these events, and you override the <code>init()</code>
   * method, also include the <code>super.init();</code> line to that method.
   * 
   * @throws Exception Any non-specific exception that may occur.
   */
  protected void init() throws Exception {
    super.init();
    addEventListener("retry", new ProxyEventListener(this));
    addEventListener("cancel", new ProxyEventListener(this));
    addEventListener("reset", new ProxyEventListener(this));
  }

  /**
   * Handles the <code>retry</code> event.
   * 
   * @throws Exception Any non-specific exception that may occur.
   */
  public void handleEventRetry() throws Exception {
  // noop
  }

  /**
   * Handles the <code>cancel</code> event.
   * 
   * @throws Exception Any non-specific exception that may occur.
   */
  public void handleEventCancel() throws Exception {
    cancel();
  }

  /**
   * Handles the <code>reset</code> event.
   * 
   * @throws Exception Any non-specific exception that may occur.
   */
  public void handleEventReset() throws Exception {
    reset(null);
  }

  /**
   * A widget specific handling of an exception.
   */
  protected void handleWidgetException(Exception e) throws Exception {
    this.exception = e;
    if (ExceptionUtils.getRootCause(e) != null) {
      log.error("Critical exception occured: ", ExceptionUtils.getRootCause(e));
    } else {
      log.error("Critical exception occured: ", e);
    }
    UpdateRegionContext updateRegionContext = EnvironmentUtil
        .getUpdateRegionContext(getEnvironment());
    if (updateRegionContext != null) {
      updateRegionContext.disableOnce();
    }
  }

  /**
   * Overrides the <code>update()</code> functionality to catch and handle
   * exceptions.
   */
  protected void update(InputData input) throws Exception {
    if (this.exception == null) {
      super.update(input);
    } else {
      handleUpdate(input);
    }
  }

  /**
   * Overrides the <code>event()</code> functionality to catch and handle
   * exceptions.
   */
  protected void event(Path path, InputData input) throws Exception {
    if (this.exception == null) {
      super.event(path, input);
    } else if (path != null && !path.hasNext()) {
      handleEvent(input);
    }
  }

  /**
   * Overrides the <code>propagate()</code> functionality to catch and handle
   * exceptions.
   */
  protected void propagate(Message message) throws Exception {
    try {
      super.propagate(message);
    } catch (Exception e) {
      try {
        handleWidgetException(e);
      } catch (Exception e2) {
        ExceptionUtil.uncheckException(e2);
      }
    }
  }

  /**
   * Overrides the <code>render()</code> functionality to catch and handle
   * exceptions.
   */
  protected void render(OutputData output) throws Exception {
    AtomicResponseHelper arUtil = new AtomicResponseHelper(output);
    try {
      if (this.exception != null) {
        throw this.exception;
      }
      super.render(output);
    } catch (Exception e) {
      arUtil.rollback();
      log.error("Handling error:", e);
      renderExceptionHandler(output, e);
      this.exception = null;
    }
    arUtil.commit();
  }

  /**
   * This method lets sub-classes implement their way to handle and display this
   * exception. To define the page that renders the exception to the user, one
   * can use following solution:
   * 
   * <pre><code>
   * ServletUtil.include(&quot;/WEB-INF/jsp/error.jsp&quot;, this, output);
   * </code></pre>
   * 
   * @param output output data.
   * @param e the exception that occur.
   * @throws Exception Any non-specific exception that may occur.
   */
  protected abstract void renderExceptionHandler(OutputData output, Exception e)
      throws Exception;
}
