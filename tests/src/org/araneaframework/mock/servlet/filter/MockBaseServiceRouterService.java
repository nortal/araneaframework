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

package org.araneaframework.mock.servlet.filter;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.framework.router.BaseServiceRouterService;
import org.araneaframework.mock.MockUtil;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class MockBaseServiceRouterService extends BaseServiceRouterService {

  @Override
  protected String getServiceId(InputData input) throws Exception {
    return input.getGlobalData().get("serviceId");  
  }
  
  @Override
  protected String getServiceKey() throws Exception {
    return "serviceId";
  }

  protected Environment getChildEnvironment(@SuppressWarnings("unused") Object serviceId) throws Exception {
    return MockUtil.getEnv();
  }
}
