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

package org.araneaframework;

/**
 * Implementers can enrich their objects with external Objects 
 * implementing certain interfaces. If a class implements Extendable
 * then it always should implement Narrowable too.
 * 
 * A typical use-case would be:
 * <pre>
 * <code>
 * 
 *  public SampleClass implements Extendable, Narrowable {
 *    ...
 *  }
 *  
 *  
 *  SampleClass obj = new SampleClass();
 *  
 *  obj.extend(SomeInterface.class, someObj);
 *  
 *  ....
 *  
 *  SomeInterface newObj = (SomeInterface)obj.narrow(SomeInterface.class);
 * </code>
 * </pre>
 * @see org.araneaframework.Narrowable
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface Extendable {
  
  /**
   * Adds an extension to the class, an interface with its implementation.
   * <p>
   * Can later be used via <code>narrow(Class)</code>
   * if the class implements {@link Narrowable}.
   *  
   * @param interfaceClass the interface of the extension being added 
   * @param extension a implementation of the interfaceClass
   */
  public <T> void extend(Class<T> interfaceClass, T extension);
}
