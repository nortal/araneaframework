/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

package org.araneaframework.backend.list.memorybased.expression;

/**
 * General interface for holding a Value.
 */
public interface Value {

  /**
   * Returns the name of the <code>Value</code>.
   * 
   * @return the name of the <code>Value</code>.
   */
  String getName();

  /**
   * Returns the <code>Value</code> as an <code>Object</code>.
   * 
   * @return the <code>Value</code> as an <code>Object</code>.
   */
  Object getValue();
}
