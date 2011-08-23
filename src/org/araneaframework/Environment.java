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
import org.araneaframework.core.exception.NoSuchEnvironmentEntryException;

/**
 * A special data structure providing encapsulation of data needed by different components. Every Aranea component has
 * an environment. The environment can be inherited from the parent or be standalone. No component knows from which
 * component the Environment comes from.
 * <p>
 * Component does know about the hooks in the environment. As different contexts are added to the environment the
 * component in need of them is responsible of checking them and acting upon them.
 * <p>
 * Environment is a also great tool for components defining "contexts" for their child components, since parent
 * components can add new items to (existing) environment and pass on to their children so that only they would see the
 * items.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Environment extends Serializable {

  /**
   * The key that can be used to store or retrieve <tt>Environment</tt> (as an example, a request scope attribute).
   * 
   * @since 1.1
   */
  String ENVIRONMENT_KEY = "org.araneaframework.Environment";

  /**
   * Returns the entry with the specified key from this Environment. Returns <code>null</code> if the entry is not
   * present in the environment.
   * 
   * @param <T> The type of the key class, also the type of the returned object instance.
   * @param key A class used as a key in the environment, must not be <code>null</code>.
   * @return An item found in the environment, or <code>null</code> when the key has no corresponding value in the
   *         environment.
   */
  <T> T getEntry(Class<T> key);

  /**
   * Does the same as {@link #getEntry(Class)}, but throws a {@link NoSuchEnvironmentEntryException} if entry cannot be
   * found.
   * 
   * @param <T> The type of the key class, also the type of the returned object instance.
   * @param key A class used as a key in the environment, must not be <code>null</code>.
   * @return An item found in the environment.
   * @throws NoSuchEnvironmentEntryException If environment entry could not be found.
   */
  <T> T requireEntry(Class<T> key) throws NoSuchEnvironmentEntryException;
}
