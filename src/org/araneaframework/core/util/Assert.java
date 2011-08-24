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

package org.araneaframework.core.util;

import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * Assists in validating arguments.
 * </p>
 * <p>
 * The class is based along the lines of JUnit. If an argument value is deemed invalid, an IllegalArgumentException is
 * thrown. For example:
 * </p>
 * 
 * <pre>
 * Assert.isTrue( i > 0, "The value must be greater than zero: ", i);
 * Assert.notNull( surname, "The surname must not be null");
 * </pre>
 * <p>
 * Copied from Jakarta Commons Lang for framework internal use. Please use the original from <a
 * href="http://jakarta.apache.org/commons/lang/">http://jakarta.apache.org/commons/lang/</a>.
 * </p>
 * 
 * @author Ola Berg
 * @author Stephen Colebourne
 * @author Gary Gregory
 * @author Norm Deane
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public abstract class Assert {

  /**
   * Instantiating this class is prohibited.
   */
  protected Assert() {
    throw new UnsupportedOperationException();
  }

  /**
   * Creates a string with the originating class name that can be used in exception messages.
   * 
   * @param that The object from which class name must be retrieved (required).
   * @return A common string with the class name to be used in an exception message.
   */
  public static String thisToString(Object that) {
    notNull(that, "'this' can never be null, check what you passed to Assert!");
    return String.format(" (in '%s')", that.getClass().getName());
  }

  /**
   * Asserts that the expression evaluates to <code>true</code>.
   * 
   * @param expression The expression to evaluate.
   * @param message A custom message used when the assertion fails.
   */
  public static void isTrue(boolean expression, String message) {
    if (expression == false) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that the expression evaluates to <code>true</code>.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param expression The expression to evaluate.
   * @param message A custom message used when the assertion fails (calling instance info will be appended).
   */
  public static void isTrue(Object that, boolean expression, String message) {
    if (expression == false) {
      throw new IllegalArgumentException(message + thisToString(that));
    }
  }

  /**
   * Asserts that the given object is not <code>null</code>.
   * 
   * @param object The object to check for being not <code>null</code>.
   */
  public static void notNull(Object object) {
    if (object == null) {
      throw new IllegalArgumentException("The object under assertion was null!");
    }
  }

  /**
   * Asserts that the given object is not <code>null</code>.
   * 
   * @param object The object to check for being not <code>null</code>.
   * @param message A custom message used when the assertion fails.
   */
  public static void notNull(Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Asserts that the given object is not <code>null</code>.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param object The object to check for being not <code>null</code>.
   * @param message A custom message used when the assertion fails (calling instance info will be appended).
   */
  public static void notNull(Object that, Object object, String message) {
    if (object == null) {
      throw new IllegalArgumentException(message + thisToString(that));
    }
  }

  /**
   * Asserts that the given object (a parameter to a method, where this assertion is used) is not <code>null</code>.
   * 
   * @param object The object to check for being not <code>null</code>.
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void notNullParam(Object object, String parameterName) {
    if (object == null) {
      throw new IllegalArgumentException("The parameter '" + parameterName + "' must not be null!");
    }
  }

  /**
   * Asserts that the given object (a parameter to a method, where this assertion is used) is not <code>null</code>.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param object The object to check for being not <code>null</code>.
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void notNullParam(Object that, Object object, String parameterName) {
    if (object == null) {
      throw new IllegalArgumentException("The Parameter '" + parameterName + "' must not be null!" + thisToString(that));
    }
  }

  public static void isInstanceOf(Class<?> klass, Object object, String message) {
    if (object == null) {
      return;
    }

    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void isInstanceOf(Object that, Class<?> klass, Object object, String message) {
    if (object == null) {
      return;
    }
  
    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException(message + thisToString(that));
    }
  }

  public static void isInstanceOfParam(Object that, Class<?> klass, Object object, String parameterName) {
    if (object == null) {
      return;
    }

    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must be of type '" + klass.getName()
          + "' but is of type '" + object.getClass().getName() + "'!" + thisToString(that));
    }
  }

  public static void isInstanceOfParam(Class<?> klass, Object object, String parameterName) {
    if (object == null) {
      return;
    }

    if (!klass.isAssignableFrom(object.getClass())) {
      throw new IllegalArgumentException("Parameter '" + parameterName + "' must be of type '" + klass.getName()
          + "' but is of type '" + object.getClass().getName() + "'!");
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

  public static void notEmpty(Collection<?> collection, String message) {
    if (collection == null || collection.size() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(Map<?, ?> map, String message) {
    if (map == null || map.size() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notEmpty(String string, String message) {
    if (string == null || string.length() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void noNullElementsParam(Collection<?> collection, String param) {
    notNullParam(collection, param);
    int i = 0;
    for (Object name : collection) {
      if (name == null) {
        throw new IllegalArgumentException("The validated collection parameter '" + param
            + "' contains null element at index: '" + i + "'!");
      }
      i++;
    }
  }

  public static void noNullElements(Collection<?> collection, String message) {
    notNull(collection);
    for (Object element : collection) {
      if (element == null) {
        throw new IllegalArgumentException(message);
      }
    }
  }

  public static void noNullElementsParam(Object that, Collection<?> collection, String param) {
    notNullParam(collection, param);
    int i = 0;
    for (Object element : collection) {
      if (element == null) {
        throw new IllegalArgumentException("The validated collection parameter '" + param
            + "' contains null element at index: '" + i + "'!" + thisToString(that));
      }
      i++;
    }
  }

  public static void noNullElements(Object that, Collection<?> collection, String message) {
    notNull(collection);
    for (Object element : collection) {
      if (element == null) {
        throw new IllegalArgumentException(message + thisToString(that));
      }
    }
  }
}
