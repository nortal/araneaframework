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
 * Service that clones currently running session thread upon request and sends a response
 * that redirects to cloned session thread. It can be used to support "open link in new
 * window" feature in browsers.
 * 
 * @author Taimo Peelo
 */
public interface ThreadCloningContext extends Serializable {
  /** key indicating that incoming request is requesting cloning of the current session thread */
  public static final String CLONING_REQUEST_KEY = "pleaseClone";
}
