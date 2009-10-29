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

package org.araneaframework.http.widget;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
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

  protected InputStream dataStream;

  protected int length = -1;

  protected String contentType;

  protected Map<String, String> headers;

  public DownloaderWidget(byte[] data, String contentType) {
    this(new ByteArrayInputStream(data), data.length, contentType);
  }
  
  /** @since 1.1 */
  public DownloaderWidget(byte[] data, Map<String, String> headers) {
    this(new ByteArrayInputStream(data), data.length, null, headers);
  }

  /** @since 2.0 */
  public DownloaderWidget(InputStream dataStream, String contentType) {
    this(dataStream, -1, contentType);
  }

  /** @since 2.0 */
  public DownloaderWidget(InputStream dataStream, long length, String contentType) {
    this(dataStream, length, contentType, null);
  }

  /** @since 2.0 */
  public DownloaderWidget(InputStream dataStream, long length, String contentType, Map<String, String> headers) {
    Assert.notNullParam(dataStream, "dataStream");
    this.dataStream = dataStream;
    this.length = (int) length;
    this.contentType = contentType;
    this.headers = headers;
  }

  public InputStream getData() {
    return this.dataStream;
  }

  public String getContentType() {
    return this.contentType;
  }
  
  /** @since 1.1 */
  public Map<String, String> getHeaders() {
    return this.headers;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);
    beforeFile(response);

    long length = IOUtils.copyLarge(this.dataStream, response.getOutputStream());
    IOUtils.closeQuietly(this.dataStream);

    afterFile(response, length);
  }

  protected void beforeFile(HttpServletResponse response) {
    response.setContentType(getContentType());
    if (this.headers != null) {
      for (Map.Entry<String, String> header : this.headers.entrySet()) {
        response.setHeader(header.getKey(), header.getValue());
      }
    }
    if (this.length >= 0) {
      response.setContentLength(this.length);
    }
  }

  protected void afterFile(HttpServletResponse response, long length) {
    if (this.length < 0 && length >= 0 && length < Integer.MAX_VALUE) {
      response.setContentLength((int) length);
    }
  }
}
