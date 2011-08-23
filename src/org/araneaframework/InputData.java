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

package org.araneaframework;

import java.util.Map;

/**
 * Input data contract for Aranea components. Provides access to request parameters. Components use {@link OutputData}
 * to return or store the response.
 * <p>
 * <tt>InputData</tt> divides its data into 2 groups and thus provides 2 getters for accessing them:
 * <ul>
 * <li>Scoped data - data that is {@link Scope}d and thus can be described as a {@link Path};
 * <li>Global data - data which isn't aware of scoping.
 * </ul>
 * 
 * @see OutputData
 * @see org.araneaframework.http.HttpInputData
 * @see Scope
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface InputData extends Extendable, Narrowable {

  /**
   * The key that can be used to store or retrieve <tt>InputData</tt> (as an example, a request scope attribute).
   */
  String INPUT_DATA_KEY = "org.araneaframework.InputData";

  /**
   * Provides the scoped input data with exact given path prefix.
   * 
   * @param scope The exact path prefix for scoped input data.
   * @return A map with the scoped data, or an empty map when no such scoped data is provided.
   */
  Map<String, String> getScopedData(Path scope);

  /**
   * Provides global input data. Global data excludes scoped data.
   * 
   * @return the map with the global data
   */
  Map<String, String> getGlobalData();

  /**
   * Provides the associated output data where response can be stored.
   * 
   * @return Associated output data, which is never <code>null</code>.
   */
  OutputData getOutputData();
}
