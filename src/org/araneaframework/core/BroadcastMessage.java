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

package org.araneaframework.core;

import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A {@link Message} that is sent to and operates on every {@link Component} in hierarchy. In addition, this message
 * also supports executing {@link #execute(Component)} only when a component of defined component class is reached.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BroadcastMessage implements Message {

  private Class<?> componentClass;

  /**
   * Initiates a plain broadcast message.
   */
  public BroadcastMessage() {
    this(Component.class);
  }

  /**
   * Initiates a broadcast message with a component class for which {@link BroadcastMessage#execute(Component)} will be
   * called.
   * 
   * @param componentClass The component class for which instance components {@link #execute(Component)} is called.
   * @since 1.1.1
   */
  public BroadcastMessage(Class<?> componentClass) {
    Assert.notNullParam(componentClass, "componentClass");
    this.componentClass = componentClass;
  }

  /**
   * Send method that causes {@link #execute(Component)} to be called for each <code>Component</code> in hierarchy or
   * just with those <code>Component</code>s which are instances of the defined <code>componentClass</code>.
   * 
   * @see org.araneaframework.Message#send(java.lang.Object, org.araneaframework.Component)
   */
  public final void send(Object id, Component component) {
    if (component != null) {
      component._getComponent().propagate(this);
      try {
        if (this.componentClass.isAssignableFrom(component.getClass())) {
          this.execute(component);
        }
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  /**
   * Method that is called on each {@link Component} present in the {@link Component} hierarchy where this
   * {@link BroadcastMessage} is propagated.
   * 
   * @param component {@link Component} this {@link BroadcastMessage} has reached
   */
  protected abstract void execute(Component component) throws Exception;
}
