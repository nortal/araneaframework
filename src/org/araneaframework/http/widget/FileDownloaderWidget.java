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
import org.apache.commons.io.FilenameUtils;
import org.araneaframework.core.Assert;
import org.araneaframework.uilib.support.FileInfo;

/**
 * Widget that serves content (allows downloading) of specified files.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class FileDownloaderWidget extends DownloaderWidget {

  protected String fileName;

  protected boolean contentDispositionInline;

  /**
   * Instantiates widget with download file information.
   * 
   * @param file The file to download. The file information is stored in the given {@link FileInfo} object. Must not be
   *          <code>null</code> or a NullPointerException will occur.
   */
  public FileDownloaderWidget(FileInfo file) {
    this(file.getFileStream(), file.getContentType(), file.getFileName());
  }

  /**
   * Instantiates widget with download file information.
   * 
   * @param fileContent The content for the file. Must not be <code>null</code> or a NullPointerException will occur.
   * @param contentType The content type for the file.
   * @param fileName The name for the file (must not be <code>null</code>).
   */
  public FileDownloaderWidget(byte[] fileContent, String contentType, String fileName) {
    this(new ByteArrayInputStream(fileContent), fileContent.length, contentType, fileName);
  }

  /**
   * Instantiates widget with download file information.
   * 
   * @param fileContent The content for the file (must not be <code>null</code>).
   * @param contentType The content type for the file.
   * @param fileName The name for the file (must not be <code>null</code>).
   */
  public FileDownloaderWidget(InputStream fileContent, String contentType, String fileName) {
    this(fileContent, -1, contentType, fileName);
  }

  /**
   * Instantiates widget with download file information.
   * 
   * @param fileContent The content for the file (must not be <code>null</code>).
   * @param length The (data stream) length information to send with the downloaded data. Specify -1 for no length.
   * @param contentType The content type for the file.
   * @param fileName The name for the file (must not be <code>null</code>).
   */
  public FileDownloaderWidget(InputStream fileContent, int length, String contentType, String fileName) {
    super(fileContent, length, contentType);
    Assert.notEmptyParam(fileName, "fileName");
    setFileName(fileName);
  }

  /**
   * Instantiates widget with download file information. Providing a callback is the best way for sending large files to
   * the client!
   * 
   * @param callback The callback that will be called to fetch file download data.
   * @since 2.0
   */
  public FileDownloaderWidget(FileDownloadStreamCallback callback) {
    super(callback);
  }

  /**
   * Instantiates widget with download file information.
   * 
   * @param fileContent The content for the file (must not be <code>null</code>).
   * @param headers Headers to be sent with the downloaded file.
   * @since 1.1
   */
  public FileDownloaderWidget(byte[] fileContent, Map<String, String> headers) {
    super(fileContent, headers);
  }

  /**
   * Sets a new name for the download file.
   * @param fileName
   */
  public void setFileName(String fileName) {
    this.fileName = normalizeFileName(fileName);
  }

  protected String getFileName() {
    FileDownloadStreamCallback c = getCallback();
    return c != null && c.getFileName() != null ? c.getFileName() : this.fileName;
  }

  /**
   * Returns value of currently used content-disposition response header.
   * 
   * @return false if content-disposition header is set to "attachment"
   */
  protected boolean isContentDispositionInline() {
    FileDownloadStreamCallback c = getCallback();
    return c != null && c.isContentDispositionInline() != null ? c.isContentDispositionInline() : this.contentDispositionInline;
  }

  /**
   * Sets content-disposition header to "inline" (true), "attachment" (false), or avoids it (null).
   * 
   * @param contentDispositionInline A Boolean indicating whether <i>Content-Disposition</i> should be rendered in-line
   *          (<code>true</code>), as attachment (<code>false</code>), or should be avoided (<code>null</code>).
   */
  public void setContentDispositionInline(Boolean contentDispositionInline) {
    this.contentDispositionInline = contentDispositionInline;
  }

  /** Used internally to extract only file name from supplied filename (no file path). */
  protected String normalizeFileName(String fileName) {
    // as IE (6) provides full names for uploaded files, we use some heuristics
    // to ensure filename does not include the full path. By no means bulletproof
    // as it does some harm to filenames containing slashes/backslashes.
    return FilenameUtils.getName(fileName);
  }

  @Override
  protected void afterFile(HttpServletResponse response, long length) {
    StringBuffer disposition = new StringBuffer();
    disposition.append(isContentDispositionInline() ? "inline" : "attachment");
    disposition.append("; filename=").append(getFileName()).append("; size=").append(length).append(";");
    response.addHeader("Content-Disposition", disposition.toString());
    super.afterFile(response, length);
  }

  protected FileDownloadStreamCallback getCallback() {
    return (FileDownloadStreamCallback) this.dataStreamCallback;
  }

  /**
   * If a file download stream is given to e.g. popup context, the stream must be serializable. Since streams are not
   * serializable, this callback request the stream only when needed to output it. Therefore, it escapes the
   * serialization step.
   * <p>
   * This class extends {@link DownloadStreamCallback} to get more data about the file to download.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  public static abstract class FileDownloadStreamCallback extends DownloadStreamCallback {

    /**
     * Implemented method must return a non-null name for the download file.
     * 
     * @return The name for the download file.
     */
    public abstract String getFileName();

    /**
     * Override this to avoid the <i>Content-Disposition</i> header (return <code>null</code>) or to make the file
     * download content to be rendered in-line (i.e. inside to browser; return <code>true</code>).
     * 
     * @return A Boolean indicating whether <i>Content-Disposition</i> should be rendered in-line (<code>true</code>),
     *         as attachment (<code>false</code>), or should be avoided (<code>null</code>).
     * @see <a href="http://www.ietf.org/rfc/rfc1806.txt" target="_blank">http://www.ietf.org/rfc/rfc1806.txt</a>
     */
    public Boolean isContentDispositionInline() {
      return false;
    }
  }
}
