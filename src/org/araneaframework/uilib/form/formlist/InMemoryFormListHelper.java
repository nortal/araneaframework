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

package org.araneaframework.uilib.form.formlist;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.araneaframework.uilib.form.formlist.adapter.InMemoryFormRowHandlerDecorator;
import org.araneaframework.uilib.form.formlist.model.MapFormListModel;

/**
 * Helper that facilitates holding the editable list rows in memory without saving them to database. Useful when the
 * editable list should be persisted only in the very end of user session.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class InMemoryFormListHelper<K, R> implements Serializable {

  protected Map<K, R> added = new HashMap<K, R>();

  protected Map<K, R> updated = new HashMap<K, R>();

  protected Set<K> deleted = new HashSet<K>();

  protected Map<K, R> current = new LinkedHashMap<K, R>();

  protected BaseFormListWidget<K, R> formList;

  /**
   * Constructs the helper for given <i>formList</i>, filling it with initial values.
   * 
   * @param formList The form list object.
   * @param initialData Initial row objects.
   */
  public InMemoryFormListHelper(BaseFormListWidget<K, R> formList, Collection<R> initialData) {
    this.formList = formList;

    if (initialData != null) {
      for (R row : initialData) {
        this.current.put(getKey(row), row);
      }
    }

    formList.setFormRowHandler(new InMemoryFormRowHandlerDecorator<K, R>(formList.getFormRowHandler(), this));
    formList.setModel(new MapFormListModel<R>(this.current));
  }

  /**
   * Adds a new row object. The key of the row is retrieved using the row handler from the form list.
   * 
   * @param row The row to add.
   */
  public void add(R row) {
    this.added.put(getKey(row), row);
    this.current.put(getKey(row), row);
  }

  /**
   * Adds a new row object using a provided key. Use this method instead of {@link #add(Object)} when the row handler
   * won't resolve the Id, or a custom Id must be provided.
   * 
   * @param id The Id to associate with the row.
   * @param row The row to add.
   */
  public void addWithKey(K id, R row) {
    this.added.put(id, row);
    this.current.put(id, row);
  }

  /**
   * Updates a row object by Id. This method won't call any handlers, just updates its internal data.
   * 
   * @param id The Id that is associated with the row.
   * @param row The row that is updated.
   */
  public void update(K id, R row) {
    if (this.deleted.contains(id)) {
      throw new RuntimeException("Cannot update row with id '" + id + "'!");
    }
    if (this.added.containsKey(id)) {
      this.added.put(id, row);
    } else {
      this.updated.put(id, row);
    }
    this.current.put(id, row);
  }

  /**
   * Removes a row object by id. This method won't call any handlers, just updates its internal data.
   * 
   * @param id The Id that is associated with the row that is to be deleted.
   */
  public void delete(K id) {
    if (this.deleted.contains(id)) {
      throw new RuntimeException("Cannot delete row with id '" + id + "'!");
    }
    if (this.added.containsKey(id)) {
      this.added.remove(id);
    } else {
      this.updated.remove(id);
      this.deleted.add(id);
    }
    this.current.remove(id);
  }

  /**
   * Provides the key for the row object using the form list row handler.
   * 
   * @param row The row for which the Id is resolved.
   * @return The Id associated with the row.
   */
  public K getKey(R row) {
    return this.formList.getFormRowHandler().getRowKey(row);
  }

  /**
   * Provides the list row objects that were added by the user during the session.
   * 
   * @return A map of keys and associated rows that were added by the user during the session.
   */
  public Map<K, R> getAdded() {
    return this.added;
  }

  /**
   * Provides the list row objects that were deleted by the user during the session.
   * 
   * @return A map of keys and associated rows that were added by the user during the session.
   */
  public Set<K> getDeleted() {
    return this.deleted;
  }

  /**
   * Provides the list row objects that were updated (modified) by the user during the session.
   * 
   * @return A map of keys and associated rows that were updated (modified) by the user during the session.
   */
  public Map<K, R> getUpdated() {
    return this.updated;
  }

  /**
   * Provides all list row objects that are currently displayed in the list.
   * 
   * @return A map of keys and associated rows that are currently displayed in the list.
   */
  public Map<K, R> getCurrent() {
    return this.current;
  }
}
