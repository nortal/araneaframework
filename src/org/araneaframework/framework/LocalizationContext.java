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
 * The current Locale can be changed with <code>setCurrentLocale(Locale)</code> and all
 * subsequent request to <code>getCurrentResourceBundle()</code> will use the new current Locale.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface LocalizationContext extends Serializable {
  public Locale getLocale();
  public void setLocale(Locale locale);
  
  public ResourceBundle getResourceBundle();
  public ResourceBundle getResourceBundle(Locale locale);
  
  public String localize(String key);
}
