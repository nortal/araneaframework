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

package org.araneaframework.uilib.util;

import java.text.MessageFormat;
import org.araneaframework.Environment;
import org.araneaframework.framework.LocalizationContext;

public class MessageUtil {
  public static String localize(String messageCode, Environment env) {
    LocalizationContext locCtx = 
      (LocalizationContext) env.getEntry(LocalizationContext.class);
    return locCtx.getResourceBundle().getString(messageCode);    
  }
  
  public static String format(String message, Object parameter) {
    return format(message, new Object[] {parameter});
  }  
  
  public static String format(String message, Object parameter1, Object parameter2) {
    return format(message, new Object[] {parameter1, parameter2});
  }    
  
  public static String format(String message, Object[] parameters) {
    return MessageFormat.format(message, parameters);
  }
  
  public static String localizeAndFormat(String message, Object parameter, Environment env) {
    return localizeAndFormat(message, new Object[] {parameter}, env);
  }
  
  public static String localizeAndFormat(String message, Object parameter1, Object parameter2, Environment env) {
    return localizeAndFormat(message, new Object[] {parameter1, parameter2}, env);
  }
  
  public static String localizeAndFormat(String message, Object[] parameters, Environment env) {
    return format(localize(message, env), parameters);
  }
}
