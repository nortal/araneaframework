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

package org.araneaframework.mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;

/**
 * @author toomas
 *
 */
public class MockInputData implements InputData {
  private Map<String, String> data;
  
  public MockInputData(Map<String, String> data) {
    this();
    this.data = data;
  }
  
  public MockInputData() {
    data = new HashMap<String, String>();
  }

  public Map<String, String> getScopedData(Path path) {
    /*System.out.println("getScopedData");
    System.out.println("path is "+path);
    System.out.println(data);*/
    if (data.get(path.toString()) == null) {
      //System.out.println("Returning null");
      return Collections.unmodifiableMap(new HashMap<String, String>());
    } 
    else {
      //System.out.println("Returning "+data.get(path));
      return Collections.unmodifiableMap(data);
    }
  }

  public <T> void extend(Class<T> interfaceClass, T implementation) {
    //XXX
    throw new NotImplementedException();    
  }

  public <T> T narrow(Class<T> interfaceClass) {
    //XXX
    throw new NotImplementedException();
  }

  public Map<String, String> getGlobalData() {
    return data;
  }

	public OutputData getOutputData() {
    //XXX
    throw new NotImplementedException();
	}
}

