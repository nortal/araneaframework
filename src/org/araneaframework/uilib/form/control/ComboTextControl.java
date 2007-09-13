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

package org.araneaframework.uilib.form.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is an input field combined with Select&mdash;arrow with selection options is
 * defined somewhere.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class ComboTextControl extends TextControl {
	private List inputs = new ArrayList();

	public void addPredefinedInput(String s) {
		inputs.add(s);
	}

	public Object getViewModel() {
		return new ViewModel();
	}
	
    public class ViewModel extends TextControl.ViewModel {
    	private List inputs;
    	
    	protected ViewModel() {
    		this.inputs = Collections.unmodifiableList(ComboTextControl.this.inputs);
    	}
    	
    	public List getPredefinedInputs() {
    		return this.inputs;
    	}
    }
}
