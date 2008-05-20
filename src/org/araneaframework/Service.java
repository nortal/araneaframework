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
 */

package org.araneaframework;

import java.io.Serializable;

/**
 * <code>Service</code> is a component with an extra method
 * <code>action(Path, InputData, OutputData)</code> that provides services
 * based on the requests. As requests flow through the <code>action</code>
 * they can be filtered, logged, enhanced via extending the
 * <code>InputData</code>, provide custom routing etc.
 * <p>
 * All the filters and routers in the Aranea framework are services.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Service extends Component, Serializable {

  /**
   * Provides access to the request handler.
   * 
   * @return An implementation of the interface that handles requests.
   */
  public Interface _getService();

  /**
   * Defines the interface for handling requests.
   */
  public interface Interface extends Serializable {

    /**
     * Request handling method for services.
     * 
     * @param path The path of the component to whom the action is targeted.
     * @param input The request data.
     * @param output The response data.
     */
    public void action(Path path, InputData input, OutputData output);
  }

}
