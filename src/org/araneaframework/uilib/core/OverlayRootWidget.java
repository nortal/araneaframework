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

package org.araneaframework.uilib.core;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.container.ExceptionHandlingFlowContainerWidget;
import org.araneaframework.http.util.ServletUtil;

/**
 * This class can be used to start an overlay flow. This is provided as-is standard implementation for overlay flow that
 * should meet the most common needs. Custom solutions may extend this class to provide additional functionality.
 * <p>
 * This class has 3 parameters:
 * <ul>
 * <li><code>childWidget</code> - the widget that will be rendered inside the flow;
 * <li><code>viewSelector</code> - the JSP page for flow renderer (default: "overlayRoot.jsp");
 * <li><code>errorPage</code> - the JSP page to render errors (default: "/WEB-INF/jsp/error.jsp").
 * <p>
 * These parameters are configurable through constructors and setters of this class.
 * <p>
 * Here's also an example on how to use this class:<br>
 * <code>getOverlayCtx().start(new OverlayRootWidget(new MyOverlayContentWidget()));</code>
 * <p>
 * Here's also a quick overview of "overlayRoot.jsp":
 * 
 * <pre>
 * &lt;ui:systemForm id=&quot;overlaySystemForm&quot; method=&quot;post&quot; styleClass=&quot;aranea-overlay-form&quot;&gt;
 *     &lt;ui:widgetInclude id=&quot;c&quot;/&gt;
 * &lt;/ui:systemForm&gt;
 * </pre>
 * 
 * Note that the <code>id</code> and <code>styleClass</code> attributes must have the values as in the example above
 * because Aranea scripts depend on them. Also the the widget to include must have exactly the same name as in the
 * example above.
 * <p>
 * Note on the error page: the JSP can make use of variables "viewData.rootStackTrace" and "viewData.fullStackTrace" to
 * display the error.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2
 */
public class OverlayRootWidget extends BaseUIWidget {

  /**
   * The default page to render this overlay root widget and its sub widgets.
   */
  public static String DEFAULT_ROOT_PAGE = "overlayRoot";

  /**
   * The default page to render when an uncaught exception occurs.
   */
  public static String DEFAULT_ERROR_PAGE = "/WEB-INF/jsp/error.jsp";

  /**
   * The sub widget to render. This widget is required.
   */
  protected Widget child;

  /**
   * The page to render when an uncaught exception occurs. Note that full path must be given. The simplest constructor
   * sets it to "/WEB-INF/jsp/error.jsp",
   */
  protected String errorPage;

  /**
   * Constructs a new root overlay widget with given subwidget to render. The sub widget is a required parameter.
   * <p>
   * This constructor also sets the view selector of the this root widget to "overlayRoot" and the error page to
   * "/WEB-INF/jsp/error.jsp". To override these values, use the other constructor or use the setters
   * {@link #setViewSelector(String)} and {@link #setErrorPage(String)}.
   * 
   * @param child The sub widget (required) to render.
   */
  public OverlayRootWidget(Widget child) {
    this(child, DEFAULT_ROOT_PAGE, DEFAULT_ERROR_PAGE);
  }

  /**
   * Constructs a new root overlay widget with given sub widget to render. The sub widget is a required parameter. It
   * also allows to specify custom view selector and error page for this root overlay widget. These values are also
   * required.
   * <p>
   * 
   * 
   * @param child The sub widget (required) to render.
   * @param viewSelector The page to render this widget and its sub widgets.
   * @param errorPage The page to render when an uncaught exception occurs. Note that full path must be given.
   */
  public OverlayRootWidget(Widget child, String viewSelector, String errorPage) {
    Assert.notNullParam(child, "child");
    this.child = child;
    this.viewSelector = viewSelector;
    this.errorPage = errorPage;
  }

  @Override
  protected void init() throws Exception {
    Assert.notNull(this.child, "The child component must be provided!");
    addWidget("c", new OverlayFlowContainer(this.child));
  }

  public void setErrorPage(String errorPage) {
    this.errorPage = errorPage;
  }

  /**
   * This flow container handles any errors that may occur. Without it, a static error page would be returned. However,
   * this flow container provides a way to use custom error page.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 1.1.4
   */
  protected class OverlayFlowContainer extends ExceptionHandlingFlowContainerWidget {

    protected Widget topWidget;
    
    public OverlayFlowContainer(Widget topWidget) {
      Assert.notNullParam(topWidget, "topWidget must not be null!");
      this.topWidget = topWidget;
    }

    @Override
    protected void update(InputData input) throws Exception {
      startTopWidget();
      super.update(input);
    }
    private void startTopWidget() {
      if (this.topWidget != null) {
        try {
          start(this.topWidget);
        } finally {
          this.topWidget = null;
        }
      }
    }

    @Override
    protected void renderExceptionHandler(OutputData output, Exception e) throws Exception {
      if (ExceptionUtils.getRootCause(e) != null) {
        putViewDataOnce("rootStackTrace", ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(e)));
      }
      putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e));
      ServletUtil.include(OverlayRootWidget.this.errorPage, this, output);
    }

  }

}
