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
 * Any string wildcard Like pattern handler.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public interface AnyStringWildcardHandler {
	
	/**
	 * Must be set as the Like pattern starts with any string wildcard or not.
	 * 
	 * @param startsWith Like pattern starts with any string wildcard or not.
	 */
	void setStartsWith(boolean startsWith);
	
	/**
	 * Must be set as the Like pattern ends with any string wildcard or not.
	 * 
	 * @param endsWith Like pattern ends with any string wildcard or not.
	 */
	void setEndsWith(boolean endsWith);
	
	/**
	 * Returns whether the Like pattern should start with any string wildcard.
	 * 
	 * @return whether the Like pattern should start with any string wildcard.
	 */
	boolean shouldStartWith();
	
	/**
	 * Returns whether the Like pattern should end with any string wildcard.
	 * 
	 * @return whether the Like pattern should end with any string wildcard.
	 */
	boolean shouldEndWith();
	
	/**
	 * Returns new instance of the same class.
	 * 
	 * @return new instance of the same class.
	 */
	AnyStringWildcardHandler newInstance();
	
}
