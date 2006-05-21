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

package org.araneaframework.jsp.util;

import java.io.IOException;
import java.io.Writer;


/**
 * UiStd script utilities.
 * 
 * @author Oleg Mürk
 */
public class UiStdScriptUtil {
  /**
   * Writes out event handling attribute that does nothing.
   */ 
  public static void writeEmptyEventAttribute(Writer out, String attributeName) throws IOException {
    UiUtil.writeOpenAttribute(out, attributeName);    
    out.write("javascript: return false;");
    UiUtil.writeCloseAttribute(out);
  }
  
  /**
   * Writes 'undefined' or object's string representation. 
   *
   * @author Konstantin Tretyakov
   */
  public static void writeObject(Writer out, Object o) throws IOException {
    out.write(o == null ? "undefined" : o.toString());
  }
  
  /**
   * Constructs <code>StringBuffer</code> containing javascript for invoking JS function with given arguments.
   * 
   * @param functionName JS function name
   * @param arguments
   * @return javascript for invoking function with given arguments. 
  */
  public static StringBuffer constructJSFunctionCall(String functionName, String[] arguments) {
    StringBuffer sb = new StringBuffer();
    sb.append(functionName);
    sb.append('(');
    for (int i = 0; i < arguments.length; i++) {
      if (i > 0)
        sb.append(", ");
      sb.append(arguments[i]);
    }
    sb.append(')');
    
    return sb;
  }
}
