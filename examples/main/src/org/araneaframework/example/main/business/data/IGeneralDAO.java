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

package org.araneaframework.example.main.business.data;

import java.util.List;
import org.araneaframework.example.main.business.model.GeneralMO;

public interface IGeneralDAO<T extends GeneralMO> {

  /**
   * Reads an object with specified class and Id. Returned object can be casted into specified class afterwards.
   * 
   * @param clazz object's class.
   * @param id object's Id.
   * @return object with the specified Id and class.
   */
  public abstract T getById(Class<T> clazz, Long id);

  /**
   * Reads all objects with specified class. Returned objects can be casted into specified class afterwards.
   * 
   * @param clazz objects' class.
   * @return all objects with the specified class.
   */
  public abstract List<T> getAll(Class<T> clazz);

  /**
   * Stores a new object and returns its Id.
   * 
   * @param object object.
   * @return object's Id.
   */
  public abstract Long add(T object);

  /**
   * Stores an existing object.
   * 
   * @param object object.
   */
  public abstract T edit(T object);

  /**
   * Removes an object with specified class and Id.
   * 
   * @param clazz object's class.
   * @param id object's Id.
   */
  public abstract void remove(Class<T> clazz, Long id);
}
