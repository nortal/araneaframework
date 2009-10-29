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

package org.araneaframework.uilib.form.control;

import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.TextType;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ValidationUtil;

/**
 * Class that represents a single-line text input control.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class TextControl extends StringValueControl implements FilteredInputControl<String> {

  private InputFilter inputFilter;

  protected TextType textType = TextType.TEXT;

  /**
   * Empty constructor.
   */
  public TextControl() {}

  /**
   * Makes a text control with specific type.
   * 
   * @param textType specific type.
   */
  public TextControl(TextType textType) {
    this(textType, null, null);
  }

  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   */
  public TextControl(Long minLength, Long maxLength) {
    this(minLength, maxLength, false);
  }

  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   * @param trimValue whether the value from request will be trimmed.
   */
  public TextControl(Long minLength, Long maxLength, boolean trimValue) {
    this(TextType.TEXT, minLength, maxLength, trimValue);
  }

  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param textType specific type.
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   */
  public TextControl(TextType textType, Long minLength, Long maxLength) {
    this(textType, minLength, maxLength, false);
  }

  /**
   * Makes a text control with specific type, minimum and maximum length constraints, and trimming constraints.
   * 
   * @param textType specific type.
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   * @param trimValue whether the value from request will be trimmed.
   */
  public TextControl(TextType textType, Long minLength, Long maxLength, boolean trim) {
    setTextType(textType);
    setMinLength(minLength);
    setMaxLength(maxLength);
    setTrimValue(trim);
  }

  /**
   * Sets the specific expected text type for validating the input text.
   * 
   * @param textType The specific expected text type for validating the input text.
   */
  public void setTextType(TextType textType) {
    this.textType = textType;
  }

  /**
   * Returns the type of text for this <code>TextControl</code>. The returned type is used for validating the input
   * text.
   * 
   * @return the <code>TextType</code> of this <code>TextControl</code>.
   * @since 1.2.1
   */
  public TextType getTextType() {
    return this.textType;
  }

  public InputFilter getInputFilter() {
    return this.inputFilter;
  }

  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }

  /**
   * In case text control type is other than {@link TextType#TEXT} makes custom checks.
   */
  @Override
  protected void validateNotNull() {
    super.validateNotNull();

    if (this.textType.equals(TextType.NUMBER_ONLY)) {
      if (!ValidationUtil.isNumeric(getRawValue())) {
          addErrorWithLabel(UiLibMessages.NOT_A_NUMBER);
      }
    } else if (this.textType.equals(TextType.EMAIL)) {
      if (!ValidationUtil.isEmail(getRawValue())) {
        addErrorWithLabel(UiLibMessages.NOT_AN_EMAIL);
      }
    }

    if (getInputFilter() != null && !StringUtils.containsOnly(this.value, getInputFilter().getCharacterFilter())) {
      addErrorWithLabel(getInputFilter().getInvalidInputMessage(), getInputFilter().getCharacterFilter());
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * The view model implementation of <code>TextControl</code>. The view model provides the data for tags to render the
   * control.
   * 
   * @author <a href="mailto:olegm@webmedia.ee">Oleg Mürk</a>
   */
  public class ViewModel extends StringValueControl.ViewModel {

    protected String textType;

    protected InputFilter inputFilter;

    protected ViewModel() {
      this.textType = TextControl.this.textType.getName();
      this.inputFilter = TextControl.this.getInputFilter();
    }

    /**
     * The name of the text type that this <code>TextControl</code> accepts.
     * 
     * @return The name of the text type that this <code>TextControl</code> accepts.
     * @see TextType
     */
    public String getTextType() {
      return this.textType;
    }

    /**
     * The expected input filtering settings of this <code>TextControl</code>. If <code>null</code> then no filtering
     * should be done.
     * 
     * @return The expected input filtering settings of this <code>TextControl</code>.
     */
    public InputFilter getInputFilter() {
      return this.inputFilter;
    }
  }
}
