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

import java.io.Serializable;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface ViewPortContext extends Serializable {
  /**
   * The view port widget key in the output attributes.
   */
  public static final String VIEW_PORT_WIDGET_KEY = "org.araneaframework.framework.ViewPortContext.WIDGET_KEY";
}
