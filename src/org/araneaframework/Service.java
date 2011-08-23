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

import java.io.Serializable;

/**
 * <code>Service</code> is a component that can perform some action. As requests flow through the <code>action</code>
 * they can be filtered, logged, enhanced via extending the <code>InputData</code>, provide custom routing, etc.
 * <p>
 * All the filters and routers in the Aranea framework are services.
 * <p>
 * <tt>Service</tt> follows the template pattern by defining <code>_getService()</code> which returns the
 * implementation. The implementation is used for managing the service actions. The <tt>Service</tt> contract itself
 * does not expose direct action handling methods, since they are protected and handled by implementations.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Service extends Component {

  /**
   * The factory method returning the implementation of the Service.
   * 
   * @return the implementation of the Service.
   */
  Interface _getService();

  /**
   * The interface which takes care of calling the hooks in the <tt>Service</tt> template design pattern.
   * 
   * @see Service
   */
  interface Interface extends Serializable {

    /**
     * Action handling method for services.
     * 
     * @param path The path of the component to whom the action is targeted (<code>null</code> is also valid).
     * @param input Input data for the service or for its child components.
     * @param output Output data for the service or for its child components.
     */
    void action(Path path, InputData input, OutputData output);
  }

}
