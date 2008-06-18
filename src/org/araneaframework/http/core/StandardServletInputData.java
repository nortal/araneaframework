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
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.iterators.EnumerationIterator;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoCurrentOutputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpInputData;

/**
 * A ServletInputdata implementation which uses a StandardPath for determining
 * the scope.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletInputData implements HttpInputData {
  private HttpServletRequest req;
  
  private Map<Class<?>, Object> extensions = new HashMap<Class<?>, Object>();
  
  private Map<String, String> globalData = new HashMap<String, String>();
  private Map<String, Map<String, String>> scopedData = new HashMap<String, Map<String, String>>();
  private boolean dataInited;
  
  private StringBuffer path;
  private LinkedList<String> pathPrefixes = new LinkedList<String>();
  
  private String servletPath;
  
  /**
   * Constructs a StandardServletInputData from the request. 
   * @param request
   */
  public StandardServletInputData(HttpServletRequest request) {
    Assert.notNullParam(request, "request");
    
    servletPath = request.getServletPath();
    
    setRequest(request);
    extend(HttpServletRequest.class, req);
  }
  
  private void setRequest(HttpServletRequest request) {
    req = request;
    dataInited = false;
    
    path = new StringBuffer(req.getPathInfo() == null ? "" : req.getPathInfo());
    pathPrefixes = new LinkedList<String>();
  }

  @SuppressWarnings("unchecked")
  private void initData() {
	globalData.clear();
    scopedData.clear();
    
    Enumeration<String> params = req.getParameterNames();
    
    while (params.hasMoreElements()) {
      String key = params.nextElement();
      
      if (key.lastIndexOf(".") == -1) {
        //global data - no prefix data
        globalData.put(key, req.getParameter(key));
      }
      else {
        //scoped data
        String prefix = key.substring(0, key.lastIndexOf("."));
        String subKey = key.substring(key.lastIndexOf(".") + 1);
        
        Map<String, String> map = scopedData.get(prefix);
        
        if (map == null) {
          map = new HashMap<String, String>();
          scopedData.put(prefix, map);
        }

        map.put(subKey, req.getParameter(key));
      }
    }
    
    dataInited = true;
  }
  
  public Map<String, String> getGlobalData() {
    if (!dataInited) initData();
    return Collections.unmodifiableMap(globalData);
  }

  public Map<String, String> getScopedData(Path scope) {
    if (!dataInited) {
			initData();
    }

    Map<String, String> result = scopedData.get(scope.toString());

    if (result != null) {
			return Collections.unmodifiableMap(result);
		} else {
			return Collections.emptyMap();
		}
  }

  public <T> void extend(Class<T> interfaceClass, T implementation) {
    if (HttpServletRequest.class.equals(interfaceClass) && implementation != null)
      setRequest((HttpServletRequest) implementation);
    
    extensions.put(interfaceClass, implementation);
  }

  @SuppressWarnings("unchecked")
  public <T> T narrow(Class<T> interfaceClass) {
    T extension = (T) extensions.get(interfaceClass); 
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
    url.append(servletPath);
    return url.toString();
  }
  
  public String getContainerPath() {
    return servletPath;
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
  
  public String getContextPath() {
    return req.getContextPath();
  }
  
  public String getRequestURL() {
    return req.getRequestURL().toString();
  }

  public String getContentType() {
    return req.getContentType();
  }

  public Locale getLocale() {
    return req.getLocale();
  }

  @SuppressWarnings("unchecked")
  public Iterator<String> getParameterNames() {
    return new EnumerationIterator(req.getParameterNames());
  }

  public String[] getParameterValues(String name) {
    return req.getParameterValues(name);
  }

  public String getPath() {
    return path.toString();
  }

  public void popPathPrefix() {
    path.insert(0, pathPrefixes.removeLast()); 
  }

  public void pushPathPrefix(String pathPrefix) {
    Assert.notEmptyParam(pathPrefix, "pathPrefix");
    
    pathPrefixes.addLast(pathPrefix);
    path.delete(0, pathPrefix.length() - 1);
  }
  
  public void setCharacterEncoding(String encoding) {
    Assert.notEmptyParam(encoding, "encoding");

    try {
      req.setCharacterEncoding(encoding);
    }
    catch (UnsupportedEncodingException e) {
      ExceptionUtil.uncheckException(e);
    }
  }

}
