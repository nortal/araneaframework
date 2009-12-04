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

package org.araneaframework.core.util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.Message;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.MountContext;

/**
 * A special message that can be used to render the components tree. It does not go to all
 * components because not all of higher level components are accessible. However, most of the
 * components are still available in the result.
 * <p>
 * The recommended usage of this class is through the {@link #execute(Environment)} or
 * {@link #execute(Environment, ItemDecorator)} methods.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public class SessionTreeRenderingMessage implements Message {

  protected static final String SPACES3 = "&nbsp;&nbsp;&nbsp;";

  protected ItemDecorator decorator = new ItemDecorator();

  protected int level = 0;

  protected List components = new LinkedList();

  protected ComponentItem activeComponent;

  /**
   * Executes the <code>SessionTreeRenderingMessage</code> and returns an instance of it so that its
   * different <code>get*()</a> methods could be invoke to retrieve results in different formats.
   * 
   * @param env The environment, which is required for initial component lookup to start traversing.
   * @return An instance of <code>SessionTreeRenderingMessage</code> so that its
   *         <code>get*()</a> methods could be invoke to retrieve results in different formats.
   */
  public static SessionTreeRenderingMessage execute(Environment env) {
    return execute(env, null);
  }

  /**
   * Executes the <code>SessionTreeRenderingMessage</code> and returns an instance of it so that its
   * different <code>get*()</a> methods could be invoke to retrieve results in different formats.
   * 
   * @param env The environment, which is required for initial component lookup to start traversing.
   * @param decorator The decorator for the string result, which may be <code>null</code> to use the
   *          default implementation.
   * @return An instance of <code>SessionTreeRenderingMessage</code> so that its
   *         <code>get*()</a> methods could be invoke to retrieve results in different formats.
   */
  public static SessionTreeRenderingMessage execute(Environment env, ItemDecorator decorator) {
    return execute(env, decorator, MountContext.class);
  }

  /**
   * Executes the <code>SessionTreeRenderingMessage</code> and returns an instance of it so that its
   * different <code>get*()</a> methods could be invoke to retrieve results in different formats.
   * 
   * @param env The environment, which is required for initial component lookup to start traversing.
   * @param decorator The decorator for the string result, which may be <code>null</code> to use the
   *          default implementation.
   * @param initialEnvEntry The class that will be looked for in the environment to find the initial
   *          component, from which the sub components look up is started.
   * @return An instance of <code>SessionTreeRenderingMessage</code> so that its
   *         <code>get*()</a> methods could be invoke to retrieve results in different formats.
   */
  public static SessionTreeRenderingMessage execute(Environment env, ItemDecorator decorator, Class initialEnvEntry) {
    Assert.notNullParam(env, "env");

    Component c = (Component) env.requireEntry(initialEnvEntry);
    SessionTreeRenderingMessage msg = new SessionTreeRenderingMessage(decorator);

    c._getComponent().propagate(msg);

    return msg;
  }

  /**
   * The constructor for creating an instance of the message. Although the static
   * <code>execute(...)</code> method are more preferred, you can still use the message separately.
   * Also note that the contructor requires <code>ItemDecorator</code>, which may be
   * <code>null</code> to use the default implementation for decorating the output.
   * 
   * @param decorator The decorator that is used for generating the string output. You may provide a
   *          custom implementation, or <code>null</code> to use the default.
   */
  public SessionTreeRenderingMessage(ItemDecorator decorator) {
    this.decorator = decorator == null ? new ItemDecorator() : decorator;
  }

  public void send(Object id, Component component) {
    if (component != null) {
      ComponentItem currentComponent = this.activeComponent;

      try {
        this.activeComponent = this.add(id, component);
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }

      this.level++;
      component._getComponent().propagate(this);
      this.level--;

      this.activeComponent = currentComponent;
    }
  }

  /**
   * Adds the component with given ID to the results.
   * @param id The ID of the component, which may be <code>null</code>.
   * @param component The component to add.
   * @return The {@link ComponentItem} that was added to the results.
   */
  protected ComponentItem add(Object id, Component component) {
    ComponentItem result = new ComponentItem(id, component);
    if (this.activeComponent != null) {
      this.activeComponent.getChildren().add(result);
    } else {
      this.components.add(result);
    }
    return result;
  }

  /**
   * Returns the components found by this message. The items in this list are of type
   * {@link ComponentItem}. Note that the returned list may contain only one component item because
   * actually the component will return its child components through
   * {@link ComponentItem#getChildren()}.
   * 
   * @return A list of {@link ComponentItem}s.
   */
  public List getComponents() {
    return this.components;
  }

  /**
   * Provides the result as a <code>StringBuffer</code> that is composed by {@link ItemDecorator}.
   * 
   * @return The results as a <code>StringBuffer</code> object.
   */
  public StringBuffer getResult() {
    StringBuffer result = new StringBuffer();
    if (!this.components.isEmpty()) {
      compose(this.decorator, result, (ComponentItem) this.components.get(0), 0);
    }
    return result;
  }

  /**
   * An internal solution for composing the <code>StringBuffer</code> result recursively.
   * 
   * @param decorator The decorator used for composing the <code>StringBuffer</code>.
   * @param sb The <code>StringBuffer</code> where the output is stored.
   * @param item The component that is added to the output. Its children are also recursively
   *          processed by this method.
   * @param level The depth of the item compared to the root component in the results.
   */
  protected void compose(ItemDecorator decorator, StringBuffer sb, ComponentItem item, int level) {
    decorator.add(sb, item, level);
    if (item.hasChildren()) {
      for (Iterator i = item.getChildren().iterator(); i.hasNext();) {
        compose(decorator, sb, (ComponentItem) i.next(), level + 1);
      }
    }
  }

  /**
   * Provides the result as a <code>String</code> that is composed by {@link ItemDecorator}.
   * 
   * @return The results as a <code>String</code> object.
   */
  public String getResultStr() {
    return getResult().toString();
  }

  /**
   * The decorator class for taking care of rendering the result in <code>StringBuffer</code>
   * format. It is used by methods in {@link SessionTreeRenderingMessage}.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 1.2.3
   */
  public class ItemDecorator implements Serializable {

    /**
     * Adds an item to the resulting <code>StringBuffer</code>.
     * @param sb The <code>StringBuffer</code> where the result is stored.
     * @param item The component and its children information.
     * @param level The "depth" of this <code>item</code> compared to the root component.
     */
    public void add(StringBuffer sb, ComponentItem item, int level) {
      sb.append(StringUtils.repeat(SPACES3, level));
      if (level > 0) {
        sb.append(item.hasChildren() ? "&raquo;&nbsp;" : SPACES3);
      }
      sb.append(item).append("<br/>");
    }
  }

  /**
   * A class implementation to store component related info in an easibly accessible manner.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 1.2.3
   */
  public class ComponentItem implements Serializable {

    private Object id;

    private Component component;

    private List children = new LinkedList();

    /**
     * The only way to create and provide data to this component item.
     * 
     * @param id The ID of the <code>component</code> (usually <code>null</code> or
     *          <code>String</code>).
     * @param component The component object.
     */
    public ComponentItem(Object id, Component component) {
      this.id = id;
      this.component = component;
    }

    /**
     * Provides the ID of the component (usually <code>null</code> or <code>String</code>).
     * 
     * @return The ID of the component (usually <code>null</code> or <code>String</code>).
     */
    public Object getId() {
      return this.id;
    }

    /**
     * Provides the component.
     * 
     * @return The component.
     */
    public Component getComponent() {
      return this.component;
    }

    /**
     * Provides the child components of this component.
     * 
     * @return The child components of this component.
     */
    public List getChildren() {
      return this.children;
    }

    /**
     * Provides a Boolean indicating whether this component has any child components.
     * 
     * @return A Boolean indicating whether this component has any child components.
     */
    public boolean hasChildren() {
      return !this.children.isEmpty();
    }

    public String toString() {
      StringBuffer sb = new StringBuffer("[")
        .append(this.id)
        .append("] = ")
        .append(ClassUtils.getShortClassName(this.component, "(null)"))
        .append(" (")
        .append(this.children.size())
        .append(")");
      return sb.toString();
    }
  }
}
