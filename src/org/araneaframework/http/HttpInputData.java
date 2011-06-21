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

package org.araneaframework.http;

import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.InputData;

/**
 * Provides methods to deal with and manipulate low-level HTTP constructs. Wraps the {@link HttpServletRequest}.
 * <p>
 * The main difference is how we handle the so called PATH_INFO (the additional path after the servlet). The problem is
 * that unlike most common cases Aranea components form a hierarchy. Therefore if a parent is mapped to path prefix
 * "myPath/*" and its child is mapped to a path prefix "myChildPath/*" if the path handling were absolute the child
 * would never get the mapped calls. This is due to the child being really mapped to the path "myPath/myChildPath".
 * Therefore the parent must consume the prefix "myPath/" using method <code>pushPathPrefix()</code> and then the child
 * will be correctly matched to the relative path "myChildPath".
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * @see HttpOutputData
 */
public interface HttpInputData extends InputData {

  /**
   * Returns an iteration over names of parameters submitted with the current request.
   * 
   * @return An iteration over names of request parameters.
   */
  Iterator<String> getParameterNames();

  /**
   * Returns the array of values of the specified parameter submitted with the current request.
   * 
   * @param name The name of the parameter for which the values must be retrieved.
   * @return The values associated with the given parameter.
   */
  String[] getParameterValues(String name);

  /**
   * Returns the character encoding that is used to decode the request parameters.
   * 
   * @return A character encoding.
   */
  String getCharacterEncoding();

  /**
   * Sets the character encoding that is used to decode request parameters. Note that this must be called before any
   * parameters are read according to the Servlet specification.
   * 
   * @param encoding The character encoding that will be used to decode request parameters.
   */
  void setCharacterEncoding(String encoding);

  /**
   * Returns the MIME content type of the request body or <code>null</code> if the body is lacking.
   * 
   * @return The MIME content type of the request body or <code>null</code>.
   */
  String getContentType();

  /**
   * Returns the preferred Locale that the client will accept content in, based on the Accept-Language header. If the
   * client request doesn't provide an Accept-Language header, this method returns the default locale for the server.
   * 
   * @return The client locale.
   */
  Locale getLocale();

  /**
   * Returns the target URL of the current request. The returned URL contains a protocol, server name, port number, and
   * server path, but it does not include query string parameters.
   * 
   * @return The target URL of the current request.
   * @see HttpServletRequest#getRequestURL()
   */
  String getRequestURL();

  /**
   * Returns an URL pointing to the Aranea container (in most cases the dispatcher servlet) that also contains servlet
   * information. Note that this method is the main (central) method used to retrieve an URL in Aranea. Therefore the
   * implementation plays an important role how client-side references work.
   * 
   * @return The URL pointing to the dispatcher servlet of Aranea. The implementation may decide, if the URL will
   *         contain the protocol and server information or not.
   */
  String getContainerURL();

  /**
   * Returns the part of this request's URL that calls the servlet. This path starts with a "/" character and includes
   * either the servlet name or a path to the servlet, but does not include any extra path information or a query
   * string. In URL, the container path (if provided in the request) follows the context path.
   * 
   * @return A <code>String</code> containing the name or path of the servlet being called, or an empty
   *         <code>String</code>.
   * @since 1.1
   * @see HttpServletRequest#getServletPath()
   */
  String getContainerPath();

  /**
   * Returns an URL pointing to the Aranea container context (in most cases the web application root).
   * 
   * @return A <code>String</code> containing the name or path of the servlet being called, or an empty
   *         <code>String</code>.
   */
  String getContextURL();

  /** @since 1.1 */
  String getContextPath();

  /**
   * Returns the path on the server starting from the dispatcher servlet that has been submitted as the part of the
   * request target URL.
   */
  String getPath();

  /**
   * Returns the path on the server starting from the dispatcher servlet that has been submitted as the part of the
   * request target URL. Unlike {@link #getPath()}, removes all forward-slashes from the beginning of the path.
   * 
   * @return The path without forward-slashes in the beginning.
   * @since 2.0
   */
  String getSimplePath();

  /**
   * Consumes the path prefix (may be nested) allowing children to be mapped to a relative path. The children won't now
   * that a path element was consumed, and to them, the path is like they expect.
   * <p>
   * This is useful for components that listen for requests that match a certain path under the root context. For
   * example, a component that received request for "/a/b" may use <code>pushPathPrefix("a")</code> so that components
   * listening for "/b" could processed as well.
   * 
   * @param pathPrefix The path prefix to consume. Must not contain a forward-slash!
   */
  void pushPathPrefix(String pathPrefix);

  /**
   * Restores the previously consumed path prefix.
   * 
   * @return The restored path element.
   */
  String popPathPrefix();

  /**
   * Provides a way to specify whether the URL returned by {@link #getContainerURL()} starts with the protocol server
   * and server information or just starts with the slash to represent the URL starting with context path.
   * 
   * @param useFullURL A Boolean indicating whether the path should be absolute or relative to the host.
   * @since 1.2.3
   */
  void setUseFullURL(boolean useFullURL);
}
