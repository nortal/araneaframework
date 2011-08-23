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
 * Widget is a service with enhanced input and output data processing. It divides input-output processing into "legacy"
 * action-handling and new event handling, which consists of three phases in that particular order:
 * <ol>
 * <li>updating (<code>update(InputData)</code>)
 * <li>(targeted) event handling (<code>event(Path, InputData)</code>)
 * <li>rendering (<code>render(OutputData)</code>)
 * </ol>
 * <p>
 * Note that event handling takes place only when the widget was asked to perform a specific event, while updating and
 * rendering should take place on all widgets in active hierarchy.
 * <p>
 * <tt>Widget</tt> follows the template pattern by defining <code>_getWidget()</code> which returns the implementation.
 * The implementation is used for managing the widget input-output data processing. The <tt>Widget</tt> contract itself
 * does not expose direct input-output data processing methods, since they are protected and handled by implementations.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Widget extends Service, Serializable {

  /**
   * The factory method returning the implementation of the Widget.
   * 
   * @return the implementation of the Widget.
   */
  Interface _getWidget();

  /**
   * The interface which takes care of calling the hooks in the <tt>Widget</tt> template design pattern.
   * 
   * @see Widget
   */
  interface Interface extends Serializable {

    /**
     * Initiates input data processing and gives the widget the chance to update itself.
     * 
     * @param input The input data for the widget (never <code>null</code>).
     */
    void update(InputData input);

    /**
     * Initiates event processing during which the target event receiver will perform its event handling.
     * 
     * @param path Path to (child) widget to whom the event is addressed. Empty path or <code>null</code> means that the
     *          underlying widget will process the event. When a path item (widget) is not found, event may be dumped.
     * @param input The input data for the widget (never <code>null</code>).
     */
    void event(Path path, InputData input);

    /**
     * Initiates widget state rendering after input data has been processed. The rendering method is not specified here,
     * and is not restricted to anything. However, the provided output data could be used for returning or storing the
     * rendered state.
     * 
     * @param output The output data (never <code>null</code>) for the widget, can be used for storing rendering
     *          results.
     */
    void render(OutputData output);
  }
}
