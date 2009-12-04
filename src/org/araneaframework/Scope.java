/*
 * Copyright 2006-2007 Webmedia Group Ltd.
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
 * Scope is an abstraction of the <i>location</i> of the component in the
 * hierarchy. Every component has its scope.
 * 
 * @since 1.1
 */
public interface Scope extends Serializable {

  /**
   * Every component has its unique ID compared to other children of its parent.
   * Here is a way to view the ID of the component.
   * 
   * @return the ID of the component.
   */
  Object getId();

  /**
   * Every component has a parent except the root component. Here is a way to
   * view the scope of the parent component.
   * 
   * @return the scope of the parent component.
   */
  Scope getParent();

  /**
   * Builds and returns the full path of the component. The path consists of the
   * IDs of all the components starting with the root flow. For example, if
   * <code>a</code> is the ID of the root flow, and <code>c</code> is the ID
   * of the component whose path we want to know, the path may be like
   * following: <code>a.b.c</code>.
   * 
   * @return the Path of the component.
   */
  Path toPath();

}
