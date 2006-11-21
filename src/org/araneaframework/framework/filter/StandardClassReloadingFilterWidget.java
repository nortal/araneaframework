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
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.RelocatableDecorator;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardClassReloadingFilterWidget extends BaseApplicationWidget {
  private static final Logger log = Logger.getLogger(StandardClassReloadingFilterWidget.class);
  
  private String childClassName;
  private RelocatableWidget child;

  public void setChildClass(String childClass) {
    this.childClassName = childClass;
  }
  
  protected void init() throws Exception {    
    super.init();  
    
    ClassLoader cl = newClassLoader();
    Class childClass = cl.loadClass(childClassName);

    child = new RelocatableDecorator((Widget) childClass.newInstance());
    child._getComponent().init(getEnvironment());    
    _getComposite().attach("c", child);
  }
  
  private ClassLoader newClassLoader() throws MalformedURLException {
    ServletContext sctx = (ServletContext) getEnvironment().getEntry(ServletContext.class);
    return new ReloadingClassloader(new URL[] {sctx.getResource("/WEB-INF/classes")}, getClass().getClassLoader());
  }
  
  private Serializable reload(Serializable child) throws Exception {
    return deepCopy(newClassLoader(), child);
  }
  
  protected void update(InputData input) throws Exception {
    super.update(input);
    try {
      child._getRelocatable().overrideEnvironment(null);
      child = (RelocatableWidget) reload(child);      
    }
    catch (ClassNotFoundException e) {
      log.error("Failed to reload widget classes", e);
    }
    finally {
      child._getRelocatable().overrideEnvironment(getEnvironment());
    }
    
    _getComposite().attach("c", child);
  }
  
  protected void render(OutputData output) throws Exception {
    try {
      output.pushScope("c");
      child._getWidget().render(output);
    }
    finally {
      output.popScope();
    }
  }
  
  private  Serializable deepCopy(ClassLoader cl, Serializable original) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
    ObjectOutputStream out = new ObjectOutputStream(baos);
    try {
      out.writeObject(original);
    }
    finally {
      out.close();
    }
    
    byte[] serialized = baos.toByteArray();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
    
    ReloadingObjectInputStream in = new ReloadingObjectInputStream(cl, bais);
    Object obj = in.readObject();
    
    return (Serializable) obj;
  }
 
  private static class ReloadingObjectInputStream extends ObjectInputStream {
    private ClassLoader cl;
    
    public ReloadingObjectInputStream(ClassLoader cl, InputStream in) throws IOException {
      super(in);
      this.cl = cl;
    }

    protected Class resolveClass(ObjectStreamClass desc) throws ClassNotFoundException {
      String name = desc.getName();
      return cl.loadClass(name);
    }
  }
  
  private class ReloadingClassloader extends URLClassLoader {

    public ReloadingClassloader(URL[] urls, ClassLoader parent) {
      super(urls, parent);
    }
    
    public Class loadClass(String name) throws ClassNotFoundException {
      if (hasLoadedClass(this, name))
        return super.loadClass(name);
      
      try {
        return findClass(name);
      }
      catch (ClassNotFoundException e) {
        return getParent().loadClass(name);
      }
    }
  }
  
  private boolean hasLoadedClass(ClassLoader cl, String name) {
    try {
      Method mFindLoadedClass = ClassLoader.class.getDeclaredMethod(
          "findLoadedClass", new Class[] { String.class });
      mFindLoadedClass.setAccessible(true);
      if (mFindLoadedClass.invoke(cl, new Object[] { name }) != null)
        return true;
      
      Method mGetParent = ClassLoader.class.getDeclaredMethod("getParent",
          new Class[] {});
      ClassLoader parent = (ClassLoader) mGetParent.invoke(cl, new Object[] {});
      if (parent != null)
        return hasLoadedClass(parent, name);
      
      return false;
    } 
    catch (Exception e) {
      throw new NestableRuntimeException(e);
    }    
  }
}
