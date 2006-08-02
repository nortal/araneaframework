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
 * Service is component with an extra method <code>action(Path, InputData, OutputData)</code> 
 * provide services based on the requests. As requests flow through the <code>action</code> they
 * can be filtered, logged, enhanced via extending the InputData, provide custom routing etc.
 * <br><br>
 * All the filters and routers in the Aranea framework are services.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface Service extends Component, Serializable {
  public Interface _getService();
  
  public interface Interface extends Serializable {
    public void action(Path path, InputData input, OutputData output);
  }
}
