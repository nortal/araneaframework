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
import org.araneaframework.mock.core.MockBaseComponent;
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
}
