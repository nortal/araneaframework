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

import java.util.Map;
import org.araneaframework.http.HttpInputData;

/**
 * Provides access to request parameters.
 * 
 * <p>
 * InputData has 2 types of getters
 * <ul>
 *  <li>Scoped data - data that depends on the current scope</li>
 *  <li>Global data - data which isn't aware of scoping</li>
 * </ul>
 * </p>
 * 
 * @see HttpInputData
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface InputData extends Extendable, Narrowable {
  
  /**
   * The key that can be used to retrieve InputData (as an example a request scope attribute).
   */
  public static final String INPUT_DATA_KEY = "org.araneaframework.InputData";
  
  /**
   * Returns the data with the Path prefix.
   * @param scope the Path prefix
   * @return a map with the data
   */
  public Map getScopedData(Path scope);
  
  /**
   * Returns the global data of this object. Global data is not the same
   * as scoped data with empty path.
   * @return the map with the global data
   */
  public Map getGlobalData();
  
  /**
   * Returns the current OutputData.
   */
  public OutputData getOutputData();
}
