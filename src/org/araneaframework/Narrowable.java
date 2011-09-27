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

/**
 * Provides objects with the capability of narrowing down to a specific implementation of an interface added with extend
 * from {@link Extendable}.
 * 
 * @see Extendable
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Narrowable {

  /**
   * Narrows down to the implementation of the interfaceClass and returns that object. Can be used after calling
   * <code>extend(Class, Object)</code> on this instance, if it implements {@link Extendable}.
   * <p>
   * Throws <tt>NoSuchNarrowableException</tt> when the <tt>interfaceClass</tt> has not been extended beforehand on this
   * instance of implementation.
   * 
   * @param <T> The type of the interface class, also the type of the returned object instance.
   * @param interfaceClass The interface of the implementation that needs to be returned.
   * @return The implementation of the <tt>interfaceClass</tt>.
   * @see org.araneaframework.core.exception.NoSuchNarrowableException
   */
  <T> T narrow(Class<T> interfaceClass);
}
