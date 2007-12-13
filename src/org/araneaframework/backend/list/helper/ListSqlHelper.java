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

package org.araneaframework.backend.list.helper;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.list.helper.fields.ConcatFields;
import org.araneaframework.backend.list.helper.fields.Fields;
import org.araneaframework.backend.list.helper.fields.StandardFields;
import org.araneaframework.backend.list.helper.naming.DefaultNamingStrategy;
import org.araneaframework.backend.list.helper.naming.NamingStrategy;
import org.araneaframework.backend.list.helper.naming.OrNamingStrategy;
import org.araneaframework.backend.list.helper.naming.PrefixMapNamingStrategy;
import org.araneaframework.backend.list.helper.reader.DefaultResultSetColumnReader;
import org.araneaframework.backend.list.helper.reader.ResultSetColumnReader;
import org.araneaframework.backend.list.helper.reader.StandardResultSetColumnReader;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.uilib.list.util.Converter;


/**
 * This class provides an SQL based implementation of the list. It takes care of
 * the filtering, ordering and returning data to the web components.
 * Implementations should override abstract methods noted in those methods.
 * <p>
 * Note, that all operations on items are made on the list of "processed", that
 * is ordered and filtered items.
 * <p>
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public abstract class ListSqlHelper extends BaseListSqlHelper {

	protected static final Log log = LogFactory.getLog(ListSqlHelper.class);

	// *******************************************************************
	// FIELDS
	// *******************************************************************

	protected StandardFields standardFields;
	protected DefaultNamingStrategy defaultNamingStrategy;
	protected PrefixMapNamingStrategy prefixMapNamingStrategy;
	
	protected StandardValueConverter standardValueConverter;
	protected StandardResultSetColumnReader standardResultSetColumnReader;
	

	// *********************************************************************
	// * CONSTRUCTORS
	// *********************************************************************

	/**
	 * Creates <code>ListSqlHelper</code> without initializing any fields.
	 */
	public ListSqlHelper() {
		super();
	}
	
	/**
	 * Creates <code>ListSqlHelper</code> and provides it with the
	 * <code>DataSource</code>.
	 */
	public ListSqlHelper(DataSource dataSource) {
		super(dataSource);
	}
	
	/**
	 * Creates <code>ListSqlHelper</code> initializing the appropriate fields.
	 */
	public ListSqlHelper(ListQuery query) {
		super(query);
	}	
	
	/**
	 * Creates <code>ListSqlHelper</code> initializing the appropriate fields
	 * and providing it with the <code>DataSource</code>.
	 */
	public ListSqlHelper(DataSource dataSource, ListQuery query) {
		super(dataSource, query);
	}
	
	// *********************************************************************
	// * DATABASE MAPPING AND CONVERTERS
	// *********************************************************************	
	
	// Standard implementation
	
	protected void addFields(Fields newFields) {
		// Update "fields"
		if (fields == null) {
			fields = newFields;
		}
		else if (fields instanceof ConcatFields) {
			((ConcatFields) fields).add(newFields); 
		}
		else {
			Fields old = fields;
			ConcatFields concat = new ConcatFields();
			concat.add(old);
			concat.add(newFields);
			fields = concat;
		}
	}
	
	protected void addNamingStrategy(NamingStrategy newNamingStrategy, boolean first) {
		// Update "namingStrategy"
		if (namingStrategy == null) {
			namingStrategy = newNamingStrategy;
		}
		else if (namingStrategy instanceof OrNamingStrategy) {
			OrNamingStrategy or = (OrNamingStrategy) namingStrategy;
			if (first) {
				or.addFirst(newNamingStrategy); 
			} else {
				or.add(newNamingStrategy);
			}
		}
		else {
			NamingStrategy old = namingStrategy;
			OrNamingStrategy or = new OrNamingStrategy();
			if (first) {
				or.add(old);
				or.add(newNamingStrategy);
			} else {
				or.add(newNamingStrategy);
				or.add(old);
			}
			namingStrategy = or;
		}
	}
	
	public void setStandardFields(StandardFields standardFields) {
		this.standardFields = standardFields;
	}
	
	public StandardFields getStandardFields() {
		if (standardFields == null) {
			standardFields = new StandardFields();
			addFields(standardFields);
		}
		return standardFields;
	}
	
	public void setDefaultNamingStrategy(DefaultNamingStrategy defaultNamingStrategy) {
		this.defaultNamingStrategy = defaultNamingStrategy;
	}
	
	public DefaultNamingStrategy getDefaultNamingStrategy() {
		if (defaultNamingStrategy == null) {
			defaultNamingStrategy = new DefaultNamingStrategy();
			addFields(defaultNamingStrategy);
			addNamingStrategy(defaultNamingStrategy, true);
		}
		return defaultNamingStrategy;
	}
	
	public void setPrefixMapNamingStrategy(
			PrefixMapNamingStrategy prefixMapNamingStrategy) {
		this.prefixMapNamingStrategy = prefixMapNamingStrategy;
	}
	
	public PrefixMapNamingStrategy getPrefixMapNamingStrategy() {
		if (prefixMapNamingStrategy == null) {
			prefixMapNamingStrategy = new PrefixMapNamingStrategy();
			addNamingStrategy(prefixMapNamingStrategy, false);
		}
		return prefixMapNamingStrategy;
	}
	
	public void setStandardValueConverter(StandardValueConverter standardValueConverter) {
		this.standardValueConverter = standardValueConverter;
	}
	
	public StandardValueConverter getStandardValueConverter() {
		if (standardValueConverter == null) {
			standardValueConverter = new StandardValueConverter();
			valueConverter = standardValueConverter;
		}
		return standardValueConverter;
	}
	
	public void setStandardResultSetColumnReader(
			StandardResultSetColumnReader standardResultSetColumnReader) {
		this.standardResultSetColumnReader = standardResultSetColumnReader;
	}

	public StandardResultSetColumnReader getStandardResultSetColumnReader() {
		if (standardResultSetColumnReader == null) {
			ResultSetColumnReader defaultResultSetColumnReader = DefaultResultSetColumnReader.getInstance();
			standardResultSetColumnReader = new StandardResultSetColumnReader(defaultResultSetColumnReader);
			resultSetColumnReader = standardResultSetColumnReader; 
		}
		return standardResultSetColumnReader;
	}

	/**
	 * Adds a converter for a filter/order expression value. The converter is
	 * used by auomatic SQL query creation according to the filter/order
	 * expressions. 
	 *  
	 * @param valueName
	 *            filter/order expression value name.
	 * @param converter
	 *            converter that is used by <code>convert()</code> method.
	 *            
	 * @see #addResultSetDeconverterForBeanField(String, Converter)
	 * @see #addResultSetDeconverterForColumn(String, Converter)
	 */
	public void addDatabaseFieldConverter(String valueName, Converter converter) {
		getStandardValueConverter().addConverter(valueName, converter);
	}

	/**
	 * Adds a deconverter for <code>ResultSet</code>. The converter is used by
	 * {@link BeanResultReader} to reverseConvert() values from
	 * <code>ResultSet</code> into bean field format.
	 * 
	 * @param beanField
	 *            Bean field name.
	 * @param converter
	 *            converter that is used by <code>reverseConvert()</code>
	 *            method.
	 *            
	 * @see #addDatabaseFieldConverter(String, Converter)
	 * @see #addResultSetDeconverterForColumn(String, Converter)
	 * @see BeanResultReader
	 */
	public void addResultSetDeconverterForBeanField(String beanField, Converter converter) {
		String columnName = namingStrategy.fieldToColumnAlias(beanField);
		addResultSetDeconverterForColumn(columnName, converter);
	}
	
	/**
	 * Adds a deconverter for <code>ResultSet</code>. The converter is used by
	 * {@link BeanResultReader} to reverseConvert() values from
	 * <code>ResultSet</code> into bean field format.
	 * 
	 * @param columnName
	 *            ResultSet column name.
	 * @param converter
	 *            converter that is used by <code>reverseConvert()</code>
	 *            method.
	 *            
	 * @see #addDatabaseFieldConverter(String, Converter)
	 * @see #addResultSetDeconverterForBeanField(String, Converter)
	 * @see BeanResultReader
	 */
	public void addResultSetDeconverterForColumn(String columnName, Converter converter) {
		getStandardResultSetColumnReader().addResultSetDeconverterForColumn(columnName, converter);
	}
	
	// Mappings

	/**
	 * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
	 * <p>
	 * A given field is not listed in the <code>SELECT</code> clause.
	 * 
	 * @param fieldName
	 *            field name.
	 * @param columnName
	 *            database column name.
	 *            
	 * @see #addMapping(String, String)
	 * @see #addMapping(String, String, String)
	 */
	public void addDatabaseFieldMapping(String fieldName, String columnName) {
		getDefaultNamingStrategy().addDatabaseFieldMapping(fieldName, columnName);
	}
	
	/**
	 * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
	 * <p>
	 * A given field is also listed in the <code>SELECT</code> clause.
	 * 
	 * @param fieldName
	 *            field name.
	 * @param columnName
	 *            database column name.
	 * @param columnAlias
	 *            database column alias.
	 *
	 * @see #addMapping(String, String)
	 * @see #addDatabaseFieldMapping(String, String)
	 */
	public void addMapping(String fieldName, String columnName, String columnAlias) {
		getDefaultNamingStrategy().addMapping(fieldName, columnName, columnAlias);
	}
	
	/**
	 * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
	 * <p>
	 * A given field is also listed in the <code>SELECT</code> clause.
	 * </p>
	 * <p>
	 * The corresponding <b>column alias</b> is generated automatically.
	 * 
	 * @param fieldName
	 *            field name.
	 * @param columnName
	 *            database column name.
	 *
	 * @see #addMapping(String, String, String)
	 * @see #addDatabaseFieldMapping(String, String)
	 */	
	public void addMapping(String fieldName, String columnName) {
		getDefaultNamingStrategy().addMapping(fieldName, columnName);
	}
}
