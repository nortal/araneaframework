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

package org.araneaframework.http.filter;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpInputData;

/**
 * A filter which sets the character encoding of the request.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardRequestEncodingFilterService extends BaseFilterService {

  private String requestEncoding = "UTF-8";

  /**
   * Sets the request encoding to be applied to incoming HTTP requests. Default encoding is UTF-8.
   * 
   * @param encoding Expected request encoding.
   */
  public void setRequestEncoding(String encoding) {
    Assert.notEmptyParam(encoding, "encoding");
    this.requestEncoding = encoding;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    ((HttpInputData) input).setCharacterEncoding(this.requestEncoding);
    this.childService._getService().action(path, input, output);
  }
}
