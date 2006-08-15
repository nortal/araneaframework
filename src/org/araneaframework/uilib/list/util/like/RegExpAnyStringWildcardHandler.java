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
 * Regular expressions implementation of {@link AnyStringWildcardHandler}.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see AnyStringWildcardHandler
 */
public class RegExpAnyStringWildcardHandler extends BaseAnyStringWildcardHandler {

	public boolean shouldStartWith() {
		return true;
	}

	public boolean shouldEndWith() {
		return true;
	}

	public AnyStringWildcardHandler newInstance() {
		return new RegExpAnyStringWildcardHandler();
	}

}
