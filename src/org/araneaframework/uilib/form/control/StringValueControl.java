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
import org.araneaframework.uilib.support.DataType;
import org.araneaframework.uilib.support.UiLibMessages;

/**
 * This class represents controls, that have a value of type <code>String</code> and a single request parameter of same
 * type.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class StringValueControl extends BlankStringNullableControl<String> {

  private Long minLength;

  private Long maxLength;

  private boolean trimValue = false;

  /**
   * Sets the maximum length. A <code>null</code> value means that the maximum length check is not enforced. Any numeric
   * value will be compared with the length property of a <code>String</code>. By default, no length checks are done.
   * 
   * @param maxLength The maximum length to be used for validating input <code>String</code>s.
   */
  public void setMaxLength(Long maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * Sets the minimum length. A <code>null</code> value means that the minimum length check is not enforced. Any numeric
   * value will be compared with the length property of a <code>String</code>. By default, no length checks are done.
   * 
   * @param minLength The minimum length to be used for validating input <code>String</code>s.
   */
  public void setMinLength(Long minLength) {
    this.minLength = minLength;
  }

  /**
   * Sets whether the value from request will be trimmed (default: <code>false</code>).
   * 
   * @param trimValue Whether the value from request will be trimmed.
   */
  public void setTrimValue(boolean trimValue) {
    this.trimValue = trimValue;
  }

  public DataType getRawValueType() {
    return new DataType(String.class);
  }

  @Override
  protected String fromRequest(String parameterValue) {
    return this.trimValue ? StringUtils.trimToNull(parameterValue) : parameterValue;
  }

  @Override
  protected String toResponse(String controlValue) {
    return controlValue;
  }

  /**
   * Checks that the value (<code>String</code>) length is between the given values.
   */
  @Override
  protected void validateNotNull() {
    if (this.minLength != null && getRawValue().length() < this.minLength.longValue()) {
      addErrorWithLabel(UiLibMessages.STRING_TOO_SHORT, this.minLength.toString());
    }

    if (this.maxLength != null && getRawValue().length() > this.maxLength.longValue()) {
      addErrorWithLabel(UiLibMessages.STRING_TOO_LONG, this.maxLength.toString());
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * The view model implementation of <code>StringValueControl</code>. The view model provides the data for tags to
   * render the control.
   * 
   * @author <a href="mailto:olegm@webmedia.ee">Oleg Mürk</a>
   */
  public class ViewModel extends StringArrayRequestControl<String>.ViewModel {

    private Long minLength;

    private Long maxLength;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.minLength = StringValueControl.this.minLength;
      this.maxLength = StringValueControl.this.maxLength;
    }

    /**
     * Returns the minimum allowed length for the input.
     * 
     * @return the minimum allowed length for the input.
     */
    public Long getMinLength() {
      return this.minLength;
    }

    /**
     * Returns the maximum allowed length for the input.
     * 
     * @return the maximum allowed length for the input.
     */
    public Long getMaxLength() {
      return this.maxLength;
    }
  }
}
