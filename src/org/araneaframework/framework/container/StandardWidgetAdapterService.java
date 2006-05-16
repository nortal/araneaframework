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

package org.araneaframework.framework.container;

import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * Service that contains a widget.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardWidgetAdapterService extends BaseFilterWidget {
  private static final Logger log = Logger.getLogger(StandardWidgetAdapterService.class);
  
  /**
   * If <code>propagateAsAction(InputData)</code> returns true then the action is
   * propagated to the child. Otherwise if the request is the first one then:
   * <ul>
   * <il><code>update(input)</code></il>
   * <il><code>event(path, input)</code></il>
   * <il><code>process()</code></il>
   * </ul> 
   * are called on the child, if not then just <code>render(output).</code>
   */
  protected void action(Path path, InputData input, OutputData output) throws Exception {
   if (propagateAsAction(input)) {
     log.debug("Calling widget action().");
     childWidget._getService().action(path, input, output);
   }
   else {
     log.debug("Translating action() call to widget update()/event()/process()/render() calls.");
     
     childWidget._getWidget().update(input);
     childWidget._getWidget().event(path, input);       
     childWidget._getWidget().process();
     childWidget._getWidget().render(output);
   }
  }

  /**
   * If returns true then propagates the action to the child widget. By default returns false.
   */
  public boolean propagateAsAction(InputData input) {
    return false;
  }
}
