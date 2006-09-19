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
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.NoCurrentInputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.StandardPath;
import org.araneaframework.http.HttpOutputData;

/**
 * A implementation of ServletOutputData, MimeOutputData and ServletOverridableOutputData using
 * StandardPath for scoping.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletOutputData implements HttpOutputData {
  private transient HttpServletRequest req;
  private transient HttpServletResponse res;
  
  private StringBuffer scopeBuf = new StringBuffer();
  
  private Map extensions = new HashMap();
  private Map attributes = new HashMap();
  private Map currentTopAttributes = new HashMap();
  
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

  public Path getScope() {
    return new StandardPath(scopeBuf.toString());
  }

  public void pushScope(Object step) {
    Assert.isInstanceOfParam(String.class, step, "step");
    Assert.notEmptyParam((String) step, "step");
    
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
      scopeBuf.setLength(0);
    }
  }
  
  public void restoreScope(Path scope) {
    Assert.notNullParam(scope, "scope");
    
    scopeBuf = new StringBuffer(scope.toString());
  }

  public void pushAttribute(Object key, Object value) {
    LinkedList stack = (LinkedList) attributes.get(key);
    
    if (stack == null) {
      stack = new LinkedList();
      attributes.put(key, stack); 
    }
    
    stack.addFirst(value);
    currentTopAttributes.put(key, value);
  }
  
  public Object popAttribute(Object key) {
    LinkedList stack = (LinkedList) attributes.get(key);
    currentTopAttributes.remove(key);
    
    
    if (stack == null || stack.size() == 0) {
      throw new EmptyStackException();
    }
    
    Object result = stack.removeFirst();
    if (stack.size() > 0)
      currentTopAttributes.put(key, stack.getFirst());
    return result;
  }

  public Object getAttribute(Object key) {
    LinkedList stack = (LinkedList) attributes.get(key);
    
    if (stack == null || stack.size() == 0)
      return null;
    
    return stack.getFirst();
  }

  public Map getAttributes() {    
    return Collections.unmodifiableMap(currentTopAttributes);
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
