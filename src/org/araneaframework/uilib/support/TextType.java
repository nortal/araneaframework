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

package org.araneaframework.uilib.support;

import java.io.Serializable;
import org.apache.commons.lang.enums.Enum;


/**
 * Specifies the text type for {@link org.araneaframework.uilib.form.control.TextControl}.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class TextType extends Enum implements Serializable {

  // Please note that the string values are used in javascript.
  // So you can't change them.
  
  /**
   * Any text. 
   */
  public final static TextType TEXT = new TextType("TEXT");  
  
  /**
   * String containing numbers only. 
   */
  public final static TextType NUMBER_ONLY = new TextType("NUMBER_ONLY");   
  
  /**
   * E-mail.
   */
  public final static TextType EMAIL = new TextType("EMAIL");  
  
  
  private TextType(String textType) {
    super(textType);
  }
  
}
