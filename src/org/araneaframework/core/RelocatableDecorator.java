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

package org.araneaframework.core;

import java.io.Serializable;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.araneaframework.Composite;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Relocatable;
import org.araneaframework.Service;
import org.araneaframework.Viewable;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.Relocatable.RelocatableWidget;

/**
 * A decorator for a service making it relocatable. A relocatable service can be
 * moved from one parent to another. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class RelocatableDecorator extends BaseService implements Serializable, RelocatableService, RelocatableWidget, ApplicationService,  ApplicationWidget{
  //*******************************************************************
  // FIELDS
  //*******************************************************************  
  protected Service child;

  //*******************************************************************
  // PROTECTED CLASSES
  //*******************************************************************
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
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  public Relocatable.Interface _getRelocatable() {
    return new RelocatableComponentImpl();
  }
  
  /**
   * Constructs a new StandardRelocatableServiceDecorator and sets its child service to child.
   * @param child
   */
  public RelocatableDecorator(Service child) {
    this.child = child;
  }
  
  /**
   * Sets the child service of this component.
   * @param child
   */
  public void setChildService(Service child) {
    this.child = child;
  }
  
  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************
  protected void init() throws Exception {
    child._getComponent().init(new BaseEnvironment() {
      public Object getEntry(Object key) {
        return getEnvironment().getEntry(key);
      }
    });
  }
  
  protected void propagate(Message message) throws Exception {
    message.send(null, child);
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    child._getService().action(path, input, output);
  }
  
  protected void destroy() throws Exception {
    child._getComponent().destroy();
  }

  public Environment getChildEnvironment() {
    return ((ApplicationComponent) child).getChildEnvironment();
  }

  public Composite.Interface _getComposite() {
    return ((Composite) child)._getComposite();
  }

  public Viewable.Interface _getViewable() {
    return ((Viewable) child)._getViewable();
  }

  public Widget.Interface _getWidget() {
    return ((Widget) child)._getWidget();
  }
}
