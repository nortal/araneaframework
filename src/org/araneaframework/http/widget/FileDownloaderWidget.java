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

import org.araneaframework.http.service.FileDownloaderService;
import org.araneaframework.uilib.support.FileInfo;

/**
 * Widget that serves content (allows downloading) of specified files.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class FileDownloaderWidget extends FileDownloaderService {

  public FileDownloaderWidget(FileInfo file) {
    super(file);
  }

  public FileDownloaderWidget(byte[] fileContent, String contentType, String fileName) {
    super(fileContent, contentType, fileName);
  }

  protected void close() {
    // Do not try to close, because this is a widget
  }

}
