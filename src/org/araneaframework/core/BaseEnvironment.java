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

package org.araneaframework.core;

import org.araneaframework.Environment;

/**
 * Base {@link org.araneaframework.Environment} with <code>requireEntry</code>
 * implementation.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BaseEnvironment implements Environment {

  public <T> T requireEntry(Class<T> key) throws NoSuchEnvironmentEntryException {

    Assert.notNullParam(this, key, "key");

    T result = getEntry(key);

    if (result == null) {
      throw new NoSuchEnvironmentEntryException(key);
    }

    return result;
  }
}
