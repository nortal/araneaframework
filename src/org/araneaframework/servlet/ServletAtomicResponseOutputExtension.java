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

package org.araneaframework.servlet;

import java.io.Serializable;

/**
 * An extension to the response making rollbacking possible. If the stream
 * has not been commited, i can be rollbacked.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public interface ServletAtomicResponseOutputExtension extends Serializable {
  /**
   * Commit the output, write everything in the buffer to the stream.
   */
  public void commit() throws Exception;
  
  /**
   * Rollback the body of the response, not the headers. The buffer must not
   * been commited.
   */
  public void rollback() throws Exception;
}
