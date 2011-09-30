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

package org.araneaframework.http.container;

import org.araneaframework.InputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.container.StandardContainerService;
import org.araneaframework.http.HttpInputData;

/**
 * A container service that uses request path info (following the name of the servlet) to resolve the action path.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class PathInfoServiceContainerService extends StandardContainerService {

  @Override
  protected Path getActionPath(InputData input) {
    String path = ((HttpInputData) input).getPath();
    return path != null && path.length() > 1 ? new StandardPath(path.substring(1)) : null;
  }
}
