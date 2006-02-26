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

package org.araneaframework.mock.widget;

import org.araneaframework.Component;
import org.araneaframework.core.StandardWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.FlowContext.Configurator;
import org.araneaframework.framework.FlowContext.Handler;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class MockCallableWidget extends StandardWidget {
  private boolean activateCalled = false;
  private boolean deactivateCalled = false;
  
  private boolean onCallReturned = false;
  private boolean onCallCancelled = false;
  private boolean configureCalled = false;
  private boolean destroyCalled = false;
  
  private Object returnCallReturned = null;
  
  protected void enable() throws Exception {
    activateCalled = true;
  }
  
  protected void disable() throws Exception {
    deactivateCalled = true;
  }
  
  public boolean getActivateCalled() {
    return activateCalled;
  }
  
  public boolean getDeactivateCalled() {
    return deactivateCalled; 
  }

  public boolean getOnCallCancelled() {
    return onCallCancelled;
  }

  public boolean getOnCallReturned() {
    return onCallReturned;
  }
  
  public boolean getConfigureCalled() {
    return configureCalled;
  }

  /**
   * @return
   */
  public Configurator getConfigurator() {
    return new FlowContext.Configurator() {
      public void configure(Component comp) throws Exception {
        MockCallableWidget.this.configureCalled = true;
      }
    };
  }

  public Handler getHandler() {
    return new FlowContext.Handler() {
      public void onFinish(Object returnValue) throws Exception {
        MockCallableWidget.this.onCallReturned = true;
        returnCallReturned = returnValue;
      }

      public void onCancel() throws Exception {
        MockCallableWidget.this.onCallCancelled = true;
      }
    };
  }

  public Object getReturnCallReturned() {
    return returnCallReturned;
  }
  
  public void destroy() {
    destroyCalled = true;
  }

  public boolean getDestroyCalled() {
    return destroyCalled;
  }  
}
