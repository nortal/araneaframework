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
import java.util.Map;

/**
 * A map of attributes with a scope. As OutputData is passed to different methods
 * of BaseApplicationComponent and derivates, the scope helps the components understand
 * where they are located in the greater hirearchy. 
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface OutputData extends Serializable, Extendable, Narrowable {
  
  /**
   * The key that can be used to retrieve OutputData (as an example a request scope attribute).
   */
  public static final String OUTPUT_DATA_KEY = "org.araneaframework.OutputData";

  /**
   * Adds the step to the scope Path of this OutputData.
   * @param step is the next step on the Path
   */
  public void pushScope(Object step);
  
  /**
   * Removes the last entry from the Path of this OutputData .
   */
  public void popScope();
  
  /**
  * Returns the current Path of this InputData.
  * @return the Path of this InputData
  */
  public Path getScope();
  
  /**
   * Restores the scope to the passed one.
   */
  public void restoreScope(Path scope);

  /**
   * Pushes the specified value to the stack with the specified key.
   * 
   * @param key the key of the value being added
   * @param value the value to be added
   */
  public void pushAttribute(Object key, Object value);
  
  /**
   * Pops the attribute with the specified key that is top on the stack.
   * @param key the key of the attribute
   * @return the attribute under the key
   */
  public Object popAttribute(Object key);
  
  /**
   * Returns the attribute with specified key that is top on the stack.
   * @param key of the attribute
   * @return the attribute under the key
   */
  public Object getAttribute(Object key);
  
  /**
   * Returns a map of all the attributes in this OutputData that are
   * currently on the top of the stack.
   * @return a map of attributes.
   */
  public Map getAttributes();
  
  /**
   * Returns the current InputData.
   */
  public InputData getInputData();
}
