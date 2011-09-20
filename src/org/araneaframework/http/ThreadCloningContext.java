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

package org.araneaframework.http;

import java.io.Serializable;
import org.araneaframework.Relocatable;
import org.araneaframework.Relocatable.RelocatableService;
import org.araneaframework.Service;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.framework.ThreadContext;

/**
 * Service that clones currently running session thread upon request and sends a response that redirects to cloned
 * session thread. It can be used to support "open link in new window" feature in browsers.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface ThreadCloningContext extends Serializable {

  /**
   * Request parameter key which presence indicates that incoming request is requesting cloning of the current session
   * thread. Presence of this should mean that current thread is cloned and then cloned thread is immediately opened in
   * a new browser window.
   */
  String CLONING_REQUEST_KEY = "araPleaseClone";

  /**
   * Request parameter key which presence indicates that incoming request is requesting cloning of the current session
   * thread. Presence of this should mean that snapshot of current thread is made before request is routed to child
   * {@link Service}. Cloned thread is not attached to thread router (no new window with cloned thread will be opened).
   * Snapshot can be acquired during the same request by calling {@link #acquireThreadSnapshot()}.
   * 
   * @since 1.1
   */
  String CLONE_ONLY_REQUEST_KEY = "araCloneOnly";

  /**
   * Attaches clone as new thread level service (see {@link ThreadContext}). It assumes that the service is already
   * initialized and thus will not attempt to reinitialize it (as using
   * {@link ManagedServiceContext#addService(String, Service)} would try to do).
   * 
   * @param clone cloned service to start as new session level thread
   * @return identifier of created session thread
   * @since 1.1
   */
  String startClonedThread(RelocatableService clone);

  /**
   * Returns the snapshot of current thread, made before child {@link Service} of {@link ThreadCloningContext}
   * implementation has had the chance to process the request. Only applicable when request contained either
   * {@link #CLONE_ONLY_REQUEST_KEY} or {@link #CLONING_REQUEST_KEY} parameter. Deserialization of the snapshot could be
   * done as follows:
   * <p>
   * 
   * <pre><code>
   *   ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(acquireSerializedThread()));
   *   RelocatableService clone = (RelocatableService) ois.readObject();
   * </code></pre>
   * </p>
   * <p>
   * Snapshot may then be started as cloned thread with {@link #startClonedThread(Relocatable.RelocatableService)}.
   * </p>
   * 
   * @return serialized snapshot of current thread or <code>null</code>
   * @since 1.1
   */
  byte[] acquireThreadSnapshot();
}
