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

/**
 * Utility to determine the class-loader that should be used for loading resources.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public abstract class ClassLoaderUtil {

  /**
   * Instantiating this class is prohibited.
   */
  private ClassLoaderUtil() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the thread context ClassLoader, if available. Otherwise falls back to the ClassLoader of ClassLoaderUtil.
   * 
   * @return The acquired class-loader.
   */
  public static ClassLoader getDefaultClassLoader() {
    return getClassLoaders().get(0);
  }

  /**
   * Provides an input stream for a resource from class-path.
   * 
   * @param name The path to a resource (required).
   * @return An input stream to the found resource, or <code>null</code> when the resource was not found.
   */
  public static InputStream getResourceAsStream(String name) {
    URL url = findResource(name);
    if (url == null) {
      return null;
    }
    try {
      return url.openStream();
    } catch (IOException e) {
      return null;
    }
  }

  /**
   * Tries to load class using the ClassLoaders in the order that getClassLoaders() returns them. On success returns the
   * class.
   * 
   * @param name The complete class name to load.
   * @return The matching class that was found.
   * @throws ClassNotFoundException When the class was not found in the class-path.
   */
  public static Class<?> loadClass(String name) throws ClassNotFoundException {
    Assert.notNullParam(name, "name");
    for (Iterator<ClassLoader> iter = getClassLoaders().iterator(); iter.hasNext();) {
      ClassLoader loader = iter.next();
      try {
        return loader.loadClass(name);
      } catch (ClassNotFoundException e) {
        if (!iter.hasNext()) {
          throw e;
        }
      }
    }
    throw new ClassNotFoundException();
  }

  /**
   * Searches through all the ClassLoaders provided by the getClassLoaders() for the resource identified by name.
   * Returns the URL of the first found resource.
   * 
   * @param name The path to a resource (required).
   * @return The URL for the resource, or <code>null</code> when the resource was not found.
   */
  public static URL findResource(String name) {
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
   * Searches through all the ClassLoaders provided by the getClassLoaders() for the resources identified by name.
   * Returns a union of all the found URLs.
   * 
   * @param name The path to a resource (required).
   * @return An enumeration of found URLs to given resource path.
   * @throws IOException When a problem occurs accessing a class-path resource.
   */
  @SuppressWarnings("unchecked")
  public static Enumeration<URL> findResources(final String name) throws IOException {
    Assert.notNullParam(name, "name");
    List<URL> list = new ArrayList<URL>();
    List<ClassLoader> loaders = getClassLoaders();
    for (ClassLoader loader : loaders) {
      Enumeration<URL> resources = loader.getResources(name);
      list.addAll(EnumerationUtils.toList(resources));
    }
    return Collections.enumeration(list);
  }

  /**
   * Returns a list of ClassLoaders in the order that Aranea searches for resources.
   * 
   * @return A list of found class-loaders.
   */
  public static List<ClassLoader> getClassLoaders() {
    List<ClassLoader> loaders = new ArrayList<ClassLoader>(2);
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader != null) {
      loaders.add(classLoader);
    }
    loaders.add(ClassLoaderUtil.class.getClassLoader());
    return loaders;
  }
}
