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

package org.araneaframework.tests.util;

import org.araneaframework.framework.ThreadContext;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class RequestUtil {
  /** 
   * Request is considered SUBMITTED only when some HTML form inputs
   * are present. Aranea Form Elements are not updated (read from request) unless 
   * request is marked submitted.*/
  public static MockHttpServletRequest markSubmitted(MockHttpServletRequest req) {
    req.addParameter(ThreadContext.THREAD_SERVICE_KEY, "");
    return req;
  }
}
