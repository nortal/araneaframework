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

package org.araneaframework.framework.core;

import java.io.Serializable;

/**
 * Interface to be implemented by the components that wish to be aware of their
 * render state (whether they were actually rendered to the response or not).
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface RenderStateAware extends Serializable {

  /**
   * Returns the render state of implementing class(component).
   * 
   * @return render state of implementing class(component)
   */
  boolean isRendered();

  /**
   * Only use when you really know what you are doing.
   * 
   * @param rendered
   */
  void _setRendered(boolean rendered);
}
