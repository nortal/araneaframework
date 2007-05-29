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
  
  private Map extensions = new HashMap();
  
  private Map globalData = new HashMap();
  private Map scopedData = new HashMap();
  private boolean dataInited;
  
  private StringBuffer path;
  private LinkedList pathPrefixes = new LinkedList();
  
  /**
   * Constructs a StandardServletInputData from the request. 
   * @param request
   */
  public StandardServletInputData(HttpServletRequest request) {
    Assert.notNullParam(request, "request");
    
    setRequest(request);
    extend(HttpServletRequest.class, req);
  }
  
  private void setRequest(HttpServletRequest request) {
    req = request;
    dataInited = false;
    
    path = new StringBuffer(req.getPathInfo() == null ? "" : req.getPathInfo());
    pathPrefixes = new LinkedList();
  }

  private void initData() {
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
    
    dataInited = true;
  }
  
  public Map getGlobalData() {
    if (!dataInited) initData();
    return Collections.unmodifiableMap(globalData);
  }

  public Map getScopedData(Path scope) {
    if (!dataInited) initData();
    Map result = (Map)scopedData.get(scope.toString());
    if (result != null) {
      return Collections.unmodifiableMap(result);  
    }
    else {
      return Collections.unmodifiableMap(Collections.EMPTY_MAP);
    }
  }

  public void extend(Class interfaceClass, Object implementation) {
    if (HttpServletRequest.class.equals(interfaceClass) && implementation != null)
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
  
  public String getContainerPath() {
    return req.getServletPath();
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
