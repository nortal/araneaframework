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

import java.io.Serializable;
import org.araneaframework.core.NoSuchEnvironmentEntryException;

/**
 * A special data structure providing encapsulation of data needed by different components. 
 * Every Aranea component has an environment. The environment can be inherited from the parent
 * or be stand-alone. No component knows from which component the Environment comes from.
 * <p>
 * Component does know about the hooks in the environment. As different contexts are added to
 * the environment the component in need of them is responsible of checking them and acting upon
 * them. 
 * <p>
 * Since version XXX of Aranea there are some restrictions of the content of the environments: the keys
 * should be class literals and the values should be the instances of the key's class. So each
 * implementation of this interface should enforce that restriction.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 */
public interface Environment extends Serializable {
  
  /**
   * The key that can be used to retrieve Environment (as an example a request scope attribute).
   * 
   * @since 1.1
   */
  public static final String ENVIRONMENT_KEY = "org.araneaframework.Environment";
  
  /**
   * Returns the entry with the specified key from this Environment. 
   * Returns null if the entry is not present in the environment.
   */
  public <T> T getEntry(Class<T> clazz);
  
  /**
   * Does the same as {@link #getEntry(Class)}, but throws a {@link NoSuchEnvironmentEntryException} if 
   * entry cannot be found. 
   * 
   * @throws NoSuchEnvironmentEntryException If environment entry could not be found.
   */
  public <T> T requireEntry(Class<T> key) throws NoSuchEnvironmentEntryException;
}
