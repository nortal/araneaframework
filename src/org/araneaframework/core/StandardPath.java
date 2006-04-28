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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import org.araneaframework.Path;

/**
 * Path is represented as a dot-separated String. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardPath implements Path {
  private ListIterator path;
  
  /**
   * Constructs a path from the fullPath. Expects fullPath to be a dot-separated String.
   * @param fullPath
   */
  public StandardPath(String fullPath) {
    List list = new ArrayList();
    StringTokenizer tokenizer = new StringTokenizer(fullPath, ".");
    
    while (tokenizer.hasMoreElements()) {
      list.add(tokenizer.nextElement());
    }
    this.path = list.listIterator();
  }
  

  /**
   * @see org.araneaframework.Path#getNext()
   */
  public Object getNext() {
  	Object result = path.next();
  	path.previous();
  	return result;
  }

  /**
   * @see org.araneaframework.Path#next()
   */
  public Object next() {
    return path.next();
  }

  /**
   * @see org.araneaframework.Path#hasNext()
   */
  public boolean hasNext() {
    return path.hasNext();
  }
  
  /**
   * Returns a dot-separated String of the Path.
   */
  public String toString() {
    StringBuffer result = new StringBuffer();
    int counter = 0;
    while(hasNext()) {
      result.append((String)next()+".");
      counter++;
    }
    if (result.length()>0 && result.charAt(result.length()-1)=='.') {
      result = new StringBuffer(result.substring(0, result.length()-1));
    }
    for (int i=0;i<counter;i++) {
      path.previous();
    }
    
    return result.toString();
  }
}
