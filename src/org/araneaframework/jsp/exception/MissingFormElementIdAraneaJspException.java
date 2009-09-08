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

package org.araneaframework.jsp.exception;

/**
 * Exception thrown when some tag that expects to operate on some
 * {@link org.araneaframework.uilib.form.FormElement} does not know
 * {@link org.araneaframework.uilib.form.FormElement} id, and cannot deduce it
 * from pagecontext either.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class MissingFormElementIdAraneaJspException extends AraneaJspException {

  private static final long serialVersionUID = 1L;

  public MissingFormElementIdAraneaJspException(Object object) {
    super("You must set the 'id' either directly or using 'ui:formElement' "
        + "for tag '" + object.getClass() + "'");
  }
}
