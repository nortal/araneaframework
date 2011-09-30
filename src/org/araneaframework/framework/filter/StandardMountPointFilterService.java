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

/**
 * Filter service that enforces <tt>MountContext</tt> mapping. If the current request is sent with mounting criteria, it
 * will send the mounting message produced by the mounting {@link MountContext.MessageFactory}.
 * <p>
 * This service doesn't require any configuration.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see MountContext
 * @see org.araneaframework.http.filter.StandardMountingFilterService
 */
public class StandardMountPointFilterService extends BaseFilterService {

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    MountContext mountCtx = getEnvironment().requireEntry(MountContext.class);
    Message mountMsg = mountCtx.getMountedMessage(input);

    if (mountMsg != null) {
      mountMsg.send(null, getChildService());
    }

    super.action(path, input, output);
  }
}
