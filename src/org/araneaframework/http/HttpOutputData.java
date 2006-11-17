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

package org.araneaframework.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.OutputData;

/**
 * Provides methods to deal with the manipulate low-level HTTP constructs. Wraps the {@link HttpServletResponse}.
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 * @see HttpInputData
 */
public interface HttpOutputData extends OutputData {
  /**
   * Encodes the URL to include some additional information (e.g. HTTP session identifier). 
   * Note that Aranea may include some information not present in the servlet spec.
   */
  String encodeURL(String url);
  
  /**
   * Sends an HTTP redirect to a specified location URL.
   */
  void sendRedirect(String location) throws IOException;
  
  /**
   * Returns an <code>OutputStream</code> that can be used to write to response. 
   * Note that unlike the Servlet specification, Aranea permits to use stream and writer interchangeably.
   */
  OutputStream getOutputStream() throws IOException;
  /**
   * Returns a <code>PrintWriter</code> that can be used to write to response. 
   * Note that unlike the Servlet specification, Aranea permits to use stream and writer interchangeably.
   */
  PrintWriter getWriter() throws IOException;
  
  /**
   * Sets the MIME content type of the output. May include the charset, e.g. "text/html; charset=UTF-8".
   */
  void setContentType(String type); 
  
  /**
   * Returns the character encoding used to write out the response.
   */
  String getCharacterEncoding();
  
  /**
   * Sets the character encoding used to write out the response.
   */
  void setCharacterEncoding(String encoding);
  
  /**
   * Returns the locale associated with the response.
   */
  Locale getLocale();
}
