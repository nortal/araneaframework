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

package org.araneaframework.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.EnumerationUtils;
import org.araneaframework.core.Assert;

/**
 * Utility to determine the classloader that should be used for loading
 * resources.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class ClassLoaderUtil {

  /**
   * Returns the thread context ClassLoader, if available. Otherwise falls back
   * to the ClassLoader of ClassLoaderUtil.
   * 
   * @return acquired classloader.
   */
  public static ClassLoader getDefaultClassLoader() {
    return getClassLoaders().get(0);
  }

  public static InputStream getResourceAsStream(String name) {
    Assert.notNullParam(name, "name");
    URL url = findResource(name);
    if (url == null)
      return null;
    try {
      return url.openStream();
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Trys to load class using the ClassLoaders in the order that
   * getClassLoaders() returns them. On success returns the class.
   * 
   * @throws ClassNotFoundException
   */
  public static Class<?> loadClass(String name) throws ClassNotFoundException {
    Assert.notNullParam(name, "name");
    for (Iterator<ClassLoader> iter = getClassLoaders().iterator(); iter.hasNext();) {
      ClassLoader loader = iter.next();
      try {
        return loader.loadClass(name);
      } catch (ClassNotFoundException e) {
        if (!iter.hasNext())
          throw e;
      }
    }
    throw new ClassNotFoundException();
  }

  /**
   * Searches through all the ClassLoaders provided by the getClassLoaders() for
   * the resource identified by name. Returns the URL of the first found
   * resource.
   */
  public static URL findResource(final String name) {
    Assert.notNullParam(name, "name");
    for (ClassLoader loader : getClassLoaders()) {
      URL url = loader.getResource(name);
      if (url != null) {
        return url;
      }
    }
    return null;
  }

  /**
   * Searches through all the ClassLoaders provided by the getClassLoaders() for
   * the resources identified by name. Returns an union of all the found URLs.
   */
  @SuppressWarnings("unchecked")
  public static Enumeration<ClassLoader> findResources(final String name) throws IOException {
    Assert.notNullParam(name, "name");
    List<URL> list = new ArrayList<URL>();
    List<ClassLoader> loaders = getClassLoaders();
    for (ClassLoader loader : loaders) {
      Enumeration<URL> resources = loader.getResources(name);
      list.addAll(EnumerationUtils.toList(resources));
    }
    return Collections.enumeration(loaders);
  }

  /**
   * Returns a list of ClassLoaders in the order that Aranea searches for
   * resources.
   */
  public static List<ClassLoader> getClassLoaders() {
    List<ClassLoader> loaders = new ArrayList<ClassLoader>();
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader != null) {
      loaders.add(classLoader);
    }
    loaders.add(ClassLoaderUtil.class.getClassLoader());
    return loaders;
  }
}
