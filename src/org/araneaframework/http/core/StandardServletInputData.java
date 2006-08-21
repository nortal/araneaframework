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

package org.araneaframework.http.core;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.iterators.EnumerationIterator;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.EmptyPathStackException;
import org.araneaframework.core.NoCurrentOutputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.StandardPath;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.util.ServletUtil;

/**
 * A ServletInputdata implementation which uses a StandardPath for determining
 * the scope.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletInputData implements HttpInputData {
  private HttpServletRequest req;
  
  private StringBuffer scopeBuf = new StringBuffer();
  private Map extensions = new HashMap();
  
  private Map globalData = new HashMap();
  private Map scopedData = new HashMap();
  
  private StringBuffer path;
  private LinkedList pathPrefixes = new LinkedList();
  
  /**
   * Constructs a StandardServletInputData from the request. 
   * @param request
   */
  public StandardServletInputData(HttpServletRequest request) {
    setRequest(request);
    extend(HttpServletRequest.class, req);
  }
  
  public void setRequest(HttpServletRequest request) {
    req = request;
    
    globalData.clear();
    scopedData.clear();
    
    Enumeration params = req.getParameterNames();
    
    while (params.hasMoreElements()) {
      String key = (String)params.nextElement();
      
      if (key.lastIndexOf(".") == -1) {
        //global data - no prefix data
        globalData.put(key, req.getParameter(key));
      }
      else {
        //scoped data
        String prefix = key.substring(0, key.lastIndexOf("."));
        String subKey = key.substring(key.lastIndexOf(".") + 1);
        
        Map map = (Map)scopedData.get(prefix);
        
        if (map == null) {
          map = new HashMap();
          scopedData.put(prefix, map);
        }

        map.put(subKey, req.getParameter(key));
      }
    }
    
    path = new StringBuffer(req.getPathInfo() == null ? "" : req.getPathInfo());
    pathPrefixes = new LinkedList();
  }
  
  private HttpServletRequest getRequest() {
    return ServletUtil.getRequest(this);
  }

  public Path getScope() {
    return new StandardPath(scopeBuf.toString());
  }

  public void pushScope(Object step) {
    if (scopeBuf.length()>0) {
      scopeBuf.append("."+step);
    }
    else {
      scopeBuf.append(step);
    }
  }

  public void popScope() {
    if (scopeBuf.toString().lastIndexOf(".") != -1) {
      scopeBuf.setLength(scopeBuf.toString().lastIndexOf("."));
    }
    else {
      if (scopeBuf.length()==0) {
        throw new EmptyPathStackException();
      }
      scopeBuf.setLength(0);
    }
  }
  
  public Map getGlobalData() {    
    return Collections.unmodifiableMap(globalData);
  }

  public Map getScopedData() {
    Map result = (Map)scopedData.get(scopeBuf.toString());
    if (result != null) {
      return Collections.unmodifiableMap(result);  
    }
    else {
      return Collections.unmodifiableMap(new HashMap());
    }
  }

  public void extend(Class interfaceClass, Object implementation) {
    if (HttpServletRequest.class.equals(interfaceClass))
      setRequest((HttpServletRequest) implementation);
    
    extensions.put(interfaceClass, implementation);
  }

  public Object narrow(Class interfaceClass) {
    Object extension = extensions.get(interfaceClass); 
    if (extension == null)
      throw new NoSuchNarrowableException(interfaceClass);
    return extension;
  }

	public OutputData getOutputData() {
		OutputData output = (OutputData)req.getAttribute(OutputData.OUTPUT_DATA_KEY);
		if (output == null)
			throw new NoCurrentOutputDataSetException("No OutputData set in the request.");
		else
			return output;
	}

  public String getCharacterEncoding() {
    return req.getCharacterEncoding();
  }

  public String getContainerURL() {
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
  
  public String getContextURL() {
    StringBuffer url = new StringBuffer();
    url.append(req.getScheme());
    url.append("://");
    url.append(req.getServerName());    
    url.append(":");
    url.append(req.getServerPort());
    url.append(req.getContextPath());
    return url.toString();
  }
  
  public URL getRequestURL() {
    try {
      return new URL(req.getRequestURL().toString());
    }
    catch (MalformedURLException e) {
      throw new AraneaRuntimeException(e);
    }
  }

  public String getContentType() {
    return req.getContentType();
  }

  public Locale getLocale() {
    return req.getLocale();
  }

  public Iterator getParameterNames() {
    return new EnumerationIterator(req.getParameterNames());
  }

  public String[] getParameterValues(String name) {
    return req.getParameterValues(name);
  }

  public String getPath() {
    return path.toString();
  }

  public void popPathPrefix() {
    path.insert(0, (String) pathPrefixes.removeLast()); 
  }

  public void pushPathPrefix(String pathPrefix) {
    pathPrefixes.addLast(pathPrefix);
    path.delete(0, pathPrefix.length() - 1);
  }
  
  public void setCharactedEncoding(String encoding) throws UnsupportedEncodingException {
    req.setCharacterEncoding(encoding);
  }
}
