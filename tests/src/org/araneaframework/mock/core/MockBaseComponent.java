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

package org.araneaframework.mock.core;

import org.araneaframework.core.BaseComponent;

/**
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class MockBaseComponent extends BaseComponent {
	private Boolean enabled = null;
	private boolean alive = false;

	protected void init() throws Exception {
		enabled = Boolean.TRUE;
		alive = true;
	}
	
	protected void disable() throws Exception {
		enabled = Boolean.FALSE;
	}

	protected void enable() throws Exception {
		enabled = Boolean.TRUE;
	}

	protected void destroy() throws Exception {
		enabled = null;
		alive = false;
	}
}
