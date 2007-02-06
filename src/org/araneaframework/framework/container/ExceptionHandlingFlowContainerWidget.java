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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.http.util.AtomicResponseHelper;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class ExceptionHandlingFlowContainerWidget extends StandardFlowContainerWidget {
  private static final Logger log = Logger.getLogger(ExceptionHandlingFlowContainerWidget.class);
  
  protected Exception exception;  
  
  public ExceptionHandlingFlowContainerWidget() {
    super();
  }

  public ExceptionHandlingFlowContainerWidget(Widget topWidget) {
    super(topWidget);
  }
  
  protected void init() throws Exception {
    super.init();
    
    addEventListener("retry", new ProxyEventListener(this));
    addEventListener("cancel", new ProxyEventListener(this));
    addEventListener("reset", new ProxyEventListener(this));
  }

  public void handleEventRetry() throws Exception {
    //noop
  }  
  
  public void handleEventCancel() throws Exception {
    cancel();
  }
  
  public void handleEventReset() throws Exception {
    reset(null);    
  }
  
  protected void handleWidgetException(Exception e) throws Exception {
    this.exception = e;
    
    if (ExceptionUtils.getRootCause(e) != null)
      log.error("Critical exception occured: ", ExceptionUtils.getRootCause(e));
    else
      log.error("Critical exception occured: ", e);
  }
  
  protected void update(InputData input) throws Exception {
    if (exception == null)
      super.update(input);    
    else handleUpdate(input);
  }
  
  protected void event(Path path, InputData input) throws Exception {
    if (exception == null)
      super.event(path, input);    
    else if (path != null && !path.hasNext())
      handleEvent(input);
  }
  
  protected void process() throws Exception {
    if (exception == null)
      super.process();
    else handleProcess();
  }
  
  protected void render(OutputData output) throws Exception {
    AtomicResponseHelper arUtil = new AtomicResponseHelper(output);
    
    try {
      if (exception != null)
        throw exception;
      
      super.render(output);
    }
    catch (Exception e) {
      arUtil.rollback();
      
      log.error("Handling error:", e);
            
      renderExceptionHandler(output, e);
      
      exception = null;
    }
    
    arUtil.commit();
  }
  
  protected abstract void renderExceptionHandler(OutputData output, Exception e) throws Exception;
}
