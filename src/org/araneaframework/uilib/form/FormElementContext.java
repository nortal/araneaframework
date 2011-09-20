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
import java.util.Set;
import org.araneaframework.Environment;
import org.araneaframework.framework.MessageContext.MessageData;

/**
 * Interface that defines the context of a form element.
 * 
 * @param <C> The type of the inner value of form element control.
 * @param <D> The type of the form element value.
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface FormElementContext<C, D> extends Serializable {

  /**
   * Provides whether the underlying form element is mandatory.
   * 
   * @return A Boolean that is <code>true</code> when the underlying form element is mandatory.
   */
  boolean isMandatory();

  /**
   * Provides whether the underlying form element is disabled.
   * 
   * @return A Boolean that is <code>true</code> when the underlying form element is disabled.
   */
  boolean isDisabled();

  /**
   * Provides whether the underlying form element value is read from request during the current request.
   * 
   * @return A Boolean that is <code>true</code> when the underlying form element value is updated.
   */
  boolean isRead();

  /**
   * Provides whether the underlying form element is valid.
   * 
   * @return A Boolean that is <code>true</code> when the underlying form element is valid.
   */
  boolean isValid();

  /**
   * Registers an error with given localizable message code and optional parameters to the message on the underlying
   * form element.
   * 
   * @param error A localizable message code for error message (required)
   * @param params Optional message parameters.
   */
  void addError(String error, Object... params);

  /**
   * Registers errors on the underlying form element.
   * 
   * @param errors Errors to be registered on the form element.
   */
  void addErrors(Set<MessageData> errors);

  /**
   * The environment of the underlying form element.
   * 
   * @return The form element environment.
   */
  Environment getEnvironment();

  /**
   * The label of the underlying form element.
   * 
   * @return The form element label.
   */
  String getLabel();

  /**
   * Provides the current value of the underlying form element.
   * 
   * @return The current value of form element.
   */
  D getValue();

  /**
   * Provides the control of the underlying form element.
   * 
   * @return The control of form element.
   * @since 1.1
   */
  Control<C> getControl();

  /**
   * Provides the value converter of the underlying form element.
   * 
   * @return The value converter of form element.
   * @since 1.1
   */
  Converter<C, D> getConverter();
}
