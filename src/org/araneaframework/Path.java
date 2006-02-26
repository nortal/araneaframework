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

package org.araneaframework;

import java.io.Serializable;

/**
 * Implements <code>Iterator</code> pattern, providing one-time access to the 
 * specific steps that form a path in a hierarchical structure. 
 * <br><br>
 * Path can be used to show the path to a specific component in a composite
 * (hierarchy of components) and then events can be routed exactly to certain
 * components.
 * <br><br>
 * Path is also used in {@link org.araneaframework.InputData} to specify which data is meant for
 * which component. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface Path extends Cloneable, Serializable {
  
  /**
   * Returns next step in the path without changing the current position.  
   * @return the next step in the path
   */
  public Object getNext();
  
  /**
   * Returns the next step in path.
   * @return the next step in the path
   */
  public Object next();
  
  /**
   * Returns true if this path has more elements. 
   * @return true if path has more elements
   */
  public boolean hasNext();
}
