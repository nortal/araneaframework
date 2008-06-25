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

package org.araneaframework.example.main.web;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.container.ExceptionHandlingFlowContainerWidget;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * This class is the root widget for overlay mode. Note that this class is
 * necessary, unless one wants the widget passed in the OverlayContext.start(widget)
 * method to handle the entire page.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class OverlayRootWidget extends BaseUIWidget {

  private static final long serialVersionUID = 1L;

  private Widget child;

  public OverlayRootWidget(Widget child) {
    this.child = child;
  }

  protected void init() throws Exception {
    Assert.notNull(child);
    addWidget("c", new OverlayFlowContainer(child));
    setViewSelector("overlayRoot");
  }

  /**
   * This flow container handles any errors that may occur. Without it, a static
   * error page would be returned. However, this flow container provides a
   * custom error page.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 1.1.4
   */
  private class OverlayFlowContainer extends ExceptionHandlingFlowContainerWidget {

    private static final long serialVersionUID = 1L;

    public OverlayFlowContainer(Widget topWidget) {
        super(topWidget);
    }

    protected void renderExceptionHandler(OutputData output, Exception e)
        throws Exception {

      if (ExceptionUtils.getRootCause(e) != null) {
        putViewDataOnce("rootStackTrace", ExceptionUtils.getFullStackTrace(
            ExceptionUtils.getRootCause(e)));
      }        
      putViewDataOnce("fullStackTrace", ExceptionUtils.getFullStackTrace(e)); 
      
      ServletUtil.include("/WEB-INF/jsp/menuError.jsp", this, output);
    }

  }

}
