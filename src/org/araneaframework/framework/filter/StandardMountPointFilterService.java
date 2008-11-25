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

package org.araneaframework.framework.filter;

import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.MountContext;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.filter.StandardMountingFilterService;

/**
 * This service serves as the entry point for mounted URLs. If the current
 * request is sent to one of such URLs it will send the mounting message
 * produced by the mounting
 * {@link org.araneaframework.framework.MountContext.MessageFactory}.
 * <p>
 * This service doesn't require any configuration.
 * 
 * @see MountContext
 * @see StandardMountingFilterService
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardMountPointFilterService extends BaseFilterService {
  private static final long serialVersionUID = 1L;

  protected void action(Path path, InputData input, OutputData output)
      throws Exception {
    MountContext mountCtx = getEnvironment().requireEntry(MountContext.class);
    Message mountMsg = mountCtx.getMountedMessage(input);
    if (mountMsg != null) {
      mountMsg.send(null, childService);
    }
    super.action(path, input, output);
  }
}
