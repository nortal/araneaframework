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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.ClassLoaderUtil;
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
   * Gets a resource bundle using the specified resource bundle name and current locale
   * and the ClassLoaders provided by the ClassLoaderUtil.
   */
  public ResourceBundle getResourceBundle(Locale locale) {
	  List loaders = ClassLoaderUtil.getClassLoaders();
	  
	  for (Iterator iter = loaders.iterator(); iter.hasNext();) {
		ClassLoader loader = (ClassLoader) iter.next();
		try {
			return ResourceBundle.getBundle(resourceBundleName, locale, loader);
		}
		catch(MissingResourceException e) {
			if (!iter.hasNext())
				throw e;
		}
	  }
     throw new MissingResourceException("No resource bundle for the specified base name can be found",
    		 							getClass().getName(), "");
  }

  public String localize(String key) {
    return getResourceBundle().getString(key);
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
	output.pushAttribute(LOCALIZATION_CONTEXT_KEY, this);
	
	try {
		super.action(path, input, output);
	}
	finally {
		output.popAttribute(LOCALIZATION_CONTEXT_KEY);
	}
  }
}
