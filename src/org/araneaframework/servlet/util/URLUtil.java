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

package org.araneaframework.servlet.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.InputData;
import org.araneaframework.servlet.ServletInputData;

/**
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 */
public class URLUtil {
  /**
   * Removes all leading and trailing slashes.
   */
  public static String normalizeURI(String uri) {
    if (uri == null) 
      return null;
    
    // lose the first slashes
    while (uri.indexOf("/") == 0 && uri.length() > 0)
      uri = uri.substring(1);

    // lose the first slashes
    while (uri.lastIndexOf("/") == (uri.length() - 1) && uri.length() > 0)
      uri = uri.substring(0, uri.length() - 1);

    return uri;
  }

  public static String[] splitURI(String uri) {
    List result = new ArrayList();
    uri = normalizeURI(uri);

    StringTokenizer tokenizer = new StringTokenizer(uri, "/");
    while (tokenizer.hasMoreTokens()) {
      result.add(tokenizer.nextToken());
    }

    return (String[]) result.toArray(new String[result.size()]);
  }
  
  public static String parametrizeURI(String uri, Map parameters) {
    StringBuffer sb = new StringBuffer(uri);
    
    if (parameters != null && parameters.size() > 0) {
      sb.append('?');
      for (Iterator i = parameters.entrySet().iterator(); i.hasNext();) {
        Map.Entry pair = (Map.Entry) i.next();
        sb.append((String)pair.getKey());
        sb.append('=');
        sb.append(pair.getValue());
        if (i.hasNext())
          sb.append('&');
      }
    }

    return sb.toString();
  }
  
  /**
   * Returns request URL up to the servlet name.
   * 
   * This is an utility method alike to <code>HttpServletRequest.getRequestURL</code>, but returned URL 
   * only contains only protocol, server name, port number, web application context path,
   * and servlet path. Query string parameters are ignored and so is everything after servlet path. 
   * As URL might be simulated by {@link org.araneaframework.framework.MountContext} this method
   * should usually be used instead of <code>getRequestURL</code>.
   * 
   * @param input request data
   * @return request URL up to the servlet name.
   */
  public static String getServletRequestURL(InputData input) {
    HttpServletRequest req = ((ServletInputData) input).getRequest();

    StringBuffer url = new StringBuffer();
    url.append(req.getScheme());
    url.append("://");
    url.append(req.getServerName());    
    url.append(":");
    url.append(req.getServerPort());
    url.append(req.getContextPath());
    url.append(req.getServletPath());
    return url.toString();
  }
}
