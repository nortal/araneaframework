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
package org.araneaframework.uilib.list.structure.filter;

import java.util.Comparator;
import java.util.Locale;

import org.araneaframework.Environment;
import org.araneaframework.uilib.form.FormWidget;

/**
 * Filter context that is used by list filters for their initialization. 
 * 
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public interface FilterContext {
	
	Environment getEnvironment();

	FormWidget getForm();

	boolean isIgnoreCase();

	Locale getLocale();

	String getFieldLabel(String fieldId);

	Class getFieldType(String fieldId);

	Comparator getComparator(String fieldId);

}