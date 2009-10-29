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

package org.araneaframework.uilib.list;

import java.util.Comparator;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.util.Event;

/**
 * List context. General interface that can be used to access the current list configuration.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * 
 * @see ConfigurationContext
 * @see ListWidget
 */
public interface ListContext {

  /**
   * Returns the type of this field.
   * 
   * @param fieldId field id.
   * @return the type of this field.
   */
  Class<?> getFieldType(String fieldId);

  /**
   * Returns the current case sensitivity behavior.
   * 
   * @return the current case sensitivity behavior.
   */
  boolean isIgnoreCase();

  /**
   * Run an event after the initialization.
   * 
   * @param event an event.
   */
  void addInitEvent(Event event);

  /**
   * Returns the label of this field.
   * 
   * @param fieldId field id.
   * @return the label of this field.
   */
  String getFieldLabel(String fieldId);

  /**
   * Returns the {@link Comparator} of this field.
   * 
   * @param fieldId field id.
   * @return the comparator of this field.
   */
  @SuppressWarnings("unchecked")
  Comparator getFieldComparator(String fieldId);
}
