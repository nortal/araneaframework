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
import java.io.OutputStream;
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

  protected OutputStream fileStream;

  protected String fileName;

  protected boolean contentDispositionInline = true;

  public FileDownloaderWidget(FileInfo file) {
    this(file.getFileStream(), file.getContentType(), file.getFileName());
  }

  public FileDownloaderWidget(byte[] fileContent, String contentType, String fileName) {
    this(new ByteArrayInputStream(fileContent), fileContent.length, contentType, fileName);
  }

  public FileDownloaderWidget(InputStream fileContent, String contentType, String fileName) {
    this(fileContent, -1, contentType, fileName);
  }

  public FileDownloaderWidget(InputStream fileContent, int length, String contentType, String fileName) {
    super(fileContent, length, contentType);
    Assert.notEmptyParam(fileName, "fileName");
    setFileName(fileName);
  }

  /** @since 1.1 */
  public FileDownloaderWidget(byte[] fileContent, Map<String, String> headers) {
    super(fileContent, headers);
  }

  public void setFileName(String fileName) {
    this.fileName = normalizeFileName(fileName);
  }

  /**
   * Returns value of currently used content-disposition response header.
   * 
   * @return false if content-disposition header is set to "attachment"
   */
  public boolean isContentDispositionInline() {
    return this.contentDispositionInline;
  }

  /**
   * Sets content-disposition header to "inline" (true) or "attachment" (false).
   */
  public void setContentDispositionInline(boolean contentDispositionInline) {
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
    disposition.append(this.contentDispositionInline ? "inline" : "attachment");
    disposition.append("; filename=").append(this.fileName).append("; size=").append(this.length).append(";");
    response.addHeader("Content-Disposition", disposition.toString());
    super.afterFile(response, length);
  }
}
