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
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardPath;

/**
 * @author toomas
 *
 */
public class MockInputData implements InputData {
  private Map data;
  private List pathPrefix;
  
  public MockInputData(Map data) {
    this();
    this.data = data;
  }
  
  public MockInputData() {
    pathPrefix = new ArrayList();
    data = new HashMap();
  }
  
  public Path getScope() {
    return new StandardPath(getScopePathString());
  }

  public Map getScopedData() {
    String path = getScopePathString();
    /*System.out.println("getScopedData");
    System.out.println("path is "+path);
    System.out.println(data);*/
    if (data.get(path) == null) {
      //System.out.println("Returning null");
      return Collections.unmodifiableMap(new HashMap());
    } 
    else {
      //System.out.println("Returning "+data.get(path));
      return Collections.unmodifiableMap((Map)data.get(path));
    }
  }

  public void pushScope(Object step) {
    pathPrefix.add(step);
  }

  public void popScope() {
    pathPrefix.remove(pathPrefix.size()-1);
  }
  
  private String getScopePathString() {
    StringBuffer result = new StringBuffer();
    Iterator ite = pathPrefix.iterator();
    while (ite.hasNext()) {
      result.append(ite.next()+".");
    }
    if (result.length()>0) {
      result = new StringBuffer(result.substring(0, result.length()-1));
    }
    return result.toString();
  }

  public String toString() {
    return getScopePathString();
  }

  public void extend(Class interfaceClass, Object implementation) {
  }

  public Object narrow(Class interfaceClass) {
    return null;
  }

  public Map getGlobalData() {
    return data;
  }

	public OutputData getCurrentOutputData() {
		return null;
	}
}

