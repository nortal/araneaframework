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

package org.araneaframework.jsp.tag.entity;

/**
 * Entity tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "gt"
 *   body-content = "empty"
 *   description = "HTML <i>&amp;gt;</i> entity."
 */
public class UiGtEntityTag extends UiEntityTag {
	protected void init() {
		super.init();
		
		this.code = "gt";
	}
}
