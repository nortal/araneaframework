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

package org.araneaframework.core.message;

import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A message that is sent to and operates on every component in hierarchy. In addition, this message also supports
 * executing {@link #execute(Component)} only when a component of specific type is reached.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BroadcastMessage implements Message {

  private final Class<?> componentType;

  /**
   * Initiates a broadcast message that will process all components on its way.
   */
  public BroadcastMessage() {
    this(Component.class);
  }

  /**
   * Initiates a broadcast message that will process only components of given type (when component is assignable to
   * given type).
   * 
   * @param componentType The component class for which instance components {@link #execute(Component)} is called.
   * @since 1.1.1
   */
  public BroadcastMessage(Class<?> componentType) {
    Assert.notNullParam(componentType, "componentClass");
    this.componentType = componentType;
  }

  /**
   * Send method implementation that causes {@link #execute(Component)} to be called for each <code>Component</code> in
   * hierarchy or just with those <code>Component</code>s which are of specific type (as specified in constructor).
   * <p>
   * {@inheritDoc}
   * 
   * @see org.araneaframework.Message#send(java.lang.Object, org.araneaframework.Component)
   */
  public final void send(Object id, Component component) {
    if (component != null) {
      component._getComponent().propagate(this);
      try {
        if (this.componentType.isAssignableFrom(component.getClass())) {
          execute(component);
        }
      } catch (Exception e) {
        throw ExceptionUtil.uncheckException(e);
      }
    }
  }

  /**
   * Method that is called on each component present in the component hierarchy where this message is propagated.
   * 
   * @param component A component this message has reached.
   * @throws Exception Any exception that may occur during processing.
   */
  protected abstract void execute(Component component) throws Exception;
}
