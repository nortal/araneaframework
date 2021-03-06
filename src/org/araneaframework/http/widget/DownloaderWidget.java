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
import java.io.Serializable;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.util.ServletUtil;

/**
 * Widget that serves downloadable content.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class DownloaderWidget extends BaseApplicationWidget {

  protected InputStream dataStream;

  protected int length = -1;

  protected String contentType;

  protected Map<String, String> headers;

  protected DownloadStreamCallback dataStreamCallback;

  /**
   * Instantiates widget with download data.
   * 
   * @param data The data to be downloaded as bytes.
   * @param contentType The content type for the bytes.
   */
  public DownloaderWidget(byte[] data, String contentType) {
    this(new ByteArrayInputStream(data), data.length, contentType);
  }
  
  /**
   * Instantiates widget with download data.
   * 
   * @param data The data to be downloaded as bytes.
   * @param headers Headers to be sent with the downloaded data.
   * @since 1.1
   */
  public DownloaderWidget(byte[] data, Map<String, String> headers) {
    this(new ByteArrayInputStream(data), data.length, null, headers);
  }

  /**
   * Instantiates widget with download data.
   * 
   * @param dataStream The stream pointing to the data to be downloaded.
   * @param contentType The content type for the data stream.
   * @since 2.0
   */
  public DownloaderWidget(InputStream dataStream, String contentType) {
    this(dataStream, -1, contentType);
  }

  /**
   * Instantiates widget with download data.
   * 
   * @param dataStream The stream pointing to the data to be downloaded.
   * @param length The (data stream) length information to send with the downloaded data. Specify -1 for no length.
   * @param contentType The content type for the data stream.
   * @since 2.0
   */
  public DownloaderWidget(InputStream dataStream, long length, String contentType) {
    this(dataStream, length, contentType, null);
  }

  /**
   * Instantiates widget with download data.
   * 
   * @param dataStream The stream pointing to the data to be downloaded.
   * @param length The (data stream) length information to send with the downloaded data. Specify -1 for no length.
   * @param contentType The content type for the data stream.
   * @param headers Headers to be sent with the downloaded data.
   * @since 2.0
   */
  public DownloaderWidget(InputStream dataStream, long length, String contentType, Map<String, String> headers) {
    Assert.notNullParam(dataStream, "dataStream");
    this.dataStream = dataStream;
    this.length = (int) length;
    this.contentType = contentType;
    this.headers = headers;
  }

  /**
   * Instantiates widget with download data. Providing a callback is the best way for sending large files to the client!
   * 
   * @param callback The callback that will be called to fetch download data.
   * @since 2.0
   */
  public DownloaderWidget(DownloadStreamCallback callback) {
    Assert.notNullParam(callback, "callback");
    this.dataStreamCallback = callback;
  }

  protected InputStream getDataStream() {
    DownloadStreamCallback c = this.dataStreamCallback;
    InputStream result = c != null ? c.getStreamToDownload() : null;
    return result != null ? result : this.dataStream;
  }

  protected String getContentType() {
    DownloadStreamCallback c = this.dataStreamCallback;
    String result = c != null ? c.getContentType() : null;
    return StringUtils.defaultString(result, this.contentType);
  }

  protected int getLength() {
    DownloadStreamCallback c = this.dataStreamCallback;
    int result = c != null ? c.getLength() : -1;
    return result >= 0 ? result : this.length;
  }

  protected Map<String, String> getHeaders() {
    DownloadStreamCallback c = this.dataStreamCallback;
    return c != null && c.getHeaders() != null ? c.getHeaders() : this.headers;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);
    beforeFile(response);

    InputStream dataStream = getDataStream();
    long length = IOUtils.copyLarge(dataStream, response.getOutputStream());
    IOUtils.closeQuietly(dataStream);

    afterFile(response, length);
  }

  protected void beforeFile(HttpServletResponse response) {
    response.setContentType(getContentType());
    if (getHeaders() != null) {
      for (Map.Entry<String, String> header : getHeaders().entrySet()) {
        response.setHeader(header.getKey(), header.getValue());
      }
    }
    if (getLength() >= 0) {
      response.setContentLength(getLength());
    }
  }

  protected void afterFile(HttpServletResponse response, long length) {
    if (getLength() < 0 && length >= 0 && length < Integer.MAX_VALUE) {
      response.setContentLength((int) length);
    }
    this.dataStream = null; // ByteArrayOutputStream is not serializable!
    this.dataStreamCallback = null; // Should not be used anymore!
  }

  /**
   * If a file download stream is given to e.g. popup context, the stream must be serializable. Since streams are not
   * serializable, this callback request the stream only when needed to output it. Therefore, it escapes the
   * serialization step.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  public abstract static class DownloadStreamCallback implements Serializable {

    /**
     * Override this to specify a stream to data to send as the download content.
     * 
     * @return The stream for the response content.
     */
    public abstract InputStream getStreamToDownload();

    /**
     * Override this when the callback can give the content type of the stream.
     * 
     * @return The content type of the stream.
     */
    public String getContentType() {
      return null;
    }

    /**
     * Override this when the callback can give the length of the stream.
     * 
     * @return The length of the stream.
     */
    public int getLength() {
      return -1;
    }

    /**
     * Override this when the callback can give the HTTP header with the stream.
     * 
     * @return The HTTP headers to send with the stream.
     */
    public Map<String, String> getHeaders() {
      return null;
    }
  }
}
