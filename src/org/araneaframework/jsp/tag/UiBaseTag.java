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

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TryCatchFinally;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.araneaframework.OutputData;
import org.araneaframework.jsp.UiException;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.core.StandardServletServiceAdapterComponent;
import org.araneaframework.servlet.filter.StandardJspFilterService;
import org.araneaframework.uilib.ConfigurationContext;

/**
 * UI contained base tag.
 * 
 * @author Oleg Mürk
 */
public class UiBaseTag implements Tag, TryCatchFinally, UiContainedTagInterface {
	//
	// Overridable methods
	//

	/**
	 * Internal callback: initialization
	 */		
	protected void init() {}

	/**
	 * Internal callback: deinitialization
	 */		
	protected void deinit() {}
	
	/**
	 * Internal callback: before tag.
	 * @throws Exception
	 */
	protected int before(Writer out) throws Exception {
		return EVAL_BODY_INCLUDE;
	}

	/**
	 * Internal callback: after tag.
	 * @throws Exception
	 */
	protected int after(Writer out) throws Exception {
		return EVAL_PAGE;
	}

	//
	// Service methods
	//
  
	/**
	 * Evaluates attribute value and checks that it is not null.
	 */
	protected Object evaluateNotNull(String attributeName, String attributeValue, Class classObject) throws JspException {
		Object value = evaluate(attributeName, attributeValue, classObject);
		if (value == null)
			throw new UiException("Attribute '" + attributeName + "' should not evaluate to null");
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

	/**
   * This is here for convenience and historical reasons only
	 * @see UiUtil#getAttribute(PageContext, String)
	 */	
	protected Object getAttribute(String key) throws JspException {
		return UiUtil.getAttribute(pageContext, key);
	}
	
	/**
	 * @see UiUtil#getAttribute(PageContext, String, int)  
	 */	
	protected Object getAttribute(String key, int scope) throws JspException {
		return UiUtil.getAttribute(pageContext, key, scope);
	}

	/**
	 * @see UiUtil#readAttribute(PageContext, String)
	 */	
	protected Object readAttribute(String key) throws JspException {
		return UiUtil.readAttribute(pageContext, key);
	}
	
	/**
	 * @see UiUtil#readAttribute(PageContext, String, int)
	 */
	protected Object readAttribute(String key, int scope) throws JspException {
		return UiUtil.readAttribute(pageContext, key, scope);
	}
	
	/**
	 * Set attribute value, but allow restoring it to the state before 
	 * executing this action.
	 */
	protected void pushAttribute(String key, Object value) {
		pushAttribute(key, value, PageContext.PAGE_SCOPE);
	}	
	
	/**
	 * Set attribute value in given scope, but allow restoring it to the state before 
	 * executing this action.
	 */
	protected void pushAttribute(String key, Object value, int scope) {
		Map attributeBackupMap = getBackupAttributeMap(scope);	
		
		// Backup value
		Object currentAttribute = pageContext.getAttribute(key, scope);
		if (!attributeBackupMap.containsKey(key))
			attributeBackupMap.put(key, currentAttribute);
			
		// Set new value
		if (value != null)
			pageContext.setAttribute(key, value, scope);
		else
			pageContext.removeAttribute(key, scope);
	}


	/**
	 * @see UiUtil#setAttribute(PageContext, String, Object)  
	 */	
	protected void setAttribute(String key, Object value) throws JspException {
		UiUtil.setAttribute(pageContext, key, value);
	}
	
	/**
	 * @see UiUtil#setAttribute(PageContext, String, Object, int)  
	 */	
	protected void setAttribute(String key, Object value, int scope) throws JspException {
		pageContext.setAttribute(key, value, scope);
	}
  
  
  protected void include(String path) throws ServletException, IOException, JspException  {
    UiUtil.include(pageContext, path);
  }

	/**
	 * Registers a subtag.
	 */	
	protected void registerSubtag(UiContainedTagInterface subtag) {
		subtag.setPageContext(pageContext);
		registeredTags.add(subtag);
	}


	/**
	 * Unregisters a subtag.
	 */	
	protected void unregisterSubtag(UiContainedTagInterface subtag) {
		subtag.doFinally();
    
		subtag.release();
		registeredTags.remove(subtag);
	}

	
	/**
	 * Executes registered subtag.
	 */
	protected int executeSubtag(UiContainedTagInterface subtag) throws JspException {
		int result = subtag.doStartTag();
		if (result == Tag.EVAL_BODY_INCLUDE)
			return subtag.doEndTag(); 
		else
			return result;		
	}

	/**
	 * Executes start of registered subtag.
	 */
	protected int executeStartSubtag(UiContainedTagInterface subtag) throws JspException {
		return subtag.doStartTag();
	}
	
	/**
	 * Executes end of registered subtag.
	 */
	protected int executeEndSubtag(UiContainedTagInterface subtag) throws JspException {
		return subtag.doEndTag();
	}

	protected int registerAndExecuteStartTag(UiContainedTagInterface subtag) throws JspException {
		registerSubtag(subtag);
		return executeStartSubtag(subtag);
	}
	
	protected int executeEndTagAndUnregister(UiContainedTagInterface subtag) throws JspException {		
		int result = executeEndSubtag(subtag);
		unregisterSubtag(subtag);
		return result;
	}	
  
  protected ConfigurationContext getConfiguration() {
    OutputData output = 
      (OutputData) pageContext.getRequest().getAttribute(
          StandardServletServiceAdapterComponent.OUTPUT_DATA_REQUEST_ATTRIBUTE);
    StandardJspFilterService.JspConfiguration config = 
      (StandardJspFilterService.JspConfiguration) output.getAttribute(
          StandardJspFilterService.JSP_CONFIGURATION_KEY);
    return config.getConfiguration(); 
  }

	//
	// Tag methods
	//
	
	public void setParent(Tag tag) {
		this.parent = tag;
	}
	
	public Tag getParent() {
		return parent;
	}
	
	/**
	 * Initialization.
	 */
	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
		init();
		
		// Internal initialization
		registeredTags = new HashSet(); 
		attributeBackup = new HashMap();
	}

	/**
	 * Deinitialization.
	 */
	public void release() {
		deinit();
	}

	/**
	 * Start tag.
	 */
	public final int doStartTag() throws JspException {
		try {
      return before(pageContext.getOut());
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
        return after(pageContext.getOut());
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
  
  public void doCatch(Throwable arg0) throws Throwable {
    throw arg0;
  }
  
  public void doFinally() {
    releaseTags();
    restoreAllAttributes();
  }
						
	//
	// Internal implementation methods
	//
	
  private void releaseTags() {
    for (Iterator i = registeredTags.iterator(); i.hasNext();) {
      UiContainedTagInterface subtag = (UiContainedTagInterface) i.next();
      
      subtag.doFinally();      
      subtag.release();
      
      i.remove();
    }        
	}
	
	
	/**
	 * Get backup attribute map for given scope. 
	 */
	private Map getBackupAttributeMap(int scope) {
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
  private void restoreAllAttributes() {
		if (attributeBackup == null) return;
		
		for(Iterator i = attributeBackup.keySet().iterator(); i.hasNext();) {
			Integer scope = (Integer)i.next();
			Map attributeBackupMap = (Map)attributeBackup.get(scope);
			
			for(Iterator j = attributeBackupMap.keySet().iterator(); j.hasNext();) {
				String key = (String)j.next();
				Object oldAttribute = attributeBackupMap.get(key);
				if (oldAttribute != null)
					pageContext.setAttribute(key, oldAttribute, scope.intValue());
				else
					pageContext.removeAttribute(key, scope.intValue());				
			}
		}
		
		// Release data
		attributeBackup = null;
	}

	private Tag parent;
	protected PageContext pageContext;

	/**
	 * A list of registered tags.
	 */
  private Set registeredTags;
	
	/**
	 * Map: scope -> (Map: key -> backup attribute value)
	 */ 
  private Map attributeBackup; 
}
