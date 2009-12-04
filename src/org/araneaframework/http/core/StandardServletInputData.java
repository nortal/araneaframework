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

package org.araneaframework.http.core;

import java.util.StringTokenizer;

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
import org.apache.commons.lang.StringUtils;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoCurrentOutputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpInputData;

/**
 * A ServletInputdata implementation which uses a StandardPath for determining the scope.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletInputData implements HttpInputData {

  protected static final String PATH_SEPARATOR = "/";

  private HttpServletRequest req;

  private Map<Class<?>, Object> extensions = new HashMap<Class<?>, Object>();

  private Map<String, String> globalData = new HashMap<String, String>();

  private Map<String, Map<String, String>> scopedData = new HashMap<String, Map<String, String>>();

  private boolean dataInited;

  private String path;

  private LinkedList<String> pathPrefixes = new LinkedList<String>();

  private String servletPath;

  private boolean useFullURL = true;

  /**
   * Constructs a StandardServletInputData from the request.
   * 
   * @param request
   */
  public StandardServletInputData(HttpServletRequest request) {
    Assert.notNullParam(request, "request");

    this.servletPath = request.getServletPath();

    setRequest(request);
    extend(HttpServletRequest.class, this.req);
  }

  private void setRequest(HttpServletRequest request) {
    this.req = request;
    this.dataInited = false;

    this.path = StringUtils.defaultString(this.req.getPathInfo());
    this.pathPrefixes = new LinkedList<String>();
  }

  @SuppressWarnings("unchecked")
  private void initData() {
    this.globalData.clear();
    this.scopedData.clear();

    Enumeration<String> params = this.req.getParameterNames();

    while (params.hasMoreElements()) {
      String key = params.nextElement();

      if (!StringUtils.contains(key, Path.SEPARATOR)) {
        // global data - no prefix data
        this.globalData.put(key, this.req.getParameter(key));
      } else {
        // scoped data
        String prefix = StringUtils.substringBeforeLast(key, Path.SEPARATOR);
        String subKey = StringUtils.substringAfterLast(key, Path.SEPARATOR);

        Map<String, String> map = this.scopedData.get(prefix);

        if (map == null) {
          map = new HashMap<String, String>();
          this.scopedData.put(prefix, map);
        }

        map.put(subKey, this.req.getParameter(key));
      }
    }

    this.dataInited = true;
  }

  public Map<String, String> getGlobalData() {
    if (!this.dataInited) {
      initData();
    }
    return Collections.unmodifiableMap(this.globalData);
  }

  public Map<String, String> getScopedData(Path scope) {
    if (!this.dataInited) {
      initData();
    }

    Map<String, String> result = this.scopedData.get(scope.toString());

    if (result != null) {
      return Collections.unmodifiableMap(result);
    } else {
      return Collections.emptyMap();
    }
  }

  public <T> void extend(Class<T> interfaceClass, T implementation) {
    if (HttpServletRequest.class.equals(interfaceClass) && implementation != null) {
      setRequest((HttpServletRequest) implementation);
    }

    this.extensions.put(interfaceClass, implementation);
  }

  @SuppressWarnings("unchecked")
  public <T> T narrow(Class<T> interfaceClass) {
    T extension = (T) this.extensions.get(interfaceClass);
    if (extension == null) {
      throw new NoSuchNarrowableException(interfaceClass);
    }
    return extension;
  }

  public OutputData getOutputData() {
    OutputData output = (OutputData) this.req.getAttribute(OutputData.OUTPUT_DATA_KEY);
    if (output == null) {
      throw new NoCurrentOutputDataSetException("No OutputData set in the request.");
    } else {
      return output;
    }
  }

  public String getCharacterEncoding() {
    return this.req.getCharacterEncoding();
  }

  public String getContainerURL() {
    StringBuffer url = new StringBuffer();
    if (this.useFullURL) {
      url.append(this.req.getScheme());
      url.append("://");
      url.append(this.req.getServerName());
      url.append(":");
      url.append(this.req.getServerPort());
    }
    url.append(this.req.getContextPath());
    url.append(this.servletPath);
    return url.toString();
  }

  public String getContainerPath() {
    return this.servletPath;
  }

  public String getContextURL() {
    StringBuffer url = new StringBuffer();
    url.append(this.req.getScheme());
    url.append("://");
    url.append(this.req.getServerName());
    url.append(":");
    url.append(this.req.getServerPort());
    url.append(this.req.getContextPath());
    return url.toString();
  }

  public String getContextPath() {
    return this.req.getContextPath();
  }

  public String getRequestURL() {
    return this.req.getRequestURL().toString();
  }

  public String getContentType() {
    return this.req.getContentType();
  }

  public Locale getLocale() {
    return this.req.getLocale();
  }

  @SuppressWarnings("unchecked")
  public Iterator<String> getParameterNames() {
    return new EnumerationIterator(this.req.getParameterNames());
  }

  public String[] getParameterValues(String name) {
    return this.req.getParameterValues(name);
  }

  public String getPath() {
    return this.path.toString();
  }

  public String getSimplePath() {
    return trim(this.path.toString());
  }

  public String popPathPrefix() {
    String path = null;

    if (!this.pathPrefixes.isEmpty()) {
      path = this.pathPrefixes.removeLast();
      this.path = new StringBuffer(path).append(PATH_SEPARATOR).append(this.path).toString();
    }

    return path;
  }

  public void pushPathPrefix(String pathPrefix) {
    Assert.notEmptyParam(pathPrefix, "pathPrefix");

    if (startsWith(this.path, pathPrefix)) {
      this.pathPrefixes.addLast(pathPrefix);
      this.path = StringUtils.substringAfter(this.path, pathPrefix);
    }
  }

  public void setCharacterEncoding(String encoding) {
    Assert.notEmptyParam(encoding, "encoding");

    try {
      this.req.setCharacterEncoding(encoding);
    } catch (UnsupportedEncodingException e) {
      ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Checks whether the path (<code>src</code>) starts with the given <code>pathItem</code>. Similar to
   * {@link String#startsWith(String)}, but does not care whether the path starts with a forward-slash ("/").
   * 
   * @param src The path.
   * @param pathItem The path item that the path must begin with. Must not contain a forward-slash!
   * @return Whether the path starts with the given <code>pathItem</code>
   * @since 2.0
   */
  protected static boolean startsWith(String src, String pathItem) {
    Assert.isTrue(pathItem.indexOf(PATH_SEPARATOR) < 0, "The path item must not contain a forward-slash!");
    StringTokenizer pathItems = new StringTokenizer(trim(src), PATH_SEPARATOR);
    return StringUtils.equals(pathItem, pathItems.hasMoreTokens() ? pathItems.nextToken() : null);
  }

  /**
   * Removes forward-slashes from the beginning of the input string.
   * 
   * @param path The string from which forward-slashes should be removed from the beginning.
   * @return The input string without forward-slashes in the beginning.
   * @since 2.0
   */
  protected static String trim(String path) {
    while (StringUtils.startsWith(path, PATH_SEPARATOR)) {
      path = StringUtils.substringAfter(path, PATH_SEPARATOR);
    }
    return path;
  }

  public void setUseFullURL(boolean useFullURL) {
    this.useFullURL = useFullURL;
  }
}
