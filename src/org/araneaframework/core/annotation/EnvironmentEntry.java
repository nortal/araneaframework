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

package org.araneaframework.core.annotation;

import java.lang.annotation.Documented;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.araneaframework.Environment;

import java.lang.annotation.ElementType;

import java.lang.annotation.Target;

/**
 * A field-level annotation for setting the appropriate environment entry. The entry key is by default the field type,
 * or the class as specified in {@link #value()}.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnvironmentEntry {

  /**
   * An optional parameter to the {@link Environment} entry lookup. Uses the given class as the environment entry lookup
   * key. By default, it can use the field type.
   * 
   * @return The key to use for environment entry lookup
   */
  Class<?> value();

  /**
   * Specifies whether the environment entry is required. By default, it is not required.
   * 
   * @return A <code>Boolean</code> indicating whether the field is required.
   */
  boolean required();
}
