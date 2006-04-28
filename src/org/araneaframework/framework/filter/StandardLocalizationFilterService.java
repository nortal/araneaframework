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

package org.araneaframework.framework.filter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * Enriches the environment with an implementation of the
 * {@link org.araneaframework.framework.LocalizationContext}. Children can use it and thus
 * provide Locale specific content. 
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardLocalizationFilterService extends BaseFilterService implements LocalizationContext {
  public static final String RESOURCE_BUNDLE_KEY = "org.araneaframework.framework.filter.StandardLocalizationFilterService.ResourceBundle";
  
  private static final Logger log = Logger.getLogger(StandardLocalizationFilterService.class);
  private String resourceBundleName;
  private Locale currentLocale;

  /**
   * Set the name of the language, it must be a <b>valid ISO Language Code</b>. See the
   * language name in {@link Locale}.
   */
  public void setLanguageName(String languageName) {
    setLocale(new Locale(languageName));
  }
  
  /**
   * Sets the name of the resource bundle. 
   * @param resourceBundleName
   */
  public void setResourceBundleName(String resourceBundleName) {
    this.resourceBundleName = resourceBundleName;
  }

  public Locale getLocale() {
    return currentLocale;
  }

  public void setLocale(Locale currentLocale) {
    log.debug("Current locale switched to:" + currentLocale);
    this.currentLocale = currentLocale;
  }
  
  public ResourceBundle getResourceBundle() {
    return getResourceBundle(currentLocale);
  }

  protected void init() throws Exception {
    Map entries = new HashMap();
    entries.put(LocalizationContext.class, this);
    
    childService._getComponent().init(new StandardEnvironment(getChildEnvironment(), entries));
  }
  
  /** 
   * Gets a resource bundle using the specified resource bundle name and current locale,
   * and the caller's class loader.
   */
  public ResourceBundle getResourceBundle(Locale locale) {
     return ResourceBundle.getBundle(resourceBundleName, locale, Thread.currentThread().getContextClassLoader());
  }

  public String localize(String key) {
    return getResourceBundle().getString(key);
  }
}
