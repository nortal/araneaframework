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

package org.araneaframework.uilib.form;

import java.io.Serializable;

/**
 * Interface through which {@link org.araneaframework.uilib.form.FormElement}s
 * register the error messages produced by failed validation.
 * 
 * @see org.araneaframework.uilib.form.StandardFormElementValidationErrorRenderer
 * @see org.araneaframework.uilib.form.LocalFormElementValidationErrorRenderer
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface FormElementValidationErrorRenderer extends Serializable {
  /**
   * {@link org.araneaframework.uilib.form.FormElement} property key (see 
   * {@link org.araneaframework.uilib.form.FormElement#getProperty(Object)}) under which validation errors may be stored.
   */
  public static final String ERRORS_PROPERTY_KEY = "FormElementValidationErrors";
  
  /**
   * Style class which should be present on all DOM elements which contain the rendered errors. 
   */
  public static final String RENDERED_FORMELEMENTERROR_STYLECLASS = "aranea-formelementerrors";

  /**
   * Called by {@link org.araneaframework.uilib.form.FormElement#addError(String)} to register validation errors. 
   * @param element element which produced the validation error
   * @param error detailed error message
   */
  void addError(FormElement element, String error);

  /**
   * Called by {@link org.araneaframework.uilib.form.FormElement#clearErrors()} to clear validation errors.
   * @param element element which errors should be cleared
   */
  void clearErrors(FormElement element);

  /**
   * Returns client side script capable of rendering errors in desired format. This should
   * be in form that can be directly appended to rendered HTML and should be enclosed in 
   * HTML &lt;script&gt; tags.
   * 
   * When the errors are rendered with some other mechanism, returns <code>null</code> or empty String.
   *  
   * @return script capable of rendering errors in desired format */
  String getClientRenderText(FormElement element);
}
