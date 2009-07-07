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

/**
 * This class represents a further concretezation of {@link StringArrayRequestControl}, i.e. it
 * represents controls, that have a single <code>String</code> request parameter.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class StringRequestControl extends StringArrayRequestControl {

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************

  /**
   * This is just a proxy to {@link #fromRequest(String)}.
   */
  protected Object fromRequestParameters(String[] parameterValues) {
    return fromRequest(parameterValues[0]);
  }

  /**
   * This is just a proxy to {@link #preprocessRequestParameter(String)}.
   */
  protected String[] preprocessRequestParameters(String[] parameterValues) {
    String result = preprocessRequestParameter(parameterValues == null ? null : parameterValues[0]);
    return result == null ? null : new String[] { result };
  }

  /**
   * This is just a proxy to {@link #toResponse(Object)}.
   */
  protected String[] toResponseParameters(Object controlValue) {
    String result = toResponse(controlValue);
    return result == null ? null : new String[] { result };
  }

  // *********************************************************************
  // * ABSTRACT METHODS
  // *********************************************************************

  /**
   * This method should preprocess the <code>parameterValue</code> and return the processed value.
   * It may be used to <i>normalize</i> the request making the further parsing of it easier.
   * 
   * @param parameterValue The <code>String</code> value from request.
   * @return The preprocessed value from request.
   */
  protected abstract String preprocessRequestParameter(String parameterValue);

  /**
   * This method should parse the request parameters (preprocessed with
   * {@link #preprocessRequestParameter(String)}) an produce the control value.
   * 
   * @param parameterValue The request parameter.
   * @return The value of this control.
   */
  protected abstract Object fromRequest(String parameterValue);

  /**
   * This method should return the <code>String</code> representation of the control value. This
   * value is used for outputting it to the resulting page once requested.
   * 
   * @param controlValue The control value.
   * @return The <code>String</code> representation of the control value.
   */
  protected abstract String toResponse(Object controlValue);
}
