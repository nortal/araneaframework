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
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.core.NoCurrentInputDataSetException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.ContinuationManagerContext;
import org.araneaframework.http.MimeOutputData;
import org.araneaframework.http.ServletOutputData;
import org.araneaframework.http.ServletOverridableOutputData;

/**
 * A implementation of ServletOutputData, MimeOutputData and ServletOverridableOutputData using
 * StandardPath for scoping.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletOutputData implements ServletOutputData, MimeOutputData, ServletOverridableOutputData {
  private HttpServletRequest req;
  private HttpServletResponse res;
  
  private StringBuffer scopeBuf = new StringBuffer();
  
  private Map extensions = new HashMap();
  private Map attributes = new HashMap();
  private Map currentTopAttributes = new HashMap();
  
  /**
   * Constructs a StandardServletOutputData with the request and response. 
   */
  public StandardServletOutputData(HttpServletRequest request, HttpServletResponse response) {
    this.req = request;
    this.res = response;
  }
  
  public HttpServletRequest getRequest() {
    return this.req;
  }

  public HttpServletResponse getResponse() {
    return this.res;
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
      scopeBuf.setLength(0);
    }
  }
  
  public void restoreScope(Path scope) {
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
    extensions.put(interfaceClass, implementation);
  }

  public Object narrow(Class interfaceClass) {
    if (!extensions.containsKey(interfaceClass))
      throw new NoSuchNarrowableException(interfaceClass);
    return extensions.get(interfaceClass);
  }

  public void setMimeType(String type) {
    getResponse().setContentType(type);
  }

  public OutputStream getOutputStream() throws IOException {
    return getResponse().getOutputStream();
  }
  
  public void redirect(Environment environment, final String url) throws Exception {
  	ContinuationManagerContext continuationHandler = 
  		(ContinuationManagerContext) environment.getEntry(ContinuationManagerContext.class);  
  	
		// setting the continuation
		continuationHandler.runOnce(new BaseService() {
			protected void action(Path path, InputData input, OutputData output) throws Exception {
				((ServletOutputData)output).getResponse().sendRedirect(url);
			}
		});
  }

  public void setRequest(HttpServletRequest req) {
    this.req = req;
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
}
