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

package org.araneaframework.http.widget;

import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.util.ServletUtil;

/**
 * Widget that serves content.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class DownloaderWidget extends BaseApplicationWidget {

  protected byte[] data;
  protected String contentType;

  public DownloaderWidget(byte[] data, String contentType) {
    Assert.notNullParam(data, "data");
    Assert.notEmptyParam(contentType, "contentType");
    this.data = data;
    this.contentType = contentType;
  }

  public byte[] getData() {
    return data;
  }

  public String getContentType() {
    return contentType;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);
    response.setContentType(getContentType());
    response.setContentLength(getData().length);
    response.getOutputStream().write(getData());
  }

}
