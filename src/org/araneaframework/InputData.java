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
 * A map of maps with specific getters. The current position or scope in the top map
 * is maintained by a path.
 * 
 * <p>
 * InputData has 2 types of getters
 * <ul>
 *  <li>Scoped data - data that depends on the current scope</li>
 *  <li>Global data - data which isn't aware of scoping</li>
 * </ul>
 * Map is used to distinguish data meant for different Components in a Composite. As Components
 * can be structured hierarchically the pushScope and getScope of InputData are used to determine
 * the path of data needed from the InputData.
 * </p>
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface InputData extends Serializable, Extendable, Narrowable {
  
  /**
   * Returns the current Path of this InputData.
   * @return the Path of this InputData
   */
  public Path getScope();

  /**
   * Adds the step to the scope Path of this InputData.
   * @param step is the next step on the Path
   */
  public void pushScope(Object step);

  /**
   * Removes the last entry from the Path of this InputData. 
   */
  public void popScope();

  /**
   * Returns the data with the Path prefix.
   * @return a map with the data
   */
  public Map getScopedData();
  
  /**
   * Returns the global data of this object. Global data is not the same
   * as scoped data with empty path.
   * @return the map with the global data
   */
  public Map getGlobalData();
  
  /**
   * Returns the current OutputData.
   */
  public OutputData getCurrentOutputData();
}
