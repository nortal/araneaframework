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

package org.araneaframework.uilib.form.control.inputfilter;

import java.io.Serializable;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @since 1.0.11
 */
public class InputFilter implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Custom HTML attribute for defining filter applied to input field. */
	public static final String CHARACTER_FILTER_ATTRIBUTE = "arn-charFilter";
	
	private String characterFilter;
	private String invalidInputCustomMessage;


	
	public String getCharacterFilter() {
		return characterFilter;
	}

	public void setCharacterFilter(String characterFilter) {
		this.characterFilter = characterFilter;
	}

	public String getInvalidInputCustomMessage() {
		return invalidInputCustomMessage;
	}

	public void setInvalidInputCustomMessage(String invalidInputMessage) {
		this.invalidInputCustomMessage = invalidInputMessage;
	}
	
	public String getInvalidInputMessage() {
		return invalidInputCustomMessage == null ? UiLibMessages.INPUT_FILTER_NOT_MATCHED : invalidInputCustomMessage;
	}
}
