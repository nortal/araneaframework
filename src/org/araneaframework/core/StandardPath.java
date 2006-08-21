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

package org.araneaframework.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;
import org.araneaframework.Path;

/**
 * Path is represented as a dot-separated String. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardPath implements Path {
  private LinkedList path = new LinkedList();
  
  /**
   * Constructs a path from the fullPath. Expects fullPath to be a dot-separated String.
   * @param fullPath
   */
  public StandardPath(String fullPath) {
    Assert.notNull(fullPath, "Path cannot be null!");
    
    StringTokenizer tokenizer = new StringTokenizer(fullPath, ".");
    
    while (tokenizer.hasMoreElements()) {
      path.add(tokenizer.nextElement());
    }
  }
  

  /**
   * @see org.araneaframework.Path#getNext()
   */
  public Object getNext() {
  	return path.getFirst();
  }

  /**
   * @see org.araneaframework.Path#next()
   */
  public Object next() {
    return path.removeFirst();
  }

  /**
   * @see org.araneaframework.Path#hasNext()
   */
  public boolean hasNext() {
    return path.size() > 0;
  }
  
  /**
   * Returns a dot-separated String of the Path.
   */
  public String toString() {
    StringBuffer result = new StringBuffer();

    for (Iterator i = path.iterator(); i.hasNext();) {  
      result.append((String) i.next());
      if (i.hasNext())
        result.append('.');
    }

    return result.toString();
  }
}
