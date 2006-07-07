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

package org.araneaframework.core.util;

import org.apache.commons.lang.exception.NestableRuntimeException;


/**
 * This utility class contains methods for managing exceptions.
 * 
 * @author Jevgeni kabanov (ekabanov@webmedia.ee)
 */
public class ExceptionUtil {
  public static RuntimeException uncheckException(Exception e) {
    if (e instanceof RuntimeException)
      throw (RuntimeException) e;
    
    throw new NestableRuntimeException(e);
  }
}
