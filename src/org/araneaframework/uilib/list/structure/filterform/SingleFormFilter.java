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
package org.araneaframework.uilib.list.structure.filterform;

import org.araneaframework.uilib.form.Control;
import org.araneaframework.uilib.form.Data;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Interface for Filters that can be added onto {@link FormWidget} using
 * exactly one {@link FormElement}.
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 * 
 * @see FormFilter
 */
public interface SingleFormFilter extends FormFilter {
	/**
	 * Adds this filter to the form by using the specified {@link Control}.
	 * <p>
	 * Label and {@link Data} are set automatically.
	 * Mandatory is <code>false</code>.
	 * </p> 
	 * 
	 * @param control <code>Control</code> to be used in the form.
	 * @return <code>FormElement</code> that was just added onto form.
	 */
	FormElement addToForm(Control control);
	
	/**
	 * Adds this filter to the form by using the specified <code>label</code>
	 * and {@link Control}.
	 * <p>
	 * {@link Data} is set automatically.
	 * Mandatory is <code>false</code>.
	 * </p> 
	 * 
	 * @param labelId <code>label</code> to be used in the form.
	 * @param control <code>Control</code> to be used in the form.
	 * @return <code>FormElement</code> that was just added onto form.
	 */
	FormElement addToForm(String labelId, Control control);
	
	/**
	 * Adds this filter to the form by using the specified {@link FormElement}.
	 * 
	 * @param element <code>FormElement</code> to be used in the form.
	 * @return <code>FormElement</code> that was just added onto form.
	 */
	FormElement addToForm(FormElement element);
}
