/*
 * Copyright 2006-2008 Webmedia Group Ltd.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;

/**
 * A filter that logs the time it takes for the child service to complete its
 * action method (serving the request). The logging statement can have a message
 * set via <code>setMessage()</code>.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardStatisticFilterService extends BaseFilterService {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(StandardStatisticFilterService.class);

  private String message;

  public void setMessage(String message) {
    this.message = message;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    long start = System.currentTimeMillis();
    childService._getService().action(path, input, output);
    log.info(message + (System.currentTimeMillis() - start) + " ms.");
  }

  public String getMessage() {
    return message;
  }
}
