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

package org.araneaframework.uilib;

import org.araneaframework.core.AraneaRuntimeException;

/**
 * This exception is thrown if one of the {@link org.araneaframework.uilib.form.FormWidget}'s <code>get/setByFullNane</code>
 * methods is called and it is given an invalid or not existing form element name.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class InvalidFormElementNameException extends AraneaRuntimeException {
  
  /**
   * Creates an exception, which means that {@link org.araneaframework.uilib.form.FormWidget} <code>get/setByFullname</code>
   * method was given an invalid or not existing form element name. 
   * @param formElementNameSuffix the suffix of the invalid or not existing form element name. 
   */
  public InvalidFormElementNameException(String formElementNameSuffix) {
    super("Could not find form element with name suffix '" + formElementNameSuffix + "'.");
  }
}
