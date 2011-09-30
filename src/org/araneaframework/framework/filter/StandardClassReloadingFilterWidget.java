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

package org.araneaframework.framework.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.servlet.ServletContext;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.RelocatableDecorator;
import org.araneaframework.core.util.ClassLoaderUtil;

/**
 * A filter widget to simplify development through class reloading. This filter widget is not used by default, so
 * configuration must be changed to use this filter.
 * <p>
 * This widget depends on {@link ServletContext} to be available through environment.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardClassReloadingFilterWidget extends BaseApplicationWidget {

  /**
   * The default location for monitoring resource changes.
   */
  protected static final String RESOURCE_LOC = "/WEB-INF/classes";

  private static final Log LOG = LogFactory.getLog(StandardClassReloadingFilterWidget.class);

  private static final int BUF_SIZE = 512;

  private String childClassName;

  private RelocatableWidget child;

  /**
   * Sets the name of the child widget class name to use.
   * 
   * @param childClass The full name of the widget component class name to use.
   */
  public void setChildClass(String childClass) {
    this.childClassName = childClass;
  }

  @Override
  protected void init() throws Exception {
    Class<?> childClass = ClassUtils.getClass(newClassLoader(), this.childClassName);
    addWidget("c", new RelocatableDecorator((Widget) childClass.newInstance()));
  }

  @Override
  protected void update(InputData input) throws Exception {
    super.update(input);
    try {
      this.child._getRelocatable().overrideEnvironment(null);
      this.child = (RelocatableWidget) reload(this.child);
    } catch (ClassNotFoundException e) {
      LOG.error("Failed to reload widget classes", e);
    } finally {
      this.child._getRelocatable().overrideEnvironment(getEnvironment());
    }
    _getComposite().attach("c", this.child);
  }

  @Override
  protected void render(OutputData output) throws Exception {
    this.child._getWidget().render(output);
  }

  private ClassLoader newClassLoader() throws MalformedURLException {
    ServletContext sctx = getEnvironment().getEntry(ServletContext.class);
    return new ReloadingClassloader(new URL[] { sctx.getResource(RESOURCE_LOC) },
        ClassLoaderUtil.getDefaultClassLoader());
  }

  private Serializable reload(Serializable child) throws Exception {
    return deepCopy(newClassLoader(), child);
  }

  private Serializable deepCopy(ClassLoader cl, Serializable original) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(BUF_SIZE);
    ObjectOutputStream out = new ObjectOutputStream(baos);
    try {
      out.writeObject(original);
    } finally {
      out.close();
    }

    byte[] serialized = baos.toByteArray();

    ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
    ReloadingObjectInputStream in = new ReloadingObjectInputStream(cl, bais);
    return (Serializable) in.readObject();
  }

  private boolean hasLoadedClass(ClassLoader cl, String name) {
    try {
      if (MethodUtils.invokeExactMethod(cl, "findLoadedClass", name) != null) {
        return true;
      }

      Method mGetParent = ClassLoader.class.getDeclaredMethod("getParent", new Class[] {});
      ClassLoader parent = (ClassLoader) mGetParent.invoke(cl, "getParent");

      return parent != null ? hasLoadedClass(parent, name) : false;
    } catch (Exception e) {
      throw new NestableRuntimeException(e);
    }
  }

  private static class ReloadingObjectInputStream extends ObjectInputStream {

    private final ClassLoader cl;

    public ReloadingObjectInputStream(ClassLoader cl, InputStream in) throws IOException {
      super(in);
      this.cl = cl;
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws ClassNotFoundException {
      return this.cl.loadClass(desc.getName());
    }
  }

  private class ReloadingClassloader extends URLClassLoader {

    public ReloadingClassloader(URL[] urls, ClassLoader parent) {
      super(urls, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
      if (hasLoadedClass(this, name)) {
        return super.loadClass(name);
      }

      try {
        return findClass(name);
      } catch (ClassNotFoundException e) {
        return getParent().loadClass(name);
      }
    }
  }
}
