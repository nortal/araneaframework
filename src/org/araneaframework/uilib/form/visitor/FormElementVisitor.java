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

package org.araneaframework.uilib.form.visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.util.NameUtil;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class FormElementVisitor implements Serializable {
	
  //*********************************************************************
  //* CONTEXT STACK
  //*********************************************************************
	
	protected List contextStack = new ArrayList();
	
	/**
	 * Pushes the context element to stack. 
	 */
	public void pushContext(String id, FormWidget element) {
		contextStack.add(new IdElementPair(id, element));
	}
	
	/**
	 * Pops the context element from the stack.
	 */
	public void popContext() {
		contextStack.remove(contextStack.size() - 1);
	}
	
	/**
	 * Represents a context element paired with its id.
	 * 
	 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
	 */
	private class IdElementPair implements Serializable {
		private String id;
		private FormWidget element;
		
		public IdElementPair(String id, FormWidget element) {
			this.id = id;
			this.element = element;
		}
		
		public FormWidget getElement() {
			return element;
		}
		public String getId() {
			return id;
		}
	}
	
  //*********************************************************************
  //* CONTEXT METHODS
  //*********************************************************************
	
	/**
	 * Returns the current context element that should be the parent of the element being visited.  
	 */
	public FormWidget getParent() {
		return ((IdElementPair) contextStack.get(contextStack.size() - 1)).getElement();
	}
	
	/**
	 * Returns the identifier prefix to the element being visited in relation to the root
	 * visisted element.
	 */
	public String getPrefix() {
		String result = "";
		
		for (Iterator i = contextStack.iterator(); i.hasNext();) {
			IdElementPair pair = (IdElementPair) i.next();
			result = NameUtil.getFullName(result, pair.getId());
		}
		
		return result;
	}
	
	/**
	 * Returns the depth that visited element is on (number of parents).
	 */
	public int getDepth() {
		return contextStack.size();
	}
	
  //*********************************************************************
  //* VISITING METHODS
  //*********************************************************************
	
	/**
	 * Visits all elements and subelements of the given composite element.
	 */
	public void visitAll(FormWidget form) {
		form.accept("", this);
	}
	
  //*********************************************************************
  //* ABSTRACT METHODS
  //*********************************************************************
	
	/**
	 * Visits a simple element.
	 */
	public abstract void visit(String id, FormElement element);
	
	/**
	 * Visits a composite element.
	 */
	public abstract void visit(String id, FormWidget element);
}
