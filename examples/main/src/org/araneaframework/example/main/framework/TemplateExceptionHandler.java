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
package org.araneaframework.example.main.framework;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.framework.ContinuationContext;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.servlet.ServletInputData;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.util.ServletUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class TemplateExceptionHandler extends BaseService { 
  private static final Logger log = Logger.getLogger(TemplateExceptionHandler.class);
  
  public static final String ERROR_ACTION = "errorAction";
  
  private Throwable exception;  
  private Environment contextEnvironment;
  
  private boolean rendered = false;
  
  public TemplateExceptionHandler(Throwable exception, Environment environment) {
    this.contextEnvironment = environment;
    this.exception = exception;
        
    log.error("Exception occured:", exception);
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (rendered) {
      Object errorAction = input.getGlobalData().get(ERROR_ACTION);
      
      if ("retry".equals(errorAction)) {
        retry();
      }
      else if ("cancelFlow".equals(errorAction)) {
        cancelFlow();
      }
    }
    
    if (ExceptionUtils.getRootCause(exception) != null) {
      ServletUtil.publishModel((ServletInputData) input, "rootStackTrace", 
          ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(exception)));
    }        
    ServletUtil.publishModel((ServletInputData) input, "fullStackTrace", 
        ExceptionUtils.getFullStackTrace(exception)); 
    
    ServletUtil.include("/WEB-INF/jsp/error.jsp", getEnvironment(), (ServletOutputData) output);
    
    rendered = true;
  }

  private void retry() throws Exception {
    ContinuationContext cCtx = 
      (ContinuationContext) getEnvironment().getEntry(ContinuationContext.class);
    cCtx.finish();
  }
  
  private void cancelFlow() throws Exception {
    FlowContext flowCtx = 
      (FlowContext) contextEnvironment.getEntry(FlowContext.class);
    
    flowCtx.cancel();
    
    retry();
  }
  
  /*private void mainMenu()  throws Exception {
    EhlMenuControlContext mcCtx = 
      (EhlMenuControlContext) contextEnvironment.getEntry(EhlMenuControlContext.class);
    
    mcCtx.reset(new org.araneaframework.core.StandardEnvironmentAwareCallback() {});
    
    retry();
  }*/
  
  /*private void logout()  throws Exception {
    EhlTrustedSecurityContext tsCtx = (EhlTrustedSecurityContext) contextEnvironment.getEntry(EhlTrustedSecurityContext.class);
    tsCtx.logout((ServletInputData) getCurrentInput());
    
    retry();
  }*/
}
