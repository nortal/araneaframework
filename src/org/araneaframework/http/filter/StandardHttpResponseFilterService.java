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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.ServletUtil;

/**
 * A filter which sets all the necessary headers of the response. 
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardHttpResponseFilterService extends BaseFilterService {
  private String contentType = "text/html; charset=UTF-8";
  private boolean cacheable = false;
  private long cacheHoldingTime = 3600000;
  
  private Map cookies = new HashMap();
  private Map headers = new HashMap();
  
  /**
   * Sets if the response is cacheable or not. By default it is cacheable.
   */
  public void setCacheable(boolean cacheable) {
    this.cacheable = cacheable;
  }
  
  /**
   * Sets the content type of the request. This is a required field of the response.
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }
  
  /**
   * Constructs cookies from the key, value pair in the map of the cookies and sets them.
   * @param cookies
   */
  public void setCookies(Map cookies) {
    this.cookies = cookies;
  }
  
  /**
   * Sets the headers of the response from the map headers. The key of the map is the name of
   * the header and the value is the corresponding value.
   */
  public void setHeaders(Map headers) {
    this.headers = headers;
  }
  
  /**
   * Sets the cache-control's max-age parameter, value is in milliseconds.
   */
  public void setCacheHoldingTime(long cacheHoldingTime) {
    this.cacheHoldingTime = cacheHoldingTime;
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);
    
    if (contentType == null) {
      throw new AraneaRuntimeException("Content type not set!");
    }
    
    response.setContentType(contentType);
    
    if (!cacheable) {
      response.setHeader("Pragma", "no-cache");       
      response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
      response.setDateHeader ("Expires", 1);
    }
    else {
      response.setHeader("Cache-Control", "max-age=" + (cacheHoldingTime / 1000));
      response.setDateHeader ("Expires", System.currentTimeMillis () + cacheHoldingTime);
    }
    
    for (Iterator i = headers.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();      
      response.setHeader((String) entry.getKey(), (String) entry.getValue());    
    }
    
    for (Iterator i = cookies.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();      
      response.addCookie(new Cookie((String) entry.getKey(), (String) entry.getValue()));    
    }
    
    childService._getService().action(path, input, output);
  }
}
