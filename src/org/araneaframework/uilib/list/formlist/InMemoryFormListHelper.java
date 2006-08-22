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

package org.araneaframework.uilib.list.formlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.uilib.list.RowHandler;

/**
 * Helper that facilitates holding the editable list rows in memory without saving them to database. 
 * Useful when the editable list should be persisted only in the very end of user session. 
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class InMemoryFormListHelper implements Serializable {
	protected List addedRows = new ArrayList();
	
	protected Map added = new HashMap();
	protected Map updated = new HashMap();
	protected Set deleted = new HashSet();
	
	protected Map current = new LinkedMap();
	
	protected RowHandler rowHandler;
	
	/**
	 * @param initalData initial row objects.
	 * @param rowHandler row handler used to query row object ids.
	 */
	public InMemoryFormListHelper(Collection initalData, RowHandler rowHandler) {
		this.rowHandler = rowHandler;
		
		for (Iterator i = initalData.iterator(); i.hasNext();) {
			Object row = (Object) i.next();
			current.put(rowHandler.getRowKey(row), row);
		}
	}
	
	/**
	 * Adds a new row object assigning it temporary id.
	 */
	public void add(Object row) {
		added.put(getTempKey(row), row);		
		current.put(getTempKey(row), row);
	}
	
	/**
	 * Adds a new row object using a provided key.
	 */
	public void addWithKey(Object id, Object row) {
		added.put(id, row);		
		current.put(id, row);	  
	}
	
	/**
	 * Updates a row object by id.
	 */
	public void update(Object id, Object row) {
		if (deleted.contains(id))
			throw new RuntimeException("Cannot update row with id '" + id + "'!");
		
		if (added.containsKey(id))
			added.put(id, row);
		else
			updated.put(id, row);
		
		current.put(id, row);
	}
	
	/**
	 * Removes a row object by id.
	 */
	public void delete(Object id) {
		if (deleted.contains(id))
			throw new RuntimeException("Cannot delete row with id '" + id + "'!");
		
		if (added.containsKey(id))
			added.remove(id);
		else {
			updated.remove(id);		
			deleted.add(id);
		}
		
		current.remove(id);
	}
	
	protected static class RowTempId implements Serializable {
		private Object row;
		
		public RowTempId(Object row) {
			this.row = row;
		}
		
		public Object getRow() {
			return this.row;
		}
		
		public String toString() {
			return "tempId@" + Integer.toHexString(super.hashCode());
		}
	}
	
	/**
	 * Returns a temporary key for the object (if object is new, assigns a new temporary key). 
	 */
	public Object getTempKey(Object row) {
		for (Iterator i = addedRows.iterator(); i.hasNext();) {
			RowTempId elem = (RowTempId) i.next();
			if (elem.getRow() == row) {
				return elem;
			}
		}
		
		Object result = new RowTempId(row);
		addedRows.add(result);
		
		return result;
	}
	
	/**
	 * Returns row objects added during the session.
	 */
	public Map getAdded() {
		return this.added;
	}
	
	/**
	 * Returns row objects deleted during the session.
	 */
	public Set getDeleted() {
		return this.deleted;
	}
	
	/**
	 * Returns row objects updated during the session.
	 */
	public Map getUpdated() {
		return this.updated;
	}
	
	/**
	 * Returns current row objects. 
	 */
	public Map getCurrent() {
		return this.current;
	}
}
