/*
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.tests.framework.component;

import junit.framework.TestCase;
import org.araneaframework.Component;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseComponent;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.mock.MockInputData;
import org.araneaframework.mock.MockOutputData;
import org.araneaframework.tests.mock.MockEnvironment;

/**
 * {@link Component} lifecycle constraint satisfiability.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class LifeCycleTests extends TestCase {

  // tests that dead component stays dead
  public void testPermantentDeath() throws Exception {
    BaseComponent c = new BaseComponent();
    c._getComponent().init(null, new MockEnvironment());
    c._getComponent().destroy();
    try {
      c._getComponent().init(null, new MockEnvironment());
      fail("Attempted to reanimate destroyed Component -- exception should have occured.");
    } catch (Exception e) {
      // good
    }
  }

  // invalid leftover calls are those that activate the methods that directly
  // depend on request or response
  public void testInvalidLeftOverCalls() throws Exception {
    BaseWidget w = new BaseWidget();
    w._getComponent().init(null, new MockEnvironment());
    w._getComponent().destroy();
    try {
      w._getComponent().destroy();
      fail("Double destroy() is prohibited.");
    } catch (IllegalStateException e) {
      // fine
    }
  }

  // all leftover calls are considered valid
  public void testValidLeftOverCalls() throws Exception {
    BaseApplicationWidget w = new BaseApplicationWidget();
    w._getComponent().init(null, new MockEnvironment());
    w._getComponent().destroy();
    w._getComponent().propagate(new BroadcastMessage() {

      private static final long serialVersionUID = 1L;

      protected void execute(Component component) throws Exception {
        return;
      }
    });
    w.addWidget("new", new BaseApplicationWidget());
    w.removeWidget("new");
    w._getComponent().disable();
    w._getComponent().enable();
    w._getService().action(null, new MockInputData(), new MockOutputData());
    w._getWidget().update(new MockInputData());
    w._getWidget().event(null, new MockInputData());
    w._getWidget().render(new MockOutputData());
  }
}
