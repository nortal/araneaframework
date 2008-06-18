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

/**
 * Provides objects with the capability of narrowing down to a specific implementation
 * of an interface added with extend from {@link org.araneaframework.Extendable}.
 * 
 * @see org.araneaframework.Extendable
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface Narrowable {
  
  /**
   * Narrows down to the implementation of the interfaceClass, returns
   * the object. Can be used after the <code>extend(Class, Object);</code>
   * if the object implements {@link Extendable}.
   *  
   * @param interfaceClass the interface of the implementation in need of
   * @return the implementation of the interfaceClass
   */
  public <T> T narrow(Class<T> interfaceClass);
}
