/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Assists in validating arguments.</p>
 * 
 * <p>The class is based along the lines of JUnit. If an argument value is 
 * deemed invalid, an IllegalArgumentException is thrown. For example:</p>
 * 
 * <pre>
 * Assert.isTrue( i > 0, "The value must be greater than zero: ", i);
 * Assert.notNull( surname, "The surname must not be null");
 * </pre>
 * 
 * <p>Copied from Jakarta Commons Lang for framework internal use. 
 * Please use the original from <a href="http://jakarta.apache.org/commons/lang/">http://jakarta.apache.org/commons/lang/</a>.</p>
 *
 * @author Ola Berg
 * @author Stephen Colebourne
 * @author Gary Gregory
 * @author Norm Deane
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public abstract class Assert {

  public static String thisToString(Object that) {
    notNull(that, "'this' can never be null, check what you passed to Assert!");
    return " [this :: " + that.getClass().getName() + "]";
  }
  
  public static void isTrue(Object that, boolean expression, String message) {
    if (expression == false) {
      throw new IllegalArgumentException(message + thisToString(that));
    }
  }
  
  public static void isTrue(boolean expression, String message) {
    if (expression == false) {
      throw new IllegalArgumentException(message);
    }
  }
  
  public static void notNull(Object object) {
    if (object == null) {
      throw new IllegalArgumentException("The object under assertion was null!");
    }
  }

  public static void notNull(Object that, Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message + thisToString(that));
    }
  }
  
  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }
  
  public static void notNullParam(Object object, String parameterName) {
    if (object == null) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must not be null!");
    }
  }
  
  public static void notNullParam(Object that, Object object, String parameterName) {
    if (object == null) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must not be null!" + thisToString(that));
    }
  }
  
  public static void isInstanceOf(Object that, Class klass, Object object, String message) {
    if (object == null) return;
    
    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException(message + thisToString(that));
    }
  }
  
  public static void isInstanceOf( Class klass, Object object, String message) {
    if (object == null) return;
    
    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException(message);
    }
  }
  
  public static void isInstanceOfParam(Object that, Class klass, Object object, String parameterName) {
    if (object == null) return;
    
    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must be of type '" + klass.getName() + "' but is of type '" + object.getClass().getName() + "'!" + thisToString(that));
    }
  }
  
  public static void isInstanceOfParam( Class klass, Object object, String parameterName) {
    if (object == null) return;
    
    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must be of type '" + klass.getName() + "' but is of type '" + object.getClass().getName() + "'!");
    }
  }

  public static void notEmptyParam(String string, String parameterName) {
    if (string == null || string.length() == 0) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must not be empty!");
    }
  }
  
  public static void notEmptyParam(Object that, String string, String parameterName) {
    if (string == null || string.length() == 0) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must not be empty!" + thisToString(that));
    }
  }
  

  public static void notEmpty(Object[] array, String message) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Collection collection, String message) {
    if (collection == null || collection.size() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Map map, String message) {
    if (map == null || map.size() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(String string, String message) {
    if (string == null || string.length() == 0) {
      throw new IllegalArgumentException(message);
    }
  }
  
  public static void noNullElementsParam(Collection collection, String param) {
    notNullParam(collection, param);
    int i = 0;
    for (Iterator it = collection.iterator(); it.hasNext();) {
      if (it.next() == null)
        throw new IllegalArgumentException("The validated collection contains null element at index: '" + i + "'!");
      i++;
    }
  }
  
  public static void noNullElements(Collection collection, String message) {
    notNull(collection);
    int i = 0;
    for (Iterator it = collection.iterator(); it.hasNext();) {
      if (it.next() == null)
        throw new IllegalArgumentException(message);
      i++;
    }
  }
  
  public static void noNullElementsParam(Object that, Collection collection, String param) {
    notNullParam(collection, param);
    int i = 0;
    for (Iterator it = collection.iterator(); it.hasNext();) {
      if (it.next() == null)
        throw new IllegalArgumentException("The validated collection contains null element at index: '" + i + "'!" + thisToString(that));
      i++;
    }
  }
  
  public static void noNullElements(Object that, Collection collection, String message) {
    notNull(collection);
    int i = 0;
    for (Iterator it = collection.iterator(); it.hasNext();) {
      if (it.next() == null)
        throw new IllegalArgumentException(message + thisToString(that));
      i++;
    }
  }
}