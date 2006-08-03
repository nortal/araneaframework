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

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Environment;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardPath;
import org.araneaframework.http.core.StandardServletInputData;
import org.araneaframework.http.core.StandardServletOutputData;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockUtil {  
  public static StandardServletInputData getInput() {
    HttpServletRequest req = new MockHttpServletRequest();
    return new StandardServletInputData(req);
  }
  
  public static StandardServletOutputData getOutput() {
    HttpServletRequest req = new MockHttpServletRequest();
    HttpServletResponse res = new MockHttpServletResponse();
    return new StandardServletOutputData(req, res);
  }
  
  public static Path getPath() {
    return new StandardPath("");
  }
  
  public static Path getPath(String str) {
    return new StandardPath(str);
  }
  
  public static Environment getEnv() {
    return new StandardEnvironment(null, new HashMap());
  }
}
