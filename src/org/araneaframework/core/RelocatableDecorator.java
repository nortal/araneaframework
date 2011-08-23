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

import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.Service;
import org.araneaframework.Viewable;
import org.araneaframework.Widget;

/**
 * A {@link Relocatable}'s decorator to make a service relocatable. A relocatable service can be moved from one parent
 * to another, and the relocatable service will have access to the right <code>Environment</code>. The
 * <code>Environment</code> of the decorator can be overridden.
 * <p>
 * Therefore, to make a service relocatable, decorate it with this class:
 * 
 * <pre>Relocatable relocatable = new RelocatableDecorator(yourService);</pre>
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class RelocatableDecorator extends BaseService implements RelocatableWidget, ApplicationWidget {

  /**
   * The child service that is made relocatable.
   */
  protected Service child;

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  public Relocatable.Interface _getRelocatable() {
    return new RelocatableComponentImpl();
  }

  /**
   * Constructs a new <code>RelocatableDecorator</code> and sets its child service to child.
   * 
   * @param child The service that should be relocatable.
   */
  public RelocatableDecorator(Service child) {
    this.child = child;
  }

  // *******************************************************************
  // PROTECTED METHODS
  // *******************************************************************

  /**
   * Overrides the default initialization to also initialize the child service with a not <code>null</code> environment.
   */
  @Override
  protected void init() {
    this.child._getComponent().init(getScope(), new BaseEnvironment() {

      public <T> T getEntry(Class<T> key) {
        if (getEnvironment() == null) {
          return null;
        }
        return getEnvironment().getEntry(key);
      }

    });
  }

  /**
   * Overrides default implementation so that messages would reach the child service.
   */
  @Override
  protected void propagate(Message message) {
    message.send(null, this.child);
  }

  /**
   * Overrides default implementation so that actions would reach the child service.
   */
  @Override
  protected void action(Path path, InputData input, OutputData output) {
    this.child._getService().action(path, input, output);
  }

  /**
   * Overrides default implementation so that the child service would also be destroyed.
   */
  @Override
  protected void destroy() {
    this.child._getComponent().destroy();
  }

  /**
   * Overrides default implementation so that the child environment of the child service would be returned.
   */
  public Environment getChildEnvironment() {
    return ((ApplicationComponent) this.child).getChildEnvironment();
  }

  /**
   * Overrides default implementation so that the child service composite implementation would be returned.
   */
  public Composite.Interface _getComposite() {
    return ((Composite) this.child)._getComposite();
  }

  /**
   * Overrides default implementation so that the child service viewable implementation would be returned.
   */
  public Viewable.Interface _getViewable() {
    return ((Viewable) this.child)._getViewable();
  }

  /**
   * Overrides default implementation so that the child service widget implementation would be returned.
   */
  public Widget.Interface _getWidget() {
    return ((Widget) this.child)._getWidget();
  }

  // *******************************************************************
  // PROTECTED CLASSES
  // *******************************************************************

  /**
   * Relocatable implementation that uses synchronization to perform relocatable's environment update during relocation
   * to make sure no one else uses it at the same time..
   */
  protected class RelocatableComponentImpl implements Relocatable.Interface {

    public void overrideEnvironment(Environment newEnv) {
      _startCall();
      _setEnvironment(newEnv);
      _endCall();
    }

    public Environment getCurrentEnvironment() {
      return getEnvironment();
    }
  }

}
