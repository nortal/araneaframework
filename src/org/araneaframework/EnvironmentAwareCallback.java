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
 * Callbacks that are <code>Environment</code> aware.
 * <p>
 * For example, the call may use the flow context from the
 * <code>Environment</code> to start a new flow using the
 * <code>childEnvironment</code> of the flow context.
 */
public interface EnvironmentAwareCallback extends Serializable {

  /**
   * This method is expected to use the given <code>Environment</code> to
   * start a new flow or use it for whatever is necessary.
   * 
   * @param env the <code>Environment</code> of the flow context.
   * @throws Exception Any runtime exception that may occur.
   */
  public void call(Environment env) throws Exception;

}
