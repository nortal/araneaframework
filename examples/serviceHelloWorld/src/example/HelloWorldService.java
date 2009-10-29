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

package example;

import java.io.Writer;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.jsp.util.JspUtil;

public class HelloWorldService extends BaseService {
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    ((HttpOutputData) output).setContentType("text/html");    
    Writer out = ((HttpOutputData) output).getWriter();
    
    try {
      JspUtil.writeStartTag(out, "html"); {
        JspUtil.writeStartTag(out, "head"); {
          JspUtil.writeStartTag(out, "title"); {
            out.write("Hello world!");
          }
          JspUtil.writeEndTag(out, "title");
        }
        JspUtil.writeEndTag(out, "head");
        
        JspUtil.writeStartTag(out, "body"); {
          JspUtil.writeStartTag(out, "h1"); {
            out.write("Hello world!");
          }
          JspUtil.writeEndTag(out, "h1");
        }
        JspUtil.writeEndTag(out, "body");
        
      } 
      JspUtil.writeEndTag(out, "html");
    
      out.flush();
    }
    finally {
      out.close();
    }
  }
}
