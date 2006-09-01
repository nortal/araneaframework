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

package org.araneaframework.http.support;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * ResourceBundle that which {@link FallbackResourceBundle#handleGetObject(String)} searches
 * for <code>key</code> from all added <code>ResourceBundles</code>.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class FallbackResourceBundle extends ResourceBundle implements Serializable {
  protected Locale locale;
  protected List resourceBundles = new ArrayList();
  
  /**
   * Adds a <code>ResourceBundle</code> from which resources are searched.
   * @param resourceBundle
   */
  public void addResourceBundle(ResourceBundle resourceBundle) {
    resourceBundles.add(resourceBundle);
    
    if (resourceBundle instanceof LocaleAwareResourceBundle) 
      ((LocaleAwareResourceBundle) resourceBundle).setLocale(locale);
  }
  
  public void clearResourceBundles() {
    resourceBundles.clear();
  }
  
  public Enumeration getKeys() {
    return null;
  }

  /** 
   * Returns the resource under the <code>key</code>. Resource is searched
   * among all <code>ResourceBundles</code> this {@link FallbackResourceBundle}
   * knows about, in same order as <code>ResourceBundles</code> were added.
   * 
   * @return the resource under the <code>key</code>
   */
  protected Object handleGetObject(String key) {
    if (locale == null)
      setLocale(getLocale());
    
    Object result = null;
    
    for (Iterator i = resourceBundles.iterator(); i.hasNext() && result == null; ) {      
      try {
        ResourceBundle currentBundle = (ResourceBundle) i.next(); 
        result = currentBundle.getObject(key);
      }
      catch (MissingResourceException e) {
        //Totally normal result
      }      
    }
    
    return result;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
    
    for (Iterator i = resourceBundles.iterator(); i.hasNext(); ) {      
      ResourceBundle currentBundle = (ResourceBundle) i.next();
      if (currentBundle instanceof LocaleAwareResourceBundle) 
        ((LocaleAwareResourceBundle) currentBundle).setLocale(locale);
    }      
  }
  
  public Locale getLocale() {
    if (locale != null)
        return locale;
    return super.getLocale();
  }

}
