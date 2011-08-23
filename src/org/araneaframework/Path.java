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
 * Implements iterator design pattern, providing one-time access to the specific steps that form a path in a
 * hierarchical structure, starting from the beginning.
 * <p>
 * Path can be used to show the path to a specific component in a composite (hierarchy of components) and then events
 * can be routed exactly to certain components identified by path.
 * <p>
 * Path is also used in {@link org.araneaframework.InputData} to specify scoped data.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public interface Path extends Cloneable, Serializable {

  /**
   * The constant representing path items separator.
   * 
   * @since 2.0
   */
  String SEPARATOR = ".";

  /**
   * Provides the next path item in the path without changing the current position. May throw an exception, when there
   * are no more path items available.
   * 
   * @return The next path item in the path.
   */
  String getNext();

  /**
   * Provides the next path item in the path, moving the current position in the path one step forward. May throw an
   * exception, when there are no more path items available.
   * 
   * @return The next path item in the path.
   */
  String next();

  /**
   * Provides whether there are more path items available to iterate over them.
   * 
   * @return A Boolean that is <code>true</code>, when the underlying path has more elements.
   */
  boolean hasNext();
}
