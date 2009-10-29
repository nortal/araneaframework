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

package org.araneaframework.http.support;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.core.util.ClassLoaderUtil;

/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 * @since {since}
 */
public class ReloadingPropertyFileResourceBundle extends LocaleAwareResourceBundle {

  private static final Log LOG = LogFactory.getLog(ReloadingPropertyFileResourceBundle.class);

  protected String propertyResource = "resources";

  protected long lastReloadTime;

  protected long checkTime;

  protected long lastModified = 0;

  protected Map<String, String> properties = new HashMap<String, String>();

  public ReloadingPropertyFileResourceBundle() {}

  public ReloadingPropertyFileResourceBundle(String propertyResource) {
    this.propertyResource = propertyResource;
  }

  @Override
  protected Object handleGetObject(String key) {
    if (key == null) {
      throw new NullPointerException();
    }

    if (this.checkTime != -1 && System.currentTimeMillis() - this.lastReloadTime > this.checkTime) {
      reloadProperties();
    }

    return handleGetCachedObject(key);
  }

  private void reloadProperties() {
    try {
      String resourceName = this.propertyResource + "_" + getLocale().getLanguage() + ".properties";
      URL propertyURL = ClassLoaderUtil.findResource(resourceName);

      if (propertyURL == null) {
        resourceName = this.propertyResource + ".properties";
        propertyURL = ClassLoaderUtil.findResource(resourceName);
      }

      if (propertyURL == null) {
        LOG.warn("Localization resources for '" + getLocale().getLanguage() + "' were not found!");
        return;
      }

      File propertyFile = new File(propertyURL.getFile());

      if (this.lastModified < propertyFile.lastModified()) {

        LOG.debug("Reloading localization data from property file '" + propertyFile + "'.");

        Properties result = new Properties();
        result.load(new FileInputStream(propertyURL.getFile()));

        loadData(result);

        this.lastReloadTime = System.currentTimeMillis();
        this.lastModified = propertyFile.lastModified();
      }
    } catch (RuntimeException re) {
      throw re;
    } catch (Exception e) {
      throw new NestableRuntimeException(e);
    }
  }

  protected Object handleGetCachedObject(String key) {
    return this.properties.get(key);
  }

  protected void loadData(Properties newProperties) throws Exception {
    this.properties = new HashMap<String, String>();

    for (Enumeration<?> i = newProperties.propertyNames(); i.hasMoreElements();) {
      String key = (String) i.nextElement();
      String value = newProperties.getProperty(key);
      this.properties.put(key, value);
    }
  }

  @Override
  public Enumeration<String> getKeys() {
    return null;
  }
}
