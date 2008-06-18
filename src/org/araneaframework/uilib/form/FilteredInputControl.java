/**
 * Copyright 2007 Webmedia Group Ltd.
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

package org.araneaframework.uilib.form;

import org.araneaframework.uilib.form.control.inputfilter.InputFilter;

/**
 * {@link Control} that allows specifying {@link InputFilter} 
 * for restricting the end-user input into a field.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.0.11
 */
public interface FilteredInputControl<T> extends Control<T> {
  public InputFilter getInputFilter();
  public void setInputFilter(InputFilter inputFilter);
}
