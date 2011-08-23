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

import java.io.Serializable;
import org.araneaframework.framework.MessageContext.MessageData;

/**
 * Interface through which {@link org.araneaframework.uilib.form.FormElement}s register the error messages produced by
 * failed validation.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 * @see org.araneaframework.uilib.form.StandardFormElementValidationErrorRenderer
 * @see org.araneaframework.uilib.form.LocalFormElementValidationErrorRenderer
 */
public interface FormElementValidationErrorRenderer extends Serializable {

  /**
   * <code>FormElement</code> property key (see {@link org.araneaframework.uilib.form.FormElement#getProperty(Object)})
   * under which validation errors may be stored.
   */
  String ERRORS_PROPERTY_KEY = "FormElementValidationErrors";

  /**
   * Style class which should be present on all DOM elements which contain the rendered errors.
   */
  String RENDERED_FORMELEMENTERROR_STYLECLASS = "aranea-formelementerrors";

  /**
   * Called by
   * {@link org.araneaframework.uilib.form.FormElement#addError(org.araneaframework.framework.MessageContext.MessageData)}
   * to register validation errors.
   * 
   * @param element Element, which produced the validation error.
   * @param messageData The error message data.
   */
  void addError(FormElement<?, ?> element, MessageData messageData);

  /**
   * Called by {@link org.araneaframework.uilib.form.FormElement#clearErrors()} to clear validation errors.
   * 
   * @param element Element, for which errors should be cleared.
   */
  void clearErrors(FormElement<?, ?> element);

  /**
   * Returns client-side script capable of rendering errors in desired format. This should be in a format that can be
   * directly appended to rendered HTML and should be enclosed in HTML &lt;script&gt; tags.
   * <p>
   * When the errors are rendered with some other mechanism, returns <code>null</code> or an empty String.
   * 
   * @return script capable of rendering errors in desired format
   */
  String getClientRenderText(FormElement<?, ?> element);
}
