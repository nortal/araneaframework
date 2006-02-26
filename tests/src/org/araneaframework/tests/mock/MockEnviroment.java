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

package org.araneaframework.tests.mock;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.uilib.ConfigurationContext;


/**
 * This class implemets {@link UiLibEnviroment} interface
 * providing a straightforward implementation, which basicly returns the
 * arguments passed without any specific processing. It is used primarily for testing.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class MockEnviroment implements Environment {
	private static Logger log = Logger.getLogger(MockEnviroment.class);
	
	private Map contexts = new HashMap();
	
	public MockEnviroment() {
    contexts.put(ConfigurationContext.class, new MockConfiguration());
	}

	public Object getEntry(Object key) {
		return contexts.get(key);
	}	
}
