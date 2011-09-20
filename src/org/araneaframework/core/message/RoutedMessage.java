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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Component;
import org.araneaframework.Message;
import org.araneaframework.Path;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * A message that is sent to exactly one component in a hierarchy using a path. This method propagates through
 * components that do not have an ID (usually at the start of the component hierarchy tree) without using the given
 * path. The path matching is done from the first component with an ID. When an item in the given path is not found in
 * the actual components tree, no exception nor component processing in {@link #execute(Component)} will occur.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class RoutedMessage implements Message {

  private static final Log LOG = LogFactory.getLog(RoutedMessage.class);

  private final Path path;

  private final String destination;

  /**
   * Creates a new message to be sent to a component with given target path. The target component will be processed in
   * the {@link #execute(Component)} method.
   * 
   * @param path The path to the target component.
   */
  public RoutedMessage(Path path) {
    Assert.notNullParam(this, path, "path");

    this.path = path;
    this.destination = path.toString();
  }

  /**
   * Send method implementation that uses the <tt>id</tt> parameter and the underlying path to navigate to target
   * component. Components without an ID are just passed through.
   * <p>
   * {@inheritDoc}
   */
  public final void send(Object id, Component component) {
    // After routing finished.
    // Reaching this return usually means that the last path item, the target component was not found.
    if (!this.path.hasNext()) {
      return;
    }

    // Before named hierarchy: just passing through components without an ID.
    if (id == null) {
      component._getComponent().propagate(this);
      return;
    }

    // Routing to next: a path item was found, moving on to that component.
    if (this.path.getNext().equals(id)) {
      this.path.next(); // Advance in the path to the next path item.

      if (this.path.hasNext()) {
        component._getComponent().propagate(this);
      } else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Delivering message routed to '" + this.destination + "'");
        }

        try {
          execute(component);
        } catch (Exception e) {
          throw ExceptionUtil.uncheckException(e);
        }
      }
    }
  }

  /**
   * Method that is called on a component that is target of this message as defined by the path.
   * 
   * @param component The target component that this message has reached.
   * @throws Exception Any exception that may occur during processing.
   */
  protected abstract void execute(Component component) throws Exception;
}
