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

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This is an input field combined with Select&mdash;it allows end-user to enter text
 * into it or select some predefined value from provided list. Predefined inputs are
 * by default sorted by natural order, custom comparator may be set when needed.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class ComboTextControl extends TextControl {
	private TreeSet inputs;
	
	public ComboTextControl() {
		inputs = new TreeSet();
	}
	
	public ComboTextControl(Comparator comparator) {
		inputs = new TreeSet();
	}

	public void addPredefinedInput(String s) {
		inputs.add(s);
	}

	public void addPredefinedInputs(Collection c) {
		inputs.addAll(c);
	}

	public Object getViewModel() {
		return new ViewModel();
	}
	
    public class ViewModel extends TextControl.ViewModel {
    	private SortedSet inputs;

    	protected ViewModel() {
    		this.inputs = Collections.unmodifiableSortedSet(ComboTextControl.this.inputs);
    	}
    	
    	public Collection getPredefinedInputs() {
    		return this.inputs;
    	}
    }
}
