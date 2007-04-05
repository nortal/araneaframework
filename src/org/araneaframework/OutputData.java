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


/**
 * A map of attributes with a scope. As OutputData is passed to different methods
 * of BaseApplicationComponent and derivates, the scope helps the components understand
 * where they are located in the greater hirearchy. 
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface OutputData extends Extendable, Narrowable {
  
  /**
   * The key that can be used to retrieve OutputData (as an example a request scope attribute).
   */
  public static final String OUTPUT_DATA_KEY = "org.araneaframework.OutputData";
  
  /**
   * Returns the current InputData.
   */
  public InputData getInputData();
}
