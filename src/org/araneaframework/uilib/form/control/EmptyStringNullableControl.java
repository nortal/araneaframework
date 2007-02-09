/**
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
**/

package org.araneaframework.uilib.form.control;

import org.apache.commons.lang.StringUtils;

/**
 * This is a simple class that should be inherited by those controls,
 * which find that blank <code>String</code> in request is same as 
 * <code>null</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * TODO: rename to BlankStringNullableControl in Aranea 2.0
 */
public abstract class EmptyStringNullableControl extends StringRequestControl {
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	

  /**
   * Preprocesses the request parameter, substituting empty or blank String 
   * with null.
   */
  protected String preprocessRequestParameter(String parameterValue) {
    if (StringUtils.isBlank(parameterValue)) {
      return null;
    }
    return parameterValue;
  }
  

	public boolean isRead() {
		// semantically this is not right, as innerData may be not null when value was not
		// actually read from current request: ie in cases when update() is not called. 
		return (innerData != null && ((String[]) innerData)[0].trim().length() != 0);
	}

}
