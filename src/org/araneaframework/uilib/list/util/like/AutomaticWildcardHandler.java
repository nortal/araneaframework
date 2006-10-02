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

package org.araneaframework.uilib.list.util.like;

/**
 * Automatic implementation of {@link WildcardHandler}.
 * 
 * Wildcard is added at the start if there was no wildcard at the end
 *   and it is added at the end if there was no wildcard at the start.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see WildcardHandler
 */
public class AutomaticWildcardHandler extends BaseWildcardHandler {

	public int shouldStartWith() {
		return noWildcards() ? ANY_STRING_WILDCARD : this.startsWith;
	}

	public int shouldEndWith() {
		return noWildcards() ? ANY_STRING_WILDCARD : this.endsWith;
	}
	
	private boolean noWildcards() {
		return this.startsWith == NO_WILDCARD && this.endsWith == NO_WILDCARD;
	}

	public WildcardHandler newInstance() {
		return new AutomaticWildcardHandler();
	}

}
