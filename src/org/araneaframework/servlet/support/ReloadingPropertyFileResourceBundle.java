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

package org.araneaframework.servlet.support;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.araneaframework.core.util.ResourceResolverUtil;

/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 * @since {since}
 */
public class ReloadingPropertyFileResourceBundle extends LocaleAwareResourceBundle {
  
  private static Logger log = Logger.getLogger(ReloadingPropertyFileResourceBundle.class);

  protected String propertyResource = "resources";  

  protected long lastReloadTime;
  protected long checkTime;
  protected long lastModified = 0;
  
  protected Map properties = new HashMap();


  public ReloadingPropertyFileResourceBundle() {}
  
  public ReloadingPropertyFileResourceBundle(String propertyResource) {
    this.propertyResource = propertyResource;
  }
  
  protected Object handleGetObject(String key) {
    if (key == null) throw new NullPointerException();

    if (checkTime != -1 && System.currentTimeMillis() - lastReloadTime > checkTime) {
      reloadProperties();
    }

    return handleGetCachedObject(key);
  }

  private void reloadProperties() {
    try {
      String resourceName = propertyResource + "_" + getLocale().getLanguage() + ".properties";
      URL propertyURL = ResourceResolverUtil.getDefaultClassLoader().getResource(resourceName);

      if (propertyURL == null) {
        resourceName = propertyResource + ".properties";
        propertyURL = ResourceResolverUtil.getDefaultClassLoader().getResource(resourceName);
      }

      File propertyFile = new File(propertyURL.getFile());

      if (lastModified < propertyFile.lastModified()) {
        
        log.debug("Reloading localization data from property file '" + propertyFile + "'.");

        Properties result = new Properties();
        result.load(new FileInputStream(propertyURL.getFile()));   
        
        loadData(result);

        lastReloadTime = System.currentTimeMillis();
        lastModified = propertyFile.lastModified();
      }
    }
    catch (RuntimeException re) {
      throw re;
    }
    catch (Exception e) {
      throw new NestableRuntimeException(e);
    }
  }
  
  protected Object handleGetCachedObject(String key) {
    return properties.get(key);
  }

  protected void loadData(Properties newProperties) throws Exception {
    properties = new HashMap();

    for (Enumeration i = newProperties.propertyNames(); i.hasMoreElements();) {
      String key = (String) i.nextElement();
      String value = newProperties.getProperty(key);

      properties.put(key, value);
    }
  }
  
  public Enumeration getKeys() {
    return null;
  }
}
