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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

/**
 * Like filter wildcards utils.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class WildcardUtil {

	public static void setWildcards(WildcardHandler handler, String mask,
			String anyStringWildcard, String anyCharWildcard) {
		
		Validate.notNull(handler);
		
		if (StringUtils.isEmpty(mask)) {
			handler.setStartsWith(WildcardHandler.NO_WILDCARD);
			handler.setEndsWith(WildcardHandler.NO_WILDCARD);
		} else {
			// Start
			if (mask.startsWith(anyStringWildcard)) {
				handler.setStartsWith(WildcardHandler.ANY_STRING_WILDCARD);
			} else if (mask.startsWith(anyCharWildcard)) {
				handler.setStartsWith(WildcardHandler.ANY_CHAR_WILDCARD);
			} else {
				handler.setStartsWith(WildcardHandler.NO_WILDCARD);
			}		
			
			// End
			if (mask.endsWith(anyStringWildcard)) {
				handler.setEndsWith(WildcardHandler.ANY_STRING_WILDCARD);
			} else if (mask.endsWith(anyCharWildcard)) {
				handler.setEndsWith(WildcardHandler.ANY_CHAR_WILDCARD);
			} else {
				handler.setEndsWith(WildcardHandler.NO_WILDCARD);
			}					
		}
	}
}
