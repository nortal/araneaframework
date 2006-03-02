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

package org.araneaframework.framework.container;

import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.framework.ContinuationManagerContext;
import org.araneaframework.framework.ExceptionHandlerFactory;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardViewPortWidget extends StandardWidget {
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  
  /**
   * The widget's key in the output attributes.
   */
  public static final String VIEW_PORT_WIDGET_KEY = "org.araneaframework.framework.container.StandardViewPortWidget";
  
  /**
   * The key of the child widget in the children set.
   */
  public static final String CHILD_KEY = "root";
  
  private Widget root;
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************  
    
  public void setRoot(Widget root) {
    this.root = root;
  }
  
  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************

  protected void init() throws Exception {
    addWidget(CHILD_KEY, root);
  }
  
  protected void render(OutputData output) throws Exception {
    output.pushAttribute(VIEW_PORT_WIDGET_KEY, this);
    
    try {
      output.pushScope(CHILD_KEY);
      ((Widget) getChildren().get(CHILD_KEY))._getWidget().render(output);
    }
    finally {
      output.popScope();
      output.popAttribute(VIEW_PORT_WIDGET_KEY);
    }
  }
  
  protected void handleException(Exception e) throws Exception {
    if (getEnvironment().getEntry(ContinuationManagerContext.class) != null
        && getEnvironment().getEntry(ExceptionHandlerFactory.class) != null) {
      ContinuationManagerContext contCtx = 
        (ContinuationManagerContext) getEnvironment().getEntry(ContinuationManagerContext.class);
      
      if (!contCtx.isRunning()) {
        ExceptionHandlerFactory handlerFactory = 
          (ExceptionHandlerFactory) getEnvironment().getEntry(ExceptionHandlerFactory.class);
        contCtx.start(handlerFactory.buildExceptionHandler(e, getChildWidgetEnvironment()));
      }
    }
    
    throw e;
  }
}
