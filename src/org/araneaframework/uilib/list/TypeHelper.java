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

import org.araneaframework.http.util.EnvironmentUtil;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.araneaframework.Environment;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.list.util.ComparatorFactory;

/**
 * List fields types and comparators helper.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 * 
 * @see ListWidget
 */
@SuppressWarnings("unchecked")
public class TypeHelper implements Serializable {

  // Configuration
  private Locale locale;

  private boolean ignoreCase = true;

  // Custom comparators for fields:
  private Map<String, Comparator> comparators = new HashMap<String, Comparator>();

  private Map<String, Class> types = new HashMap<String, Class>();

  private boolean initialized = false;

  private boolean changed = true;

  public void init(Environment env) throws Exception {
    if (this.locale == null) {
      LocalizationContext l10nCtx = EnvironmentUtil.getLocalizationContext(env);
      if (l10nCtx != null) {
        this.locale = l10nCtx.getLocale();
      }
      if (this.locale == null) {
        this.locale = Locale.getDefault();
      }
    }

    this.initialized = true;
    fireChange();
  }

  private boolean isInitialized() {
    return this.initialized;
  }

  public void destroy() throws Exception {}

  public boolean isIgnoreCase() {
    return this.ignoreCase;
  }

  public void setIgnoreCase(boolean ignoreCase) {
    if (this.ignoreCase != ignoreCase) {
      this.ignoreCase = ignoreCase;
      fireChange();
    }
  }

  public Locale getLocale() {
    if (!isInitialized()) {
      throw new IllegalStateException("Must be initialized first");
    }
    return this.locale;
  }

  public void setLocale(Locale locale) {
    if (!locale.equals(this.locale)) {
      this.locale = locale;
      fireChange();
    }
  }

  // List fields

  /**
   * Returns a comparator for the specifeid field.
   * <p>
   * First, a custom comparator is returned if found.
   * <p>
   * Otherwise a comparator is tryed to create according to the field type returned by {@link #getFieldType(String)}.
   * Also {@link #isIgnoreCase()} and {@link #getLocale()} is considered for creating the new comparator.
   * 
   * @param fieldId field Id.
   * @return comparator for this field.
   */
  public Comparator getFieldComparator(String fieldId) {
    Comparator result = getCustomComparator(fieldId);

    if (result == null) {
      Class fieldType = getFieldType(fieldId);

      if (fieldType == null) {
        throw new AraneaRuntimeException("Could not resolve the value type of field '" + fieldId + "'");
      }

      result = buildComparator(fieldType);
    }
    return result;
  }

  public void addFieldType(String fieldId, Class type) {
    this.types.put(fieldId, type);
    fireChange();
  }

  /**
   * Returns the field type.
   * <p>
   * Returns <code>null</code> if no type specified for this field or no such field exists.
   * 
   * @param fieldId field Id.
   * @return type of this field.
   */
  public Class getFieldType(String fieldId) {
    return this.types.get(fieldId);
  }

  public Class removeFieldType(String fieldId) {
    Class result = this.types.remove(fieldId);
    fireChange();
    return result;
  }

  public void addCustomComparator(String fieldId, Comparator comp) {
    this.comparators.put(fieldId, comp);
    fireChange();
  }

  public Comparator getCustomComparator(String fieldId) {
    return this.comparators.get(fieldId);
  }

  public Comparator removeCustomComparator(String fieldId) {
    Comparator result = this.comparators.remove(fieldId);
    fireChange();
    return result;
  }

  // Comparator
  protected Comparator buildComparator(Class type) {
    Assert.notNullParam(this, type, "type");

    if (String.class.equals(type)) {
      return ComparatorFactory.getStringComparator(isNullFirst(), isIgnoreCase(), getLocale());
    }

    // Boolean is Comparable since Java 1.5
    if (Boolean.class.equals(type) && !Boolean.class.isAssignableFrom(Comparable.class)) {
      return ComparatorFactory.getBooleanComparator(isNullFirst(), isTrueFirst());
    }

    return ComparatorFactory.getDefault();
  }

  protected boolean isNullFirst() {
    return ComparatorFactory.NULL_FIRST_BY_DEFAULT;
  }

  protected boolean isTrueFirst() {
    return ComparatorFactory.TRUE_FIRST_BY_DEFAULT;
  }

  /**
   * @since 1.1
   */
  protected void fireChange() {
    this.changed = true;
  }

  /**
   * Returns whether the basic configuration that specifies which items are shown has changed since last call to this
   * {@link TypeHelper}'s {@link TypeHelper#checkChanged()} method.
   * 
   * @since 1.1
   */
  public boolean checkChanged() {
    boolean result = this.changed;
    this.changed = false;
    return result;
  }
}
