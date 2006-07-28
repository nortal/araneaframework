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

package org.araneaframework.servlet.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 * 
 */
public class URLUtil {
  /**
   * Removes all leading and trailing slashes.
   */
  public static String normalizeURI(String uri) {
    if (uri == null) 
      return null;
    
    // lose the first slashes
    while (uri.indexOf("/") == 0 && uri.length() > 0)
      uri = uri.substring(1);

    // lose the first slashes
    while (uri.lastIndexOf("/") == (uri.length() - 1) && uri.length() > 0)
      uri = uri.substring(0, uri.length() - 1);

    return uri;
  }

  public static String[] splitURI(String uri) {
    List result = new ArrayList();
    uri = normalizeURI(uri);

    StringTokenizer tokenizer = new StringTokenizer(uri, "/");
    while (tokenizer.hasMoreTokens()) {
      result.add(tokenizer.nextToken());
    }

    return (String[]) result.toArray(new String[result.size()]);
  }
}
