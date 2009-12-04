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

package org.araneaframework.framework;

import java.io.Serializable;

/**
 * A top level service that is not synchronized and is dependent only on the request.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface TopServiceContext extends ManagedServiceContext, Serializable {

  /**
   * The key of the top-service's ID in the request.
   */
  public static final String TOP_SERVICE_KEY = "araTopServiceId";

  /**
   * The key in the request that specifies whether the top-service should .
   */
  public static final String KEEPALIVE_KEY = "araTopServiceKeepAlive";
}
