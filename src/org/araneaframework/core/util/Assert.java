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
import org.apache.commons.lang.StringUtils;

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
    if (!expression) {
      fail(message);
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
    if (!expression) {
      fail(message, that);
    }
  }

  /**
   * Asserts that the given object is not <code>null</code>.
   * 
   * @param object The object to check for being not <code>null</code>.
   */
  public static void notNull(Object object) {
    if (object == null) {
      fail("The object under assertion was null!");
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
      fail(message);
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
      fail(message, that);
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
      fail("The parameter '" + parameterName + "' must not be null!");
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
      fail("The Parameter '" + parameterName + "' must not be null!", that);
    }
  }

  /**
   * Asserts that given object is an instance of specified class.
   * 
   * @param klass The expected class that object must be assignable to (required).
   * @param object The object to check (may be <code>null</code>).
   * @param message A custom message used when the assertion fails.
   */
  public static void isInstanceOf(Class<?> klass, Object object, String message) {
    if (object != null && !klass.isAssignableFrom(object.getClass())) {
      fail(message);
    }
  }

  /**
   * Asserts that given object is an instance of specified class.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param klass The expected class that object must be assignable to (required).
   * @param object The object to check (may be <code>null</code>).
   * @param message A custom message used when the assertion fails (calling instance info will be appended).
   */
  public static void isInstanceOf(Object that, Class<?> klass, Object object, String message) {
    if (object != null && !klass.isAssignableFrom(object.getClass())) {
      fail(message, that);
    }
  }

  /**
   * Asserts that given object (a parameter to a method) is an instance of specified class.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param klass The expected class that object must be assignable to (required).
   * @param object The object to check (may be <code>null</code>).
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void isInstanceOfParam(Object that, Class<?> klass, Object object, String parameterName) {
    if (object != null && !klass.isAssignableFrom(object.getClass())) {
      fail("Parameter '" + parameterName + "' must be of type '" + klass.getName() + "' but is of type '"
          + object.getClass().getName() + "'!", that);
    }
  }

  /**
   * Asserts that given object (a parameter to a method) is an instance of specified class.
   * 
   * @param klass The expected class that object must be assignable to (required).
   * @param object The object to check (may be <code>null</code>).
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void isInstanceOfParam(Class<?> klass, Object object, String parameterName) {
    if (object != null && !klass.isAssignableFrom(object.getClass())) {
      fail("Parameter '" + parameterName + "' must be of type '" + klass.getName() + "' but is of type '"
          + object.getClass().getName() + "'!");
    }
  }

  /**
   * Asserts that given String parameter to a method is not <code>null</code> and not empty string with zero length.
   * 
   * @param string The value to check.
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void notEmptyParam(String string, String parameterName) {
    if (string == null || string.length() == 0) {
      fail("Parameter '" + parameterName + "' must not be empty!");
    }
  }

  /**
   * Asserts that given String parameter to a method is not <code>null</code> and not an empty string with zero length.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param string The value to check.
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void notEmptyParam(Object that, String string, String parameterName) {
    if (string == null || string.length() == 0) {
      fail("Parameter '" + parameterName + "' must not be empty!", that);
    }
  }

  /**
   * Asserts that given array is not <code>null</code> and not an empty array with zero length.
   * 
   * @param array The value to check.
   * @param message A custom message used when the assertion fails.
   */
  public static void notEmpty(Object[] array, String message) {
    if (array == null || array.length == 0) {
      fail(message);
    }
  }

  /**
   * Asserts that given collection is not <code>null</code> and not an empty collection with zero length.
   * 
   * @param collection The value to check.
   * @param message A custom message used when the assertion fails.
   */
  public static void notEmpty(Collection<?> collection, String message) {
    if (collection == null || collection.size() == 0) {
      fail(message);
    }
  }

  /**
   * Asserts that given map is not <code>null</code> and not an empty map with zero length.
   * 
   * @param map The value to check.
   * @param message A custom message used when the assertion fails.
   */
  public static void notEmpty(Map<?, ?> map, String message) {
    if (map == null || map.size() == 0) {
      fail(message);
    }
  }

  /**
   * Asserts that given string is not <code>null</code> and not an empty string with zero length.
   * 
   * @param string The value to check.
   * @param message A custom message used when the assertion fails.
   */
  public static void notEmpty(String string, String message) {
    if (string == null || string.length() == 0) {
      fail(message);
    }
  }

  /**
   * Asserts that given collection (a parameter to a method) is not <code>null</code> and does not contain a
   * <code>null</code> value.
   * 
   * @param collection The value to check.
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void noNullElementsParam(Collection<?> collection, String parameterName) {
    notNullParam(collection, parameterName);
    int i = 0;
    for (Object name : collection) {
      if (name == null) {
        fail("The validated collection parameter '" + parameterName + "' contains null element at index: '" + i + "'!");
      }
      i++;
    }
  }

  /**
   * Asserts that given collection is not <code>null</code> and does not contain a <code>null</code> value.
   * 
   * @param collection The value to check.
   * @param message A custom message used when the assertion fails.
   */
  public static void noNullElements(Collection<?> collection, String message) {
    notNull(collection);
    for (Object element : collection) {
      if (element == null) {
        fail(message);
      }
    }
  }

  /**
   * Asserts that given collection is not <code>null</code> and does not contain a <code>null</code> value.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param collection The value to check.
   * @param parameterName The name of the object parameter (used in exception message).
   */
  public static void noNullElementsParam(Object that, Collection<?> collection, String parameterName) {
    notNullParam(collection, parameterName);
    int i = 0;
    for (Object element : collection) {
      if (element == null) {
        fail("The validated collection parameter '" + parameterName + "' contains null element at index: '" + i + "'!",
            that);
      }
      i++;
    }
  }

  /**
   * Asserts that given collection is not <code>null</code> and does not contain a <code>null</code> value.
   * 
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   * @param collection The value to check.
   * @param message A custom message used when the assertion fails (calling instance info will be appended).
   */
  public static void noNullElements(Object that, Collection<?> collection, String message) {
    notNull(collection);
    for (Object element : collection) {
      if (element == null) {
        fail(message, thisToString(that));
      }
    }
  }

  /**
   * Fails an assertion, i.e. throws an exception with given optional message.
   * 
   * @param message Optional message indicating why the condition failed.
   */
  public static void fail(String message) {
    throw new IllegalArgumentException(message);
  }

  /**
   * Fails an assertion, i.e. throws an exception with given optional message.
   * 
   * @param message Optional message indicating why the condition failed.
   * @param that The instance where this assertion is called from (used in exception message, thus required).
   */
  public static void fail(String message, Object that) {
    throw new IllegalArgumentException(StringUtils.defaultString(message) + thisToString(that));
  }
}
