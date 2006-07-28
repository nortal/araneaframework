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

package org.araneaframework.framework.util;

import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.framework.router.StandardTopServiceRouterService;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class StandardServiceInfo implements ServiceInfo {
    private String topServiceId;
    private String threadServiceId;
    private String requestUrl;
    
    public StandardServiceInfo(String topServiceId, String threadId, String requestUrl) {
      this.topServiceId = topServiceId;
      this.threadServiceId = threadId;
      this.requestUrl = requestUrl;
    }
    
    public String getTopServiceId() {
    	return topServiceId;
    }
    
    public String getThreadServiceId() {
    	return threadServiceId;
    }

    public String toURL() {
      StringBuffer url = new StringBuffer(requestUrl != null ? requestUrl : "");
      url.append('?').append((StandardTopServiceRouterService.TOP_SERVICE_KEY + "=")).append(topServiceId);
      if (threadServiceId != null) {
        url.append("&" + StandardThreadServiceRouterService.THREAD_SERVICE_KEY + "=");
        url.append(threadServiceId);
      }
      return url.toString();
    }
}
