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

package org.araneaframework.jsp.tag;

import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TryCatchFinally;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.util.ExceptionUtil;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.context.WidgetContextTag;
import org.araneaframework.jsp.tag.uilib.WidgetTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.util.ConfigurationUtil;

/**
 * UI contained base tag.
 * 
 * @author Oleg Mürk
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class BaseTag implements Tag, TryCatchFinally, ContainedTagInterface {

  protected Tag parent;

  protected PageContext pageContext;

  /**
   * A list of registered tags.
   */
  private Set<ContainedTagInterface> registeredTags;

  /**
   * Map: scope -> (Map: key -> backup attribute value)
   */
  private Map<Integer, Map<String, Object>> attributeBackup;

  private Set<String> globalContextEntries;

  private Map<String, Object> hiddenContextEntries;

  public static final String GLOBAL_CONTEXT_ENTRIES_KEY = "org.araneaframework.jsp.tag.BaseTag.GLOBAL_CONTEXT_ENTRIES";

  /**
   * Initialization.
   */
  public void setPageContext(PageContext pageContext) {
    this.pageContext = pageContext;
    // Internal initialization
    this.registeredTags = new HashSet<ContainedTagInterface>();
    this.attributeBackup = new HashMap<Integer, Map<String, Object>>();
  }

  /**
   * Start tag.
   */
  public final int doStartTag() throws JspException {
    try {
      return doStartTag(this.pageContext.getOut());
    } catch (RuntimeException e) {
      throw e;
    } catch (JspException e) {
      throw e;
    } catch (Exception e) {
      throw new JspException(e);
    }
  }

  /**
   * End tag
   */
  public final int doEndTag() throws JspException {
    try {
      return doEndTag(this.pageContext.getOut());
    } catch (RuntimeException e) {
      throw e;
    } catch (JspException e) {
      throw e;
    } catch (Exception e) {
      throw new JspException(e);
    }
  }

  public void setParent(Tag tag) {
    this.parent = tag;
  }

  public Tag getParent() {
    return this.parent;
  }

  /**
   * Deinitialization.
   */
  public void release() {}

  public void doCatch(Throwable t) throws Throwable {
    throw t;
  }

  /**
   * Override it to free additional resources, always call superclass method too.
   */
  public void doFinally() {
    releaseTags();
    restoreAllContextEntries();
    resetGlobalContextEntries();
  }

  /**
   * Internal callback: before tag.
   * 
   * @param out The writer to be written to.
   * @throws Exception Any exception.
   */
  protected int doStartTag(Writer out) throws Exception {
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Internal callback: after tag.
   * 
   * @param out The writer to be written to.
   * @throws Exception Any exception.
   */
  protected int doEndTag(Writer out) throws Exception {
    return EVAL_PAGE;
  }

  /**
   * Gets the value of <code>key</code> from <code>PageContext.REQUEST_SCOPE</code>.
   */
  protected Object getContextEntry(String key) {
    return JspUtil.getContextEntry(this.pageContext, key);
  }

  /**
   * Read attribute value in PageContext.REQUEST_SCOPE and ensure that it is defined.
   * 
   * @param key The context entry key to be resolved.
   * @throws JspException when entry corresponding to key is not found
   */
  protected Object requireContextEntry(String key) throws JspException {
    return JspUtil.requireContextEntry(this.pageContext, key);
  }

  /**
   * Set attribute value in given scope, but allow restoring it to the state before executing this action.
   */
  protected void addContextEntry(String key, Object value) {
    Map<String, Object> attributeBackupMap = getBackupContextEntryMap(PageContext.REQUEST_SCOPE);

    // Backup value
    Object currentAttribute = this.pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);

    if (!attributeBackupMap.containsKey(key)) {
      attributeBackupMap.put(key, currentAttribute);
    }

    // Set new value
    setGlobalContextEntry(key, value);
    if (value != null) {
      this.pageContext.setAttribute(key, value, PageContext.REQUEST_SCOPE);
    } else {
      this.pageContext.removeAttribute(key, PageContext.REQUEST_SCOPE);
    }
  }

  /**
   * Evaluates attribute value and checks that it is not null.
   */
  protected <T> T evaluateNotNull(String attributeName, String attributeValue, Class<T> classObject)
      throws JspException {
    T value = evaluate(attributeName, attributeValue, classObject);
    if (value == null) {
      throw new AraneaJspException("Attribute '" + attributeName + "' should not evaluate to null");
    }
    return value;
  }

  /**
   * Evaluates attribute value.
   */
  protected <T> T evaluate(String attributeName, String attributeValue, Class<T> classObject) {
    try {
      return ConfigurationUtil.getResolver(getConfiguration()).evaluate(attributeName, attributeValue, classObject,
          this.pageContext);
    } catch (JspException e) {
      throw ExceptionUtil.uncheckException(e);
    }
  }

  /**
   * Registers a subtag.
   */
  protected <T extends ContainedTagInterface> T registerSubtag(T subtag) {
    subtag.setPageContext(this.pageContext);
    this.registeredTags.add(subtag);
    return subtag;
  }

  /**
   * Unregisters a subtag.
   */
  protected void unregisterSubtag(ContainedTagInterface subtag) {
    subtag.doFinally();
    subtag.release();
    this.registeredTags.remove(subtag);
  }

  /**
   * Executes registered subtag.
   */
  protected int executeSubtag(ContainedTagInterface subtag) throws JspException {
    int result = subtag.doStartTag();
    return result == Tag.EVAL_BODY_INCLUDE ? subtag.doEndTag() : result;
  }

  /**
   * Executes start of registered subtag.
   */
  protected int executeStartSubtag(ContainedTagInterface subtag) throws JspException {
    return subtag.doStartTag();
  }

  /**
   * Executes end of registered subtag.
   */
  protected int executeEndSubtag(ContainedTagInterface subtag) throws JspException {
    return subtag.doEndTag();
  }

  protected int registerAndExecuteStartTag(ContainedTagInterface subtag) throws JspException {
    registerSubtag(subtag);
    return executeStartSubtag(subtag);
  }

  protected int executeEndTagAndUnregister(ContainedTagInterface subtag) throws JspException {
    int result = executeEndSubtag(subtag);
    unregisterSubtag(subtag);
    return result;
  }

  protected ConfigurationContext getConfiguration() {
    return getEnvironment().getEntry(ConfigurationContext.class);
  }

  /**
   * Returns the current <code>LocalizationContext</code>.
   * 
   * @return current <code>LocalizationContext</code>.
   */
  protected LocalizationContext getLocalizationContext() {
    return JspUtil.getLocalizationContext(this.pageContext);
  }

  /**
   * Returns the current request object.
   * 
   * @return The current request object.
   * @since 2.0
   */
  protected InputData getInputData() {
    return getOutputData().getInputData();
  }

  /**
   * Returns the current response object.
   * 
   * @return The current response object.
   */
  protected OutputData getOutputData() {
    return ServletUtil.getOutputData(this.pageContext.getRequest());
  }

  /**
   * Returns the current HTTP request object.
   * 
   * @return The current HTTP request object.
   * @since 2.0
   */
  protected HttpInputData getHttpInputData() {
    return (HttpInputData) getOutputData().getInputData();
  }

  /**
   * Returns the current HTTP response object.
   * 
   * @return The current HTTP response object.
   * @since 2.0
   */
  protected HttpOutputData getHttpOutputData() {
    return (HttpOutputData) getOutputData();
  }

  /**
   * Returns the widget environment for which this tag is rendered.
   * 
   * @return The current widget environment.
   * @since 1.1
   */
  protected Environment getEnvironment() {
    return ServletUtil.getEnvironment(this.pageContext.getRequest());
  }

  /**
   * Returns the widget for which this tag is rendered.
   * 
   * @return The current context widget.
   * @since 1.1
   */
  protected ApplicationWidget getContextWidget() {
    return JspWidgetUtil.getContextWidget(this.pageContext);
  }

  /**
   * Returns the full ID of the widget for which this tag is rendered.
   * 
   * @return The full ID of the current context widget.
   * @since 1.1
   */
  protected String getContextWidgetFullId() {
    return JspWidgetUtil.getContextWidgetFullId(this.pageContext);
  }

  private void releaseTags() {
    for (Iterator<ContainedTagInterface> i = this.registeredTags.iterator(); i.hasNext();) {
      ContainedTagInterface subtag = i.next();
      subtag.doFinally();
      subtag.release();
      i.remove();
    }
  }

  /**
   * Get backup attribute map for given scope.
   */
  private Map<String, Object> getBackupContextEntryMap(int scope) {
    if (this.attributeBackup == null) {
      this.attributeBackup = new HashMap<Integer, Map<String, Object>>();
    }

    Map<String, Object> map = this.attributeBackup.get(scope);
    if (map == null) {
      map = new HashMap<String, Object>();
      this.attributeBackup.put(scope, map);
    }
    return map;
  }

  /**
   * Restores all attributes to the values before executing this action.
   */
  private void restoreAllContextEntries() {
    if (this.attributeBackup == null) {
      return;
    }

    for (Map.Entry<Integer, Map<String, Object>> entry : attributeBackup.entrySet()) {
      int scope = entry.getKey().intValue();

      for (Map.Entry<String, Object> entry2 : entry.getValue().entrySet()) {
        Object oldAttribute = entry2.getValue();
        setGlobalContextEntry(entry2.getKey(), oldAttribute);
        if (oldAttribute != null) {
          this.pageContext.setAttribute(entry2.getKey(), oldAttribute, scope);
        } else {
          this.pageContext.removeAttribute(entry2.getKey(), scope);
        }
      }
    }

    // Release data
    this.attributeBackup = null;
  }

  @SuppressWarnings("unchecked")
  private void setGlobalContextEntry(String key, Object value) {
    if (this.hiddenContextEntries != null) {
      this.hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }

    if (value == null) {
      if (this.globalContextEntries != null) {
        this.globalContextEntries.remove(key);
      }
    } else {
      if (this.globalContextEntries == null) {
        this.globalContextEntries = (Set<String>) getContextEntry(GLOBAL_CONTEXT_ENTRIES_KEY);
        if (this.globalContextEntries == null) {
          this.globalContextEntries = new HashSet<String>();
          addContextEntry(GLOBAL_CONTEXT_ENTRIES_KEY, globalContextEntries);

          // Hide context entries that are set in ServletUtil.include:
          this.globalContextEntries.add(ServletUtil.UIWIDGET_KEY);
          this.globalContextEntries.add(WidgetContextTag.CONTEXT_WIDGET_KEY);
          this.globalContextEntries.add(Environment.ENVIRONMENT_KEY);
          this.globalContextEntries.add(WidgetTag.WIDGET_KEY);
          this.globalContextEntries.add(WidgetTag.WIDGET_ID_KEY);
          this.globalContextEntries.add(WidgetTag.WIDGET_VIEW_MODEL_KEY);
          this.globalContextEntries.add(WidgetTag.WIDGET_VIEW_DATA_KEY);
          // XXX also hide ServletUtil.LOCALIZATION_CONTEXT_KEY ?
        }
      }
      this.globalContextEntries.add(key);
    }
  }

  /**
   * @since 1.1
   */
  @SuppressWarnings("unchecked")
  protected void hideGlobalContextEntries(PageContext pageContext) {
    if (this.globalContextEntries == null) {
      this.globalContextEntries = (Set<String>) getContextEntry(GLOBAL_CONTEXT_ENTRIES_KEY);
    }

    if (this.globalContextEntries == null || this.globalContextEntries.isEmpty()) {
      return;
    }

    if (this.hiddenContextEntries != null) {
      this.hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }

    this.hiddenContextEntries = new HashMap<String, Object>();

    for (Iterator<String> i = this.globalContextEntries.iterator(); i.hasNext();) {
      String key = i.next();
      Object value = pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
      if (value != null) {
        this.hiddenContextEntries.put(key, value);
        pageContext.removeAttribute(key, PageContext.REQUEST_SCOPE);
      }
    }
  }

  /**
   * @since 1.1
   */
  protected void restoreGlobalContextEntries(PageContext pageContext) {
    if (this.hiddenContextEntries != null) {
      for (Map.Entry<String, Object> entry : this.hiddenContextEntries.entrySet()) {
        pageContext.setAttribute(entry.getKey(), entry.getValue(), PageContext.REQUEST_SCOPE);
      }
      this.hiddenContextEntries = null;
    }
  }

  private void resetGlobalContextEntries() {
    if (this.globalContextEntries != null) {
      this.globalContextEntries = null;
    }
    if (this.hiddenContextEntries != null) {
      this.hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }
  }

}
