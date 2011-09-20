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

package org.araneaframework.core.exception;

/**
 * An exception that is thrown when {@link org.araneaframework.OutputData} is being requested but it is not found. The
 * situation, where this exception occurs, is rare because output data should always be available to components
 * processing it. Therefore this exception should not be caught in most cases, and the code causing this exception
 * should be reviewed..
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class NoCurrentOutputDataSetException extends AraneaRuntimeException {

  /**
   * Creates a new instance of this runtime exception with default message.
   */
  public NoCurrentOutputDataSetException() {
    super("No OutputData set in the request.");
  }

  /**
   * Creates a new instance of this runtime exception with custom message.
   * 
   * @param msg A custom exception message.
   */
  public NoCurrentOutputDataSetException(String msg) {
    super(msg);
  }
}
