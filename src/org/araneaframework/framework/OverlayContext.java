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

package org.araneaframework.framework;

import java.util.Map;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Widget;
import org.araneaframework.framework.FlowContext.Configurator;
import org.araneaframework.framework.FlowContext.Handler;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public interface OverlayContext {
  boolean isOverlayActive();
  
  void setOverlayOptions(Map options);
  Map getOverlayOptions();

  /** FlowContext delegate methods */
  void replace(Widget flow, Configurator configurator);
  void replace(Widget flow);
  void reset(EnvironmentAwareCallback callback);
  void start(Widget flow, Configurator configurator, Handler handler);
  void start(Widget flow, Handler handler);
  void start(Widget flow);
}
