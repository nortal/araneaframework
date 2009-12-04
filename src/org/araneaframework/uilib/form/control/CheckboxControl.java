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

import org.araneaframework.uilib.support.DataType;

/**
 * This control represents a checkbox.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class CheckboxControl extends StringRequestControl<Boolean> {

  /**
   * Returns a <code>DataType</code> describing a {@link Boolean}.
   * 
   * @return A <code>DataType</code> describing a {@link Boolean}.
   */
  public DataType getRawValueType() {
    return new DataType(Boolean.class);
  }

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  /**
   * This method preprocesses request, interpreting it as following:
   * <ul>
   * <li>if there's a not <code>null</code> request parameter, it's substituted with "true"
   * <li>if there isn't a request parameter, it's substituted with "false"
   * </ul>
   * 
   * @return A <code>String</code> holding either "true" or "false".
   */
  @Override
  protected String preprocessRequestParameter(String parameterValue) {
    return Boolean.toString(parameterValue != null);
  }

  /**
   * This method makes a <code>Boolean</code> out of a <code>String</code>.
   * 
   * @param parameterValue The value from request that is parsed and evaluated as a <code>Boolean</code>.
   * @return <code>true</code> if the parameter value is equal to "true" (ignoring case). Otherwise, <code>false</code>.
   */
  @Override
  protected Boolean fromRequest(String parameterValue) {
    return Boolean.valueOf(parameterValue);
  }

  /**
   * This method makes a <code>String</code> ("true","false") out of a <code>Boolean</code>.
   */
  @Override
  protected String toResponse(Boolean controlValue) {
    return Boolean.toString(controlValue.booleanValue());
  }
}
