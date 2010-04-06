/*
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
 */

package org.araneaframework.uilib.list.util.like;


/**
 * Wildcard Like pattern handler.
 * 
 * @author Rein Raudjärv (rein@araneaframework.org)
 */
public interface WildcardHandler {
	
	int ANY_STRING_WILDCARD = 1;
	int ANY_CHAR_WILDCARD = 2;
	int NO_WILDCARD = 0;
	
	/**
	 * Must be set as which wildcard the Like pattern starts with.
	 * 
	 * @param wildcard Like pattern wildcard.
	 */
	void setStartsWith(int wildcard);
	
	/**
	 * Must be set as which wildcard the Like pattern ends with
	 * 
	 * @param wildcard Like pattern wildcard.
	 */
	void setEndsWith(int wildcard);
	
	/**
	 * Returns which wildcard the Like pattern currently starts with. 
	 * 
	 * @return Like pattern wildcard.
	 */
	int getStartsWith();
	
	/**
	 * Returns which wildcard the Like pattern currently ends with. 
	 * 
	 * @return Like pattern wildcard.
	 */
	int getEndsWith();
	
	/**
	 * Returns which wildcard the Like pattern should start with.
	 * 
	 * @return Like pattern wildcard.
	 */
	int shouldStartWith();
	
	/**
	 * Returns which wildcard the Like pattern should end with.
	 * 
	 * @return Like pattern wildcard.
	 */
	int shouldEndWith();
	
	/**
	 * Returns preprocessed mask (escaped using defined ESCAPE symbol)
	 */
	String getEscapedMask();
	
	/**
	 * Sets preprocessed mask.
	 */
	void setEscapedMask(String escapedMask);
	
	/**
	 * Returns new instance of the same class.
	 * 
	 * @return new instance of the same class.
	 */
	WildcardHandler newInstance();
	
}
