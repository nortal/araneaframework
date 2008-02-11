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

package org.araneaframework.backend.list.helper.fields;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.araneaframework.backend.list.helper.ListSqlHelper;
import org.araneaframework.backend.util.BeanUtil;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.list.structure.ListStructure;
import org.araneaframework.uilib.util.NameUtil;

/**
 * List of fields standard implementation.
 * <p>
 * All the fields are presented both in the <code>SELECT</code> and in the {@link ResultSet}.
 * </p>
 * <p>
 * Fields can be added/removed either:
 * <ul>
 * <li>one by one</li>
 * <li>as a list</li>
 * <li>using a {@link ListStructure} (all fields defined in the list)</li>
 * <li>using a Bean Class (all fields are listed using reflection)</li>
 * </ul>
 * <p>
 * For each add/remove call a <b>prefix</b >can be set.
 * Prefix should refer to a composite field in a Bean (containing another Bean).
 * </p>
 * <p>
 * Example use:
 * <pre>
 * ListSqlHelper helper = ...
 * StandardFields sf = helper.getStandardFields();
 * sf.addField("name");				// add "name"
 * sf.addField("birthdate");				// add "birthdate"
 * sf.addField("address", "town");			// add "address.town"
 * sf.addField("address", "zip");			// add "address.zip"
 * sf.addField("org", Organization.class);		// add all fields of Organization.class as sub-fields
 * Modifier.addListFields(sf.subFields("modifier"));	// let Mofifier.class add it's common fields
 * </pre>
 * 
 * @see Fields
 * @see ListSqlHelper#getStandardFields()
 * 
 * @author Rein Raudjärv
 * 
 * @since 1.1
 */
public class StandardFields implements Fields {

	private Collection fields;
	
	private String globalPrefix;
	
	/**
	 * Create an instance of {@link StandardFields} using an empty set of fields.
	 */
	public StandardFields() {
		fields = new ArrayList();
	}
	
	/**
	 * Create an instance of {@link StandardFields} extending the set of fields
	 * of another {@link StandardFields} instance and
	 * using the <code>globalPrefix</code> to all the fields added.
	 */
	public StandardFields(StandardFields parent, String globalPrefix) {
		this.fields = parent.fields;
		this.globalPrefix = globalPrefix;
	}
	
	/**
	 * Create an instance of {@link StandardFields} using the specified set of fields..
	 */
	public StandardFields(Collection fields) {
		this.fields = fields;
	}
	
	/**
	 * Create an instance of {@link StandardFields} using the set of fields
	 * defined by the {@link ListStructure}.
	 */
	public StandardFields(ListStructure structure) {
		this();
		addFields(structure);
	}
	
	/**
	 * Create an instance of {@link StandardFields} extending the set of fields
	 * of another {@link StandardFields} instance and
	 * using the <code>globalPrefix</code> to all the fields added.
	 */
	public StandardFields subFields(String globalPrefix) {
		Assert.notNullParam(globalPrefix, "globalPrefix");
		return new StandardFields(this, NameUtil.getFullName(this.globalPrefix, globalPrefix));
	}
	
	public StandardFields addField(String prefix, String field) {
		fields.add(addPrefix(prefix, field));
		return this;
	}
	
	public StandardFields removeField(String prefix, String field) {
		fields.remove(addPrefix(prefix, field));
		return this;
	}
	
	public StandardFields addFields(String prefix, Collection fields) {
		this.fields.addAll(addPrefix(prefix, fields));
		return this;
	}
	
	public StandardFields removeFields(String prefix, Collection fields) {
		this.fields.removeAll(addPrefix(prefix, fields));
		return this;
	}
	
	public StandardFields addFields(String prefix, String[] fields) {
		this.fields.addAll(addPrefix(prefix, Arrays.asList(fields)));
		return this;
	}
	
	public StandardFields removeFields(String prefix, String[] fields) {
		this.fields.removeAll(addPrefix(prefix, Arrays.asList(fields)));
		return this;
	}
	
	public StandardFields addField(String field) {
		return addField(null, field);
	}
	
	public StandardFields removeField(String field) {
		return removeField(null, field);
	}
	
	public StandardFields addFields(ListStructure structure) {
		return addFields(structure.getFields().keySet());
	}
	
	public StandardFields addFields(Collection fields) {
		return addFields(null, fields);
	}
	
	public StandardFields removeFields(Collection fields) {
		return removeFields(null, fields);
	}
	
	public StandardFields addFields(String[] fields) {
		return addFields(null, fields);
	}
	
	public StandardFields removeFields(String[] fields) {
		return removeFields(null, fields);
	}	
	
	public StandardFields addFields(Class beanClass) {
		return addFields(null, beanClass);
	}
	
	public StandardFields removeFields(Class beanClass) {
		return removeFields(null, beanClass);
	}
	
	public StandardFields addFields(String prefix, Class beanClass) {
		return addFields(prefix, BeanUtil.getFields(beanClass));
	}
	
	public StandardFields removeFields(String prefix, Class beanClass) {
		return removeFields(prefix, BeanUtil.getFields(beanClass));
	}

	public Collection getNames() {
		return fields;
	}

	public Collection getResultSetNames() {
		return fields;
	}
	
	// ---
	
	private String addPrefix(String prefix, String field) {
		String result = NameUtil.getFullName(prefix, field);
		return globalPrefix == null ? result : NameUtil.getFullName(globalPrefix, result);
	}
	
	private Collection addPrefix(String prefix, Collection fields) {
		if (prefix == null || prefix.length() == 0) {
			return fields;
		}
		
		List result = new ArrayList(fields.size());
		for (Iterator it = fields.iterator(); it.hasNext();) {
			String field = (String) it.next();
			result.add(addPrefix(prefix, field));
		}
		return result;
	}

}
