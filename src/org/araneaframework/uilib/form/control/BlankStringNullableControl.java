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
 * This is a simple class that should be inherited by those controls, which find that blank <code>String</code> in
 * request is same as <code>null</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class BlankStringNullableControl<T> extends StringRequestControl<T> {

  // *********************************************************************
  // * INTERNAL METHODS
  // *********************************************************************


  @Override
  protected String preprocessRequestParameter(String parameterValue) {
    return StringUtils.defaultIfEmpty(parameterValue, null);
  }

  @Override
  public boolean isRead() {
    // semantically this is not right, as innerData may be not null when value was not
    // actually read from current request: i.e. in cases when update() is not called.
    return this.innerData != null && ((String[]) this.innerData)[0].trim().length() != 0;
  }
}