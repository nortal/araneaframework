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
 * Widget is a component with a graphical representation (via <code>render(OutputData)</code>),
 * event handling (via <code>event(Path, InputData</code>).
 * <br><br>
 * As every Widget has a lifecycle of a Component, it also has a request cycle. Request cycle
 * starts with <code>update(InputData)</code> which gives the Widget the data from the request
 * as InputData.
 * <br><br>
 * If an event is routed to this widget, <code>event(Path, InputData)</code> is invoked.
 * <br>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Widget extends Service, Serializable {
  public Interface _getWidget();
  
  public interface Interface extends Serializable {
    
    /**
     * Widget starts its request cycle. <code>update(InputData)</code> gives the
     * widget the chance to update itself with the current InputData and do the necessary
     * updates. 
     */
    public void update(InputData data);
    
    /**
     * Widget received an event. 
     * @param path
     * @param input
     * @throws Exception
     */
    public void event(Path path, InputData input);

    /**
     * Widget outputs its graphical representation to OutputData. This method is
     * idempotent and thus can be called mupltiple times.
     */
    public void render(OutputData output) throws Exception;
  } 
}
