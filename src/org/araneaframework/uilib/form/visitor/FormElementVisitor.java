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

package org.araneaframework.uilib.form.visitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.GenericFormElement;
import org.araneaframework.uilib.form.formlist.BaseFormListWidget;
import org.araneaframework.uilib.util.NameUtil;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class FormElementVisitor implements Serializable {

  protected List<IdElementPair> contextStack = new ArrayList<IdElementPair>();

  /**
   * Pushes the context element to stack.
   */
  public void pushContext(String id, GenericFormElement element) {
    this.contextStack.add(new IdElementPair(id, element));
  }

  /**
   * Pops the context element from the stack.
   */
  public void popContext() {
    this.contextStack.remove(this.contextStack.size() - 1);
  }

  /**
   * Represents a context element paired with its id.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  private static class IdElementPair implements Serializable {

    private String id;

    private GenericFormElement element;

    public IdElementPair(String id, GenericFormElement element) {
      this.id = id;
      this.element = element;
    }

    public GenericFormElement getElement() {
      return this.element;
    }

    public String getId() {
      return id;
    }
  }

  // *********************************************************************
  // * CONTEXT METHODS
  // *********************************************************************

  /**
   * Returns the current context element that should be the parent of the element being visited.
   */
  public GenericFormElement getParent() {
    return this.contextStack.get(this.contextStack.size() - 1).getElement();
  }

  /**
   * Returns the identifier prefix to the element being visited in relation to the root visisted element.
   */
  public String getPrefix() {
    String result = "";

    for (Iterator<IdElementPair> i = this.contextStack.iterator(); i.hasNext();) {
      IdElementPair pair = i.next();
      result = NameUtil.getFullName(result, pair.getId());
    }

    return result;
  }

  /**
   * Returns the depth that visited element is on (number of parents).
   */
  public int getDepth() {
    return this.contextStack.size();
  }

  // *********************************************************************
  // * VISITING METHODS
  // *********************************************************************

  /**
   * Visits all elements and subelements of the given composite element.
   */
  public void visitAll(GenericFormElement form) {
    form.accept("", this);
  }

  // *********************************************************************
  // * ABSTRACT METHODS
  // *********************************************************************

  /**
   * Visits a simple element.
   */
  public abstract void visit(String id, FormElement<?, ?> element);

  /**
   * Visits a composite element.
   */
  public abstract void visit(String id, FormWidget element);

  /**
   * Visits a composite element.
   */
  public abstract void visit(String id, BaseFormListWidget<?, ?> element);
}
