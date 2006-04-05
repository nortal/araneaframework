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

package example;

import java.io.OutputStreamWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.ServletOutputData;

public class HelloWorldService extends BaseService {
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ((ServletOutputData) output).getResponse();    
    response.setContentType("text/html");
        
    Writer out = new OutputStreamWriter(response.getOutputStream());
    try {
      UiUtil.writeStartTag(out, "html"); {
        UiUtil.writeStartTag(out, "head"); {
          UiUtil.writeStartTag(out, "title"); {
            out.write("Hello world!");
          }
          UiUtil.writeEndTag(out, "title");
        }
        UiUtil.writeEndTag(out, "head");
        
        UiUtil.writeStartTag(out, "body"); {
          UiUtil.writeStartTag(out, "h1"); {
            out.write("Hello world!");
          }
          UiUtil.writeEndTag(out, "h1");
        }
        UiUtil.writeEndTag(out, "body");
        
      } 
      UiUtil.writeEndTag(out, "html");
    
      out.flush();
    }
    finally {
      out.close();
    }
  }
}
