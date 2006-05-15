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

package org.araneaframework.framework;

import java.io.Serializable;
import org.araneaframework.Service;

/**
 * This context allows to abort the current execution flow and pass control to a provided continuation service. The continuation service will
 * be rendered on the current and subsequent request until it explicitly passes control back using {@link org.araneaframework.framework.ContinuationContext#finish()}. This
 * will end the continuation and restore control back.  
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface ContinuationManagerContext extends Serializable {
  /**
   * Returns whether a continuation is already registered during this request and is ready to run. In such a case it is unsafe
   * to override the continuation using {@link #start(Service)}.
   */
  public boolean isRunning();
  
  /**
   * Starts the continuation (by registering it and then throwing and exception, thus passing control to the continuation manager). 
   */
  public void start(Service continuation) throws Exception;  
  
  /**
   * Does same as {@link #start(Service)}, however after the first request passes through the continuation will return control 
   * back to the starting component. 
   */
  public void runOnce(Service continuation) throws Exception;
}
