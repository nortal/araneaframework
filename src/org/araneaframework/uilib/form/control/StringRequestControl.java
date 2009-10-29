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
 * This class represents a further concretization of {@link StringArrayRequestControl}, i.e. it represents controls,
 * that have a single <code>String</code> request parameter.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class StringRequestControl<T> extends StringArrayRequestControl<T> {

  /**
   * This is just a proxy to {@link #fromRequest(String)}.
   */
  @Override
  protected final T fromRequestParameters(String[] parameterValues) {
    return fromRequest(parameterValues[0]);
  }

  /**
   * This is just a proxy to {@link #preprocessRequestParameter(String)}.
   */
  @Override
  protected final String[] preprocessRequestParameters(String[] parameterValues) {
    String result = parameterValues == null ? preprocessRequestParameter(null)
        : preprocessRequestParameter(parameterValues[0]);
    return result == null ? null : new String[] { result };
  }

  /**
   * This is just a proxy to {@link #toResponse(Object)}.
   */
  @Override
  protected final String[] toResponseParameters(T controlValue) {
    String result = toResponse(controlValue);
    return result == null ? null : new String[] { result };
  }

  // *********************************************************************
  // * ABSTRACT METHODS
  // *********************************************************************

  /**
   * The request preprocess method for given request parameter value.
   * 
   * @param parameterValue The value read from request for this control. May be <code>null</code>.
   * @return The new value (either processed or the same) for this parameter. May be <code>null</code>.
   * @see StringArrayRequestControl#preprocessRequestParameters(String[])
   */
  protected abstract String preprocessRequestParameter(String parameterValue);

  /**
   * Converts the request value to the control value.
   * 
   * @param parameterValue The value read from request for this control. May be <code>null</code>.
   * @return The value for this control as parsed from the request. May be <code>null</code>.
   * @see StringArrayRequestControl#fromRequestParameters(String[])
   */
  protected abstract T fromRequest(String parameterValue);

  /**
   * Converts the control value to the request value.
   * 
   * @param controlValue The value to be converted to <code>String</code> that will be used as the rendered input value.
   *          May be <code>null</code>.
   * @return The value of this control as converted to <code>String</code>. May be <code>null</code>.
   * @see StringArrayRequestControl#toResponseParameters(E)
   */
  protected abstract <E extends T> String toResponse(E controlValue);
}
