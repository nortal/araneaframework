/**
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
**/

package org.araneaframework.tests.framework.component;

import junit.framework.TestCase;
import org.araneaframework.Component;
import org.araneaframework.mock.MockInputData;
import org.araneaframework.mock.MockOutputData;
import org.araneaframework.mock.core.MockBaseComponent;
import org.araneaframework.mock.core.MockBaseWidget;
import org.araneaframework.tests.mock.MockEnvironment;

/**
 * {@link Component} lifecycle constraint satisfiability.
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class LifeCycleTests extends TestCase {
    // tests that dead component stays dead
	public void testPermantentDeath() throws Exception {
		MockBaseComponent c = new MockBaseComponent();
		
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
		MockBaseWidget w = new MockBaseWidget();
		w._getComponent().init(null, new MockEnvironment());
		w._getComponent().destroy();
		
		// ACTION
		try {
			w._getService().action(null, new MockInputData(), new MockOutputData());
			fail("Should not be reached");
		} catch (IllegalStateException e) {
			
		}

		// UPDATE
		try {
			w._getWidget().update(new MockInputData());
			fail("Should not be reached");
		} catch (IllegalStateException e) {
			
		}
		
		// EVENT
		try {
			w._getWidget().event(null, new MockInputData());
			fail("Should not be reached");
		} catch (IllegalStateException e) {
			
		}
		
		// TODO: PROCESS:: to be removed after process() is gone from 1.1 devel branch
		try {
			w._getWidget().process();
			fail("Should not be reached");
		} catch (IllegalStateException e) {
		
		}
		
		// RENDER
		try {
			w._getWidget().render(new MockOutputData());
			fail("Should not be reached");
		} catch (IllegalStateException e) {
		
		}
	}
	
	public void testValidLeftOverCalls() throws Exception {
		MockBaseWidget w = new MockBaseWidget();
		w._getComponent().init(null, new MockEnvironment());
		w._getComponent().destroy();
		
		w.addComponent("new", new MockBaseWidget());
		w._getComponent().disable();
		w._getComponent().enable();
	}
}
