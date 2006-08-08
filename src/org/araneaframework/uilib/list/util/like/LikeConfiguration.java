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

import java.io.Serializable;

/**
 * Like filter configuration.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class LikeConfiguration implements Serializable {
	
	/** Default custom any string wildcards. */
	public static final String[] DEFAULT_ANY_STRING_WILDCARDS = {"*","%"};
	/** Default custom any character wildcards. */
	public static final String[] DEFAULT_ANY_CHAR_WILDCARDS = {".","_","?"};
	/** Default line start/end any string wildcard handler. */
	public static final AnyStringWildcardHandler DEFAULT_HANDLER = new AutomaticAnyStringWildcardHandler();
	
	/** Custom any string wildcards (e.g. *). */
	private String[] anyStringWildcards = DEFAULT_ANY_STRING_WILDCARDS;
	/** Custom any character wildcards (e.g. ?). */
	private String[] anyCharWildcards = DEFAULT_ANY_CHAR_WILDCARDS;
	/** Custom line start/end any string wildcard handler. */
	private AnyStringWildcardHandler anyStringWildcardHandler = DEFAULT_HANDLER;
	
	/**
	 * Returns the custom any character wildcard.
	 * 
	 * @return the custom any character wildcard.
	 */
	public String[] getAnyCharWildcards() {
		return anyCharWildcards;
	}
	
	/**
	 * Sets the custom any character wildcard.
	 * 
	 * @param anyCharWildcards custom any character wildcard.
	 */
	public void setAnyCharWildcards(String[] anyCharWildcards) {
		this.anyCharWildcards = anyCharWildcards;
	}
	
	/**
	 * Returns new instance of the line start/end any string wildcard handler.
	 * 
	 * @return new instance of the line start/end any string wildcard handler.
	 */
	public AnyStringWildcardHandler createAnyStringWildcardHandler() {
		if (this.anyStringWildcardHandler == null) {
			throw new IllegalStateException("anyStringWildcardHandler must be set first");
		}
		return anyStringWildcardHandler.newInstance();
	}
	
	/**
	 * Sets the line start/end any string wildcard handler.
	 * 
	 * @param anyStringWildcardHandler line start/end any string wildcard handler.
	 */
	public void setAnyStringWildcardHandler(
			AnyStringWildcardHandler anyStringWildcardHandler) {
		this.anyStringWildcardHandler = anyStringWildcardHandler;
	}
	
	/**
	 * Returns the custom any string wildcard.
	 * 
	 * @return the custom any string wildcard.
	 */
	public String[] getAnyStringWildcards() {
		return anyStringWildcards;
	}
	
	/**
	 * Sets the custom any string wildcard.
	 * 
	 * @param anyStringWildcards custom any string wildcard.
	 */
	public void setAnyStringWildcards(String[] anyStringWildcards) {
		this.anyStringWildcards = anyStringWildcards;
	}
}
