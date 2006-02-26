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

package org.araneaframework.template.framework.error;

import java.io.Writer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.servlet.ServletOutputData;

public class TemplateCriticalErrorHandler extends BaseWidget  {
  private static final Logger log = Logger.getLogger(TemplateCriticalErrorHandler.class);
	
	protected Throwable exception;
  
  public TemplateCriticalErrorHandler(Throwable exception) {
    this.exception = exception;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    Writer out = ((ServletOutputData) output).getResponse().getWriter();
    
    ((ServletOutputData) output).getResponse().setContentType("text/html; charset=UTF-8");
    
    out.write("<html><head><title>Critical error occured!</title></head><body>");
    out.write("<h1>Critical error occured!</h1>");
    if (ExceptionUtils.getRootCause(exception) != null) {
      out.write("Root cause:<br/>");    
      out.write("<pre>"+ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(exception))+"</pre>");
    }
    out.write("Stack trace:<br/>");
    out.write("<pre>"+ExceptionUtils.getFullStackTrace(exception)+"</pre>");
    out.write("</body></html>");
  }
}
