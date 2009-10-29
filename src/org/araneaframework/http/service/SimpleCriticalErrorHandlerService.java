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

package org.araneaframework.http.service;

import java.io.Writer;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Service;
import org.araneaframework.core.BaseService;
import org.araneaframework.framework.ExceptionHandlerFactory;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.ServletUtil;

public class SimpleCriticalErrorHandlerService extends BaseService {
  protected Throwable exception;
  
  public SimpleCriticalErrorHandlerService() {
    //Factory constructor
  }
  
  public SimpleCriticalErrorHandlerService(Throwable exception) {
    this.exception = exception;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    Writer out = ((HttpOutputData) output).getWriter();
    
    ServletUtil.getResponse(output).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    
    ((HttpOutputData) output).setContentType("text/html; charset=UTF-8");
    out.write("<html><head><title>Critical error occured!</title></head><body>");
    if (ExceptionUtils.getRootCause(exception) != null) {
      out.write("<b>Root cause:</b><br/>");    
      out.write("<pre>"+ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(exception))+"</pre>");
    }        
    out.write("<b>Stack trace:</b><br/>");
    out.write("<pre>"+ExceptionUtils.getFullStackTrace(exception)+"</pre>");
    out.write("</body></html>");
  }

  
  public static class Factory implements ExceptionHandlerFactory {
    public Service buildExceptionHandler(Throwable e, Environment environment) {
      return new SimpleCriticalErrorHandlerService(e);
    }
  }    
}
