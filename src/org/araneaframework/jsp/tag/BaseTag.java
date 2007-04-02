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
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.exception.AraneaJspException;
import org.araneaframework.jsp.tag.context.WidgetContextTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetUtil;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * UI contained base tag.
 * 
 * @author Oleg Mürk
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class BaseTag implements Tag, TryCatchFinally, ContainedTagInterface {
  /* ***********************************************************************************
   * VARIABLES
   * ***********************************************************************************/
  protected Tag parent;
  protected PageContext pageContext;

  /**
   * A list of registered tags.
   */
  private Set registeredTags;

  /**
   * Map: scope -> (Map: key -> backup attribute value)
   */ 
  private Map attributeBackup;

  private Set globalContextEntries;
  private Map hiddenContextEntries;
  public static final String GLOBAL_CONTEXT_ENTRIES_KEY = "org.araneaframework.jsp.tag.BaseTag.GLOBAL_CONTEXT_ENTRIES";

  /* ***********************************************************************************
   * Tag interface methods && ContainedTagInterface methods
   * ***********************************************************************************/

  /**
   * Initialization.
   */
  public void setPageContext(PageContext pageContext) {
    this.pageContext = pageContext;

    // Internal initialization
    registeredTags = new HashSet(); 
    attributeBackup = new HashMap();
  }

  /**
   * Start tag.
   */
  public final int doStartTag() throws JspException {
    try {
      return doStartTag(pageContext.getOut());
    }
    catch(RuntimeException e) {
      throw e;
    }
    catch(JspException e) {
      throw e;		
    }
    catch(Exception e) {
      throw new JspException(e);
    }
  }

  /**
   * End tag
   */	
  public final int doEndTag() throws JspException {
    try {
      return doEndTag(pageContext.getOut());
    }
    catch(RuntimeException e) {
      throw e;
    }
    catch(JspException e) {
      throw e;
    }		
    catch(Exception e) {
      throw new JspException(e);
    }
  }

  public void setParent(Tag tag) {
    this.parent = tag;
  }

  public Tag getParent() {
    return parent;
  }

  /**
   * Deinitialization.
   */
  public void release() {}

  /* ***********************************************************************************
   * TryCatchFinally interface methods
   * ***********************************************************************************/

  public void doCatch(Throwable t) throws Throwable {
    throw t;
  }

  /** Override it to free additional resources, always call superclass method too. */
  public void doFinally() {
    releaseTags();
    restoreAllContextEntries();
    resetGlobalContextEntries();
  }

  /* ***********************************************************************************
   * Tags writing out start and end tags.
   * ***********************************************************************************/
  /**
   * Internal callback: before tag.
   * @throws Exception
   */
  protected int doStartTag(Writer out) throws Exception {
    return EVAL_BODY_INCLUDE;
  }

  /**
   * Internal callback: after tag.
   * @throws Exception
   */
  protected int doEndTag(Writer out) throws Exception {
    return EVAL_PAGE;
  }

  /* ***********************************************************************************
   * Context entry managing methods
   * ***********************************************************************************/
  /**
   * Gets the value of <code>key</code> from <code>PageContext.REQUEST_SCOPE</code>.
   */	
  protected Object getContextEntry(String key) {
    return JspUtil.getContextEntry(pageContext, key);
  }

  /**
   * Read attribute value in PageContext.REQUEST_SCOPE and ensure that it is defined.
   * @throws JspException when entry corresponding to key is not found
   */
  protected Object requireContextEntry(String key) throws JspException {
    return JspUtil.requireContextEntry(pageContext, key);
  }

  /**
   * Set attribute value in given scope, but allow restoring it to the state before 
   * executing this action.
   */
  protected void addContextEntry(String key, Object value) {
    Map attributeBackupMap = getBackupContextEntryMap(PageContext.REQUEST_SCOPE);	

    // Backup value
    Object currentAttribute = pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
    if (!attributeBackupMap.containsKey(key))
      attributeBackupMap.put(key, currentAttribute);

    // Set new value
    addGlobalContextEntry(key, value);
    if (value != null)
      pageContext.setAttribute(key, value, PageContext.REQUEST_SCOPE);
    else
      pageContext.removeAttribute(key, PageContext.REQUEST_SCOPE);
  }

  /* ***********************************************************************************
   * Attribute evaluation methods
   * ***********************************************************************************/

  /**
   * Evaluates attribute value and checks that it is not null.
   */
  protected Object evaluateNotNull(String attributeName, String attributeValue, Class classObject) throws JspException {
    Object value = evaluate(attributeName, attributeValue, classObject);
    if (value == null)
      throw new AraneaJspException("Attribute '" + attributeName + "' should not evaluate to null");
    return value;
  }

  /**
   * Evaluates attribute value.
   */
  protected Object evaluate(String attributeName, String attributeValue, Class classObject) throws JspException {
    return ExpressionEvaluatorManager.evaluate(
        attributeName, 
        attributeValue, 
        classObject, 
        this, 
        pageContext
    );
  }

  /* ***********************************************************************************
   * Subtag managing methods
   * ***********************************************************************************/
  /**
   * Registers a subtag.
   */	
  protected void registerSubtag(ContainedTagInterface subtag) {
    subtag.setPageContext(pageContext);
    registeredTags.add(subtag);
  }

  /**
   * Unregisters a subtag.
   */	
  protected void unregisterSubtag(ContainedTagInterface subtag) {
    subtag.doFinally();

    subtag.release();
    registeredTags.remove(subtag);
  }

  /**
   * Executes registered subtag.
   */
  protected int executeSubtag(ContainedTagInterface subtag) throws JspException {
    int result = subtag.doStartTag();
    if (result == Tag.EVAL_BODY_INCLUDE)
      return subtag.doEndTag(); 
    else
      return result;
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

  //
  // Service methods
  //

  protected ConfigurationContext getConfiguration() throws JspException {
    JspContext config = (JspContext) getEnvironment().getEntry(JspContext.class);
    return config.getConfiguration();
  }

  /**
   * Returns the current <code>LocalizationContext</code>.
   * @return current <code>LocalizationContext</code>.
   */
  protected LocalizationContext getLocalizationContext() throws JspException {
    return JspUtil.getLocalizationContext(pageContext);
  }

  /**
   * Returns the current response object.
   * @return the current response object.
   */
  protected OutputData getOutputData() throws JspException {
    return ServletUtil.getOutputData(pageContext.getRequest());
  }

  protected Environment getEnvironment() throws JspException {
    return ServletUtil.getEnvironment(pageContext.getRequest());
  }

  protected ApplicationWidget getContextWidget() {
    return JspWidgetUtil.getContextWidget(pageContext);
  }

  protected String getContextWidgetFullId() {
    return JspWidgetUtil.getContextWidgetFullId(pageContext);
  }

  /* ***********************************************************************************
   * PRIVATE internal method for releasing the subtags
   * ***********************************************************************************/	
  private void releaseTags() {
    for (Iterator i = registeredTags.iterator(); i.hasNext();) {
      ContainedTagInterface subtag = (ContainedTagInterface) i.next();

      subtag.doFinally();      
      subtag.release();

      i.remove();
    }        
  }

  /* ***********************************************************************************
   * PRIVATE internal methods for context entry managing.
   * ***********************************************************************************/
  /**
   * Get backup attribute map for given scope. 
   */
  private Map getBackupContextEntryMap(int scope) {
    if (attributeBackup == null)
      attributeBackup = new HashMap();

    Map map =(Map) attributeBackup.get(new Integer(scope));
    if (map == null) {
      map = new HashMap();
      attributeBackup.put(new Integer(scope), map);
    }
    return map;
  }

  /**
   * Restores all attributes to the values before executing this action.
   */	
  private void restoreAllContextEntries() {
    if (attributeBackup == null) return;

    for(Iterator i = attributeBackup.entrySet().iterator(); i.hasNext();) {
      Map.Entry attributeBackupEntry = (Map.Entry)i.next();
      int scope = ((Integer)attributeBackupEntry.getKey()).intValue();
      Map attributeBackupMap = (Map)attributeBackupEntry.getValue();

      for(Iterator j = attributeBackupMap.entrySet().iterator(); j.hasNext();) {
        Map.Entry entry2 = (Map.Entry)j.next();
        Object oldAttribute = entry2.getValue();
        addGlobalContextEntry((String)entry2.getKey(), oldAttribute);
        if (oldAttribute != null)
          pageContext.setAttribute((String)entry2.getKey(), oldAttribute, scope);
        else
          pageContext.removeAttribute((String)entry2.getKey(), scope);
      }
    }

    // Release data
    attributeBackup = null;
  }

  /* ***********************************************************************************
   * Hiding and restoring contextentries when widgetInclude is done
   * ***********************************************************************************/

  protected void addGlobalContextEntry(String key, Object value) {
    if (hiddenContextEntries != null) {
      hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }
    if (value == null) {
      if (globalContextEntries != null)
        globalContextEntries.remove(key);
    } else {
      if (globalContextEntries == null) {
        globalContextEntries = (Set) getContextEntry(GLOBAL_CONTEXT_ENTRIES_KEY);
        if (globalContextEntries == null) {
          globalContextEntries = new HashSet();
          globalContextEntries.add(WidgetContextTag.CONTEXT_WIDGET_KEY);
          globalContextEntries.add(Environment.ENVIRONMENT_KEY);
          globalContextEntries.add(WidgetContextTag.CONTEXT_WIDGET_KEY);
          addContextEntry(GLOBAL_CONTEXT_ENTRIES_KEY, globalContextEntries);
        }
      }
      globalContextEntries.add(key);
    }
  }

  protected void hideGlobalContextEntries(PageContext pageContext) {
    if (globalContextEntries == null)
      globalContextEntries = (Set) getContextEntry(GLOBAL_CONTEXT_ENTRIES_KEY);
    if (globalContextEntries == null || globalContextEntries.size() == 0)
      return;
    if (hiddenContextEntries != null) {
      hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }
    hiddenContextEntries = new HashMap();
    for (Iterator i = globalContextEntries.iterator(); i.hasNext(); ) {
      String key = (String) i.next();
      Object value = pageContext.getAttribute(key, PageContext.REQUEST_SCOPE);
      if (value != null) {
        hiddenContextEntries.put(key, value);
        pageContext.removeAttribute(key, PageContext.REQUEST_SCOPE);
      }
    }
  }

  protected void restoreGlobalContextEntries(PageContext pageContext) {
    if (hiddenContextEntries == null)
      return;
    for (Iterator i = hiddenContextEntries.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry entry = (Map.Entry) i.next();
      pageContext.setAttribute((String) entry.getKey(), entry.getValue(), PageContext.REQUEST_SCOPE);
    }
    hiddenContextEntries = null;
  }

  protected void resetGlobalContextEntries() {
    if (globalContextEntries != null) {
      globalContextEntries = null;
    }
    if (hiddenContextEntries != null) {
      hiddenContextEntries = null;
      throw new AraneaRuntimeException("ContextEntries were not restored properly");
    }
  }

}
