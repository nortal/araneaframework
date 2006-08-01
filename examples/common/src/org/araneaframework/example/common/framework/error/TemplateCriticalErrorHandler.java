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

package org.araneaframework.example.common.framework.error;

import java.io.Writer;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.servlet.ServletOutputData;

public class TemplateCriticalErrorHandler extends BaseService  {
  protected Throwable exception;
  
  public TemplateCriticalErrorHandler(Throwable exception) {
    this.exception = exception;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    Writer out = ((ServletOutputData) output).getResponse().getWriter();
    
    ((ServletOutputData) output).getResponse().setContentType("text/html; charset=UTF-8");
    out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
        "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
        "<head>\n" + 
        "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" + 
        "<title>Critical Error!!!</title>\n" + 
        "\n" + 
        "<link rel=\"stylesheet\" type=\"text/css\" href=\"?loadCSSFile=styles/_styles_screen.css&importerType=cssFileImporter\" media=\"screen\"/>" +  
        "<link rel=\"stylesheet\" type=\"text/css\" href=\"?loadCSSFile=styles/_styles_global.css&importerType=cssFileImporter\" media=\"screen\"/>" +
        "\n" + 
         "<!-- calendar -->\n" + 
        "<link href=\"calendar/calendar.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen, projection\" />\n" + 
        "<script type=\"text/javascript\" src=\"calendar/calendar.js\"></script>\n" + 
        "\n" + 
        "<script type=\"text/javascript\" src=\"calendar/calendar-ee.js\"></script>\n" + 
        "<script type=\"text/javascript\" src=\"calendar/calendar-setup.js\"></script>\n" + 
        "<!-- /calendar -->\n" + 
        "</head>\n" + 
        "\n" + 
        "<body id=\"error\">\n" + 
        "\n" + 
        "<div id=\"cont1\">\n" + 
        " <div id=\"header\">\n" + 
        "   <div class=\"box1\">\n" + 
        "     <a href=\"#\" id=\"logo\"><img src=\"gfx/logo_aranea_print.gif\" alt=\"\" /></a>\n" + 
        "   </div>\n" + 
        "\n" + 
        " </div>\n" + 
        " <div class=\"stripe1\">&nbsp;</div>\n" + 
        " <div id=\"wholder\">\n" + 
        "   <div id=\"content\">\n" + 
        "     <!-- start content -->\n" + 
        "     <h1>Error</h1>\n" + 
        "<p><blockquote><a style='font-size: larger; text-decoration: underline' href='javascript:' onclick='javascript:window.location=window.location; return false'>Logout</a>" +
        "<iframe width='0' height='0' src='?destroySession=true' style='display: none'></iframe></blockquote></p>" +          
        "     <div class=\"msg-error\">\n" +        
        "       <div style='overflow: auto;' id=\"crashinfo\">\n");
        if (ExceptionUtils.getRootCause(exception) != null) {
          out.write("<b>Root cause:</b><br/>");    
          out.write("<pre style=\'font-size: 10pt\'>"+ExceptionUtils.getFullStackTrace(ExceptionUtils.getRootCause(exception))+"</pre>");
        }        
        out.write("<b>Stack trace:</b><br/>");
        out.write("<pre style=\'font-size: 10pt\'>"+ExceptionUtils.getFullStackTrace(exception)+"</pre>");
        out.write("</body></html>");
        out.write(        
        "       </div>\n" + 
        "     </div>\n" + 
        "     <!-- end content -->\n" + 
        "   </div>\n" + 
        "\n" + 
        " </div>\n" + 
        " <div class=\"clear1\">&nbsp;</div>\n" + 
        "</div>\n" + 
        "\n" + 
        "</body>\n" + 
        "</html>\n" + 
        "");
  }
}
