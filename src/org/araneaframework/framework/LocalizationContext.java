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

package org.araneaframework.framework;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A context for providing localization via exposing the current
 * Locale and ResourceBundles. The ResourceBundle of a different Locale
 * can be accessed without chaning the current Locale with <code>getResourceBundle(Locale)</code>.
 * The current Locale can be changed with <code>setLocale(Locale)</code> and all
 * subsequent request to <code>getResourceBundle()</code> will use the new current Locale.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface LocalizationContext extends Serializable {

  /**
   * Returns the current session locale.
   */
  public Locale getLocale();
  
  /**
   * Sets the current session locale.
   */
  public void setLocale(Locale locale);
  
  
  
  /**
   * Localizes a string returning one that corresponds to the current locale.
   */
  public String localize(String key);
  
  /**
   * Returns a resource bundle corresponding to the current locale.
   */
  public ResourceBundle getResourceBundle();
  
  /**
   * Returns a resource bundle corresponding to arbitrary locale.
   */
  public ResourceBundle getResourceBundle(Locale locale);
  
  
  
  /**
   * Localizes the code and uses it to format the message with the passed arguments. 
   * The format of the localized message should be acceptable by <code>java.text.MessageFormat</code>.
   * If the localized message cannot be resolved uses <code>defaultMessage</code> instead.
   */
  public String getMessage(String code, Object[] args, String defaultMessage);
  
  /**
   * Localizes the code and uses it to format the message with the passed arguments. 
   * The format of the localized message should be acceptable by <code>java.text.MessageFormat</code>.
   */
  public String getMessage(String code, Object[] args);  
}
