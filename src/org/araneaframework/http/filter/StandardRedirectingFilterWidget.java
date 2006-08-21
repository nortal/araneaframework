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

package org.araneaframework.http.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.util.ServletUtil;


/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardRedirectingFilterWidget extends BaseFilterWidget {
  protected void update(InputData input) throws Exception {
    ResponseWrapper wrapper = new ResponseWrapper(ServletUtil.getResponse(getOutputData()));    
    ServletUtil.setResponse(getOutputData(), wrapper);
    getOutputData().extend(ResponseWrapper.class, wrapper);
    
    super.update(input);
  }
  
  protected void render(OutputData output) throws Exception {
    try {
      ResponseWrapper wrapper = (ResponseWrapper) output.narrow(ResponseWrapper.class);
      if (!wrapper.canRender())
        return;
    }
    catch (NoSuchNarrowableException e) {
      //Fine
    }
        
    super.render(output);
  }
  
  public static class ResponseWrapper extends HttpServletResponseWrapper {
    private int status;
    
    public ResponseWrapper(HttpServletResponse arg0) {
      super(arg0);
    }
    
    public void sendRedirect(String arg0) throws IOException {      
      status = SC_MOVED_TEMPORARILY;
      
      super.sendRedirect(arg0);
    }   
    
    public void setStatus(int arg0) {
      status = arg0;
      
      super.setStatus(arg0);
    }
    
    public void setStatus(int arg0, String arg1) {
      status = arg0;
      
      super.setStatus(arg0, arg1);
    }
    
    public void sendError(int arg0) throws IOException {      
      status = arg0;
      
      super.sendError(arg0);
    }
    
    public void sendError(int arg0, String arg1) throws IOException {
      status = arg0;
      
      super.sendError(arg0, arg1);
    }
    
    public boolean canRender() {
      if (status >= 300 && status < 400 || status >= 500 && status < 600)
        return false;
      
      return true;
    }
  }
}
