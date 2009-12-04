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

/**
 * This class represents a textarea control.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class TextareaControl extends StringValueControl {

  /**
   * Empty constructor.
   */
  public TextareaControl() {}

  /**
   * Makes a text control with specific minimum and maximum length constraints.
   * 
   * @param minLength The minimum permitted length.
   * @param maxLength The maximum permitted length.
   */
  public TextareaControl(Long minLength, Long maxLength) {
    setMinLength(minLength);
    setMaxLength(maxLength);
  }

  /**
   * Makes a text control with specific minimum and maximum length constraints and trimming condition.
   * 
   * @param minLength The minimum permitted length.
   * @param maxLength The maximum permitted length.
   * @param trimValue Whether the value from request will be trimmed.
   */
  public TextareaControl(Long minLength, Long maxLength, boolean trimValue) {
    setMinLength(minLength);
    setMaxLength(maxLength);
    setTrimValue(trimValue);
  }

  /**
   * Takes away &lt;CR&gt; added by Intenet Explorer.
   */
  @Override
  protected String preprocessRequestParameter(String parameterValue) {
    return StringUtils.remove(super.preprocessRequestParameter(parameterValue), '\r');
  }
}
