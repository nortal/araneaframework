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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoCurrentInputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.http.HttpOutputData;

/**
 * A implementation of ServletOutputData, MimeOutputData and ServletOverridableOutputData using
 * StandardPath for scoping.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletOutputData implements HttpOutputData {
  private HttpServletRequest req;
  private HttpServletResponse res;
  
  private Map extensions = new HashMap();
  
  /**
   * Constructs a StandardServletOutputData with the request and response. 
   */
  public StandardServletOutputData(HttpServletRequest request, HttpServletResponse response) {
    Assert.notNullParam(request, "request");
    Assert.notNullParam(response, "response");
    
    this.req = request;
    this.res = response;
    
    extend(HttpServletResponse.class, res);
  }

  public void extend(Class interfaceClass, Object implementation) {
    if (HttpServletResponse.class.equals(interfaceClass))
      setResponse((HttpServletResponse) implementation);
    
    extensions.put(interfaceClass, implementation);
  }

  public Object narrow(Class interfaceClass) {
    if (!extensions.containsKey(interfaceClass))
      throw new NoSuchNarrowableException(interfaceClass);
    return extensions.get(interfaceClass);
  }

  public OutputStream getOutputStream() throws IOException {
    return res.getOutputStream();
  }
  
  public void setResponse(HttpServletResponse res) {
    this.res = res;
  }

	public InputData getInputData() {
		InputData inputData = (InputData)req.getAttribute(InputData.INPUT_DATA_KEY);
		if (inputData == null)
			throw new NoCurrentInputDataSetException("No InputData set in the request.");
		else
			return inputData;
	}

  public String encodeURL(String url) {
    return res.encodeURL(url);
  }

  public String encodeRedirectURL(String url) {
    return res.encodeRedirectURL(url);
  }

  public String getCharacterEncoding() {
    return res.getCharacterEncoding();
  }

  public Locale getLocale() {
    return res.getLocale();
  }

  public PrintWriter getWriter() throws IOException {
    return res.getWriter();
  }

  public void sendRedirect(String location) throws IOException {
    res.sendRedirect(location);
  }
  
  public void setContentType(String type) {
    res.setContentType(type);
  }

  public void setCharacterEncoding(String encoding) {
    res.setCharacterEncoding(encoding);
  }
}
