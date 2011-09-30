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

package org.araneaframework.framework;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import org.araneaframework.core.ApplicationComponent;

/**
 * A context for providing localization via exposing the current {@link Locale} and {@link ResourceBundle}s. The
 * ResourceBundle of a different Locale can be accessed without chaning the current Locale with
 * <code>getResourceBundle(Locale)</code>. The current Locale can be changed with <code>setLocale(Locale)</code> and all
 * subsequent request to <code>getResourceBundle()</code> will use the new current Locale.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface LocalizationContext extends Serializable {

  /**
   * Returns the current session locale.
   * 
   * @return The currently used locale.
   */
  Locale getLocale();

  /**
   * Sets the current session locale.
   * 
   * @param locale The locale to use from now on.
   */
  void setLocale(Locale locale);

  /**
   * Localizes a string returning one that corresponds to the current locale.
   * 
   * @param key The key for which a localized resource will be returned.
   * @return The localized resource (may be <code>null</code>).
   */
  String localize(String key);

  /**
   * Returns a resource bundle corresponding to the current locale.
   * 
   * @return The currently used resource bundle.
   */
  ResourceBundle getResourceBundle();

  /**
   * Returns a resource bundle corresponding to arbitrary locale.
   * <p>
   * Throws <tt>MissingResourceException</tt> when no resource bundle is found for the specified locale.
   * 
   * @param locale The locale for which a resource bundle will be returned.
   * @return The resource bundle for the specified locale.
   * @see java.util.MissingResourceException
   */
  ResourceBundle getResourceBundle(Locale locale);

  /**
   * Localizes the code and uses it to format the message with the passed arguments. The format of the localized message
   * should be acceptable by <code>java.text.MessageFormat</code>. If the localized message cannot be resolved uses
   * <code>defaultMessage</code> instead.
   * 
   * @param code The key used for finding the localized message.
   * @param defaultMessage The default message used when no localized message was found for <tt>code</tt>.
   * @param args Optional arguments to be used in the message (where specified by place-holders).
   * @return The localized message with arguments injected where applicable.
   * @see java.text.MessageFormat
   */
  String getMessage(String code, String defaultMessage, Object... args);

  /**
   * Localizes the code and uses it to format the message with the passed arguments. The format of the localized message
   * should be acceptable by <code>java.text.MessageFormat</code>.
   * 
   * @param code The key used for finding the localized message.
   * @param args Optional arguments to be used in the message (where specified by place-holders).
   * @return The localized message with arguments injected where applicable.
   * @see java.text.MessageFormat
   */
  String getMessage(String code, Object... args);

  /**
   * Registers a locale change listener.
   * 
   * @param listener A listener to be called on locale change.
   * @since 1.1
   */
  void addLocaleChangeListener(LocaleChangeListener listener);

  /**
   * Unregisters a locale change listener.
   * 
   * @param listener A listener to be unregistered.
   * @return A Boolean that is <code>true</code> when the listener was unregistered.
   * @since 1.1
   */
  boolean removeLocaleChangeListener(LocaleChangeListener listener);

  /**
   * Contract interface for locale change listeners.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   * @since 1.1
   */
  static interface LocaleChangeListener extends ApplicationComponent {

    /**
     * Callback method that is called when locale changes.
     * 
     * @param oldLocale The previous locale.
     * @param newLocale The new locale.
     */
    void onLocaleChange(Locale oldLocale, Locale newLocale);
  }
}
