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
 * Provides methods to deal with the manipulate low-level HTTP constructs. Wraps the {@link HttpServletRequest}.
 * <p>
 * The main difference is how we handle the so called PATH_INFO (the additional path after the servlet). The problem is
 * that unlike most common cases Aranea components form a hierarchy. Therefore if a parent is mapped to path prefix
 * "myPath/*" and its child is mapped to a path prefix "myChildPath/*" if the path handling were absolute the child
 * would never get the mapped calles. This is due to the child being really mapped to the path "myPath/myChildPath".
 * Therefore the parent must consume the prefix "myPath/" using method <code>pushPathPrefix()</code> and then the child
 * will be correctly matched to the relative path "myChildPath".
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * @see HttpOutputData
 */
public interface HttpInputData extends InputData {

  /**
   * Returns an iterator over names of the parameters submitted with the current request.
   */
  Iterator getParameterNames();

  /**
   * Returns the array of values of the particular parameter submitted with the current request.
   */
  String[] getParameterValues(String name);

  /**
   * Returns the character encoding that is used to decode the request parameters.
   */
  String getCharacterEncoding();

  /**
   * Sets the character encoding that is used to decode the request parameters. Note that this must be called before any
   * parameters are read according to the Servlet specification.
   */
  void setCharacterEncoding(String encoding);

  /**
   * Returns the MIME content type of the request body or <code>null</code> if the body is lacking.
   */
  String getContentType();

  /**
   * Returns the preferred Locale that the client will accept content in, based on the Accept-Language header. If the
   * client request doesn't provide an Accept-Language header, this method returns the default locale for the server.
   */
  Locale getLocale();

  /**
   * Returns the target URL of the current request.
   */
  String getRequestURL();

  /**
   * Returns an URL pointing to the Aranea container (in most cases the dispatcher servlet).
   */
  String getContainerURL();

  /** @since 1.1 */
  String getContainerPath();

  /**
   * Returns an URL pointing to the Aranea container context (in most cases the web application root).
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
   * Consumes the path prefix allowing children to be mapped to a relative path.
   */
  void pushPathPrefix(String pathPrefix);

  /**
   * Restores the previously consumed path prefix.
   */
  void popPathPrefix();

  /**
   * Provides a way to specify whether the URL returned by {@link #getContainerURL()} starts with the protocol server
   * and server information or just starts with the slash to represent the URL starting with context path.
   * 
   * @param useFullURL A Boolean indicating whether the path should be absolute or relative to the host.
   * @since 1.2.3
   */
  void setUseFullURL(boolean useFullURL);

}
