/*
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
 */

package org.araneaframework.uilib.form.control.inputfilter;

import java.io.Serializable;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * The object that stores information about which characters the response must not contain. If it contains prohibited
 * characters, validation will fail, and an error message will be shown. Since the input filter is one per text control,
 * each control can have a different error message as specified through {@link #setInvalidInputCustomMessage(String)}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @see TextControl#setInputFilter(InputFilter)
 * @since 1.0.11
 */
public class InputFilter implements Serializable {

  /**
   * Custom HTML attribute for defining filter applied to input field.
   */
  public static final String CHARACTER_FILTER_ATTRIBUTE = "arn-charFilter";

  private String characterFilter;

  private String invalidInputCustomMessage;

  public InputFilter() {}

  /**
   * Creates <code>InputFilter</code> and also sets the string of characters to filter out.
   * 
   * @param characterFilter A string of characters that the response must not contain.
   */
  public InputFilter(String characterFilter) {
    this.characterFilter = characterFilter;
  }

  /**
   * Creates <code>InputFilter</code> and also sets the string of characters to filter out and the error message to
   * show when the response contains prohibited characters.
   * 
   * @param characterFilter A string of characters that the response must not contain.
   * @param invalidInputCustomMessage The error message to show.
   */
  public InputFilter(String characterFilter, String invalidInputCustomMessage) {
    this.characterFilter = characterFilter;
    this.invalidInputCustomMessage = invalidInputCustomMessage;
  }

  public String getCharacterFilter() {
    return this.characterFilter;
  }

  public void setCharacterFilter(String characterFilter) {
    this.characterFilter = characterFilter;
  }

  public String getInvalidInputCustomMessage() {
    return this.invalidInputCustomMessage;
  }

  public void setInvalidInputCustomMessage(String invalidInputCustomMessage) {
    this.invalidInputCustomMessage = invalidInputCustomMessage;
  }

  public String getInvalidInputMessage() {
    return this.invalidInputCustomMessage == null ? UiLibMessages.INPUT_FILTER_NOT_MATCHED
        : this.invalidInputCustomMessage;
  }
}
