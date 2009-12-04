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

package org.araneaframework.uilib.form;

import org.araneaframework.Environment;

import java.io.Serializable;
import java.util.Map;

/**
 * The form context is the common interface of all kinds of forms and also the key to be used for form retrieval from
 * {@link Environment}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface FormContext extends Serializable {

  /**
   * Provides whether the background validation is enabled for all form elements in this form. Background validation is
   * an automatic client-side form control's input data validation over AJAX requests. The validation is usually done
   * right after the user has selected an option or leaves a changed input (blur event).
   * 
   * @return Whether the background validation is enabled for all form elements in this form.
   */
  boolean isBackgroundValidation();

  /**
   * Provides the properties defined for this form. The properties are good for defining additional properties for the
   * form when no such property exists by default. Therefore, this is for extension default possibilities.
   * 
   * @return A modifiable map of form properties. Never a <code>null</code>.
   */
  Map<Object, Object> getProperties();
}
