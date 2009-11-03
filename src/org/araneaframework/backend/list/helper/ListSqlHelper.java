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

package org.araneaframework.backend.list.helper;

import java.sql.ResultSet;
import javax.sql.DataSource;
import org.araneaframework.backend.list.helper.fields.ConcatFields;
import org.araneaframework.backend.list.helper.fields.Fields;
import org.araneaframework.backend.list.helper.fields.StandardFields;
import org.araneaframework.backend.list.helper.naming.MappingNamingStrategyAndFields;
import org.araneaframework.backend.list.helper.naming.NamingStrategy;
import org.araneaframework.backend.list.helper.naming.OrNamingStrategy;
import org.araneaframework.backend.list.helper.naming.PrefixMapNamingStrategy;
import org.araneaframework.backend.list.helper.reader.ConverterBasedColumnReader;
import org.araneaframework.backend.list.helper.reader.DefaultResultSetColumnReader;
import org.araneaframework.backend.list.helper.reader.ResultSetColumnReader;
import org.araneaframework.backend.list.model.ListQuery;
import org.araneaframework.uilib.list.util.Converter;

/**
 * This class provides a standard SQL based implementation of the list. It takes care of the filtering, ordering and
 * returning data to the web components.
 * 
 * @see BaseListSqlHelper
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public abstract class ListSqlHelper extends BaseListSqlHelper {

  protected StandardFields standardFields;

  protected MappingNamingStrategyAndFields mappingNamingStrategyAndFields;

  protected PrefixMapNamingStrategy prefixMapNamingStrategy;

  protected StandardValueConverter standardValueConverter;

  protected ConverterBasedColumnReader converterBasedColumnReader;

  /**
   * Creates <code>ListSqlHelper</code> without initializing any fields.
   */
  public ListSqlHelper() {
    super();
  }

  /**
   * Creates <code>ListSqlHelper</code> and provides it with the <code>DataSource</code>.
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
   * Creates <code>ListSqlHelper</code> initializing the appropriate fields and providing it with the
   * <code>DataSource</code>.
   */
  public ListSqlHelper(DataSource dataSource, ListQuery query) {
    super(dataSource, query);
  }

  @Override
  protected void init() {
    super.init();
    getPrefixMapNamingStrategy(); // Add naming strategy
  }

  // *********************************************************************
  // * DATABASE MAPPING AND CONVERTERS
  // *********************************************************************

  /**
   * Add additional "set" of fields.
   * 
   * @see Fields
   */
  public void addFields(Fields newFields) {
    // Update "fields"
    if (this.fields == null) {
      this.fields = newFields;
    } else if (this.fields instanceof ConcatFields) {
      ((ConcatFields) this.fields).add(newFields);
    } else {
      Fields old = this.fields;
      ConcatFields concat = new ConcatFields();
      concat.add(old);
      concat.add(newFields);
      this.fields = concat;
    }
  }

  /**
   * Add additional naming strategy.
   * <p>
   * The given <code>namingStrategy</code> is added as last in the context of {@link OrNamingStrategy}.
   * 
   * @see NamingStrategy
   */
  public void addNamingStrategy(NamingStrategy namingStrategy) {
    addNamingStrategy(namingStrategy, false);
  }

  /**
   * Add additional naming strategy.
   * <p>
   * The given <code>namingStrategy</code> is added as first in the context of {@link OrNamingStrategy}.
   * 
   * @see NamingStrategy
   */
  public void addNamingStrategyAsFirst(NamingStrategy namingStrategy) {
    addNamingStrategy(namingStrategy, true);
  }

  protected void addNamingStrategy(NamingStrategy newNamingStrategy, boolean first) {
    // Update "namingStrategy"
    if (this.namingStrategy == null) {
      this.namingStrategy = newNamingStrategy;
    } else if (namingStrategy instanceof OrNamingStrategy) {
      OrNamingStrategy or = (OrNamingStrategy) this.namingStrategy;
      if (first) {
        or.addFirst(newNamingStrategy);
      } else {
        or.add(newNamingStrategy);
      }
    } else {
      NamingStrategy old = this.namingStrategy;
      OrNamingStrategy or = new OrNamingStrategy();
      or.add(old);
      if (first) {
        or.addFirst(newNamingStrategy);
      } else {
        or.add(newNamingStrategy);
      }
      this.namingStrategy = or;
    }
  }

  public void setStandardFields(StandardFields standardFields) {
    this.standardFields = standardFields;
  }

  /**
   * @return standard implementation of list of fields.
   */
  public StandardFields getStandardFields() {
    if (this.standardFields == null) {
      this.standardFields = new StandardFields();
      addFields(this.standardFields);
    }
    return this.standardFields;
  }

  public void setMappingNamingStrategyAndFields(MappingNamingStrategyAndFields mappingNamingStrategyAndFields) {
    this.mappingNamingStrategyAndFields = mappingNamingStrategyAndFields;
  }

  public MappingNamingStrategyAndFields getMappingNamingStrategyAndFields() {
    if (this.mappingNamingStrategyAndFields == null) {
      this.mappingNamingStrategyAndFields = new MappingNamingStrategyAndFields();
      addFields(this.mappingNamingStrategyAndFields);
      addNamingStrategyAsFirst(this.mappingNamingStrategyAndFields);
    }
    return this.mappingNamingStrategyAndFields;
  }

  public void setPrefixMapNamingStrategy(PrefixMapNamingStrategy prefixMapNamingStrategy) {
    this.prefixMapNamingStrategy = prefixMapNamingStrategy;
  }

  public PrefixMapNamingStrategy getPrefixMapNamingStrategy() {
    if (this.prefixMapNamingStrategy == null) {
      this.prefixMapNamingStrategy = new PrefixMapNamingStrategy();
      addNamingStrategy(this.prefixMapNamingStrategy);
    }
    return this.prefixMapNamingStrategy;
  }

  public void setStandardValueConverter(StandardValueConverter standardValueConverter) {
    this.standardValueConverter = standardValueConverter;
  }

  public StandardValueConverter getStandardValueConverter() {
    if (this.standardValueConverter == null) {
      this.standardValueConverter = new StandardValueConverter();
      this.valueConverter = this.standardValueConverter;
    }
    return this.standardValueConverter;
  }

  public void setConverterBasedColumnReader(ConverterBasedColumnReader converterBasedColumnReader) {
    this.converterBasedColumnReader = converterBasedColumnReader;
  }

  public ConverterBasedColumnReader getConverterBasedColumnReader() {
    if (this.converterBasedColumnReader == null) {
      ResultSetColumnReader defaultResultSetColumnReader = DefaultResultSetColumnReader.getInstance();
      this.converterBasedColumnReader = new ConverterBasedColumnReader(defaultResultSetColumnReader);
      this.resultSetColumnReader = this.converterBasedColumnReader;
    }
    return this.converterBasedColumnReader;
  }

  /**
   * Adds a converter for a filter/order expression value. The converter is used by auomatic SQL query creation
   * according to the filter/order expressions.
   * 
   * @param valueName filter/order expression value name.
   * @param converter converter that is used by <code>convert()</code> method.
   * @see #addResultSetDeconverterForBeanField(String, Converter)
   * @see #addResultSetDeconverterForColumn(String, Converter)
   */
  public <S, D> void addDatabaseFieldConverter(String valueName, Converter<S, D> converter) {
    getStandardValueConverter().addConverter(valueName, converter);
  }

  /**
   * Adds a deconverter for <code>ResultSet</code>. The converter is used by {@link BaseListSqlHelper.BeanResultReader}
   * to reverseConvert() values from <code>ResultSet</code> into bean field format.
   * 
   * @param beanField Bean field name.
   * @param converter converter that is used by <code>reverseConvert()</code> method.
   * @see #addDatabaseFieldConverter(String, Converter)
   * @see #addResultSetDeconverterForColumn(String, Converter)
   * @see BaseListSqlHelper.BeanResultReader
   */
  public <S, D> void addResultSetDeconverterForBeanField(String beanField, Converter<S, D> converter) {
    String columnName = this.namingStrategy.fieldToColumnAlias(beanField);
    addResultSetDeconverterForColumn(columnName, converter);
  }

  /**
   * Adds a deconverter for <code>ResultSet</code>. The converter is used by {@link BaseListSqlHelper.BeanResultReader}
   * to reverseConvert() values from <code>ResultSet</code> into bean field format.
   * 
   * @param columnName ResultSet column name.
   * @param converter converter that is used by <code>reverseConvert()</code> method.
   * @see #addDatabaseFieldConverter(String, Converter)
   * @see #addResultSetDeconverterForBeanField(String, Converter)
   * @see BaseListSqlHelper.BeanResultReader
   */
  public <S, D> void addResultSetDeconverterForColumn(String columnName, Converter<S, D> converter) {
    getConverterBasedColumnReader().addResultSetDeconverterForColumn(columnName, converter);
  }

  // Mappings
  /**
   * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
   * <p>
   * A given field is listed in the <code>SELECT</code> but is not read from the {@link ResultSet}.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @param columnAlias database column alias.
   * @see #addMapping(String, String, String)
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addDatabaseFieldMapping(String fieldName, String columnName, String columnAlias) {
    getMappingNamingStrategyAndFields().addDatabaseFieldMapping(fieldName, columnName, columnAlias);
  }

  /**
   * Adds a <b>field name</b> to database <b>column name</b> mapping.
   * <p>
   * A given field is listed in the <code>SELECT</code> but is not read from the {@link ResultSet}.
   * </p>
   * <p>
   * The corresponding <b>column alias</b> is generated automatically.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @see #addMapping(String, String, String)
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addDatabaseFieldMapping(String fieldName, String columnName) {
    getMappingNamingStrategyAndFields().addDatabaseFieldMapping(fieldName, columnName);
  }

  /**
   * Adds a <b>field name</b> to database <b>column alias</b> mapping.
   * <p>
   * A given field is not listed in the <code>SELECT</code> but is read from the {@link ResultSet}.
   * </p>
   * 
   * @param fieldName field name.
   * @param columnAlias database column name in the result set.
   * @see #addMapping(String, String, String)
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String)
   */
  public void addResultSetMapping(String fieldName, String columnAlias) {
    getMappingNamingStrategyAndFields().addResultSetMapping(fieldName, columnAlias);
  }

  /**
   * Adds a <b>field name</b> to database <b>column name</b> and <b>column alias</b> mapping.
   * <p>
   * A given field is listed in the <code>SELECT</code> and is read from the {@link ResultSet}.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @param columnAlias database column alias.
   * @see #addMapping(String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addMapping(String fieldName, String columnName, String columnAlias) {
    getMappingNamingStrategyAndFields().addMapping(fieldName, columnName, columnAlias);
  }

  /**
   * Adds a <b>field name</b> to database <b>column name</b>.
   * <p>
   * A given field is listed in the <code>SELECT</code> and is read from the {@link ResultSet}.
   * </p>
   * <p>
   * The corresponding <b>column alias</b> is generated automatically.
   * 
   * @param fieldName field name.
   * @param columnName database column name.
   * @see #addMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String, String)
   * @see #addDatabaseFieldMapping(String, String)
   * @see #addResultSetMapping(String, String)
   */
  public void addMapping(String fieldName, String columnName) {
    getMappingNamingStrategyAndFields().addMapping(fieldName, columnName);
  }
}
