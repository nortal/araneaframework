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

package org.araneaframework.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardPath;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockOutputData implements OutputData {
  private Map data;
  private List<Object> pathPrefix;
  
  public MockOutputData(Map data) {
    this();
    this.data = data;
  }
  
  public MockOutputData() {
    data = new HashMap();
    pathPrefix = new ArrayList<Object>();
  }
  
  public Path getScope() {
    return new StandardPath(getScopePathString());
  }

  public Object getAttribute(Object key) {
    //XXX
    return getScopedData().get(key);
  }

  public Map getAttributes() {
    //XXX
    return Collections.unmodifiableMap(getScopedData());
  }

  public void pushScope(Object step) {
    pathPrefix.add(step);
  }

  public void popScope() {
    pathPrefix.remove(pathPrefix.size()-1);
  }
  
  @Override
  public String toString() {
    return getScopePathString();
  }
  
  /*
  ** PRIVATE METHODS  
  */
  private Map getScopedData() {
    String path = getScopePathString();
    if (data.get(path) == null) {
      return new HashMap();
    } 
    else {
      return (Map)data.get(path);
    }
  }
  
  private String getScopePathString() {
    StringBuffer result = new StringBuffer();
    Iterator<Object> ite = pathPrefix.iterator();
    while (ite.hasNext()) {
      result.append(ite.next()+".");
    }
    if (result.length()>0) {
      result = new StringBuffer(result.substring(0, result.length()-1));
    }
    return result.toString();
  }

  public void extend(Class interfaceClass, Object implementation) {
    //XXX
    throw new NotImplementedException();    
  }

  public Object narrow(Class interfaceClass) {
    //XXX
    throw new NotImplementedException();
  }

  public void pushAttribute(Object key, Object value) {
    //XXX
  }

  public Object popAttribute(Object key) {
    //XXX
    return null;
  }

  public void restoreScope(Path scope) {
    //XXX
  }

	public InputData getInputData() {
		return null;
	}
}
