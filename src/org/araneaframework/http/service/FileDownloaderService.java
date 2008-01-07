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

package org.araneaframework.http.service;

import java.util.Map;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.http.widget.FileDownloaderWidget;
import org.araneaframework.uilib.support.FileInfo;

/**
 * Service that serves content (allows downloading) of specified files. It is a suicidal service that
 * kills itself after having served file content once.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class FileDownloaderService extends FileDownloaderWidget {

  public FileDownloaderService(FileInfo file) {
    super(file);
  }

  public FileDownloaderService(byte[] fileContent, String contentType, String fileName) {
    super(fileContent, contentType, fileName);
  }
  
  /** @since 1.1 */
  public FileDownloaderService(byte[] fileContent, Map headers) {
    super(fileContent, headers);
  }

  protected void close() {
    //Ensure that allow to download only once...
    ManagedServiceContext mngCtx = (ManagedServiceContext) getEnvironment().getEntry(ManagedServiceContext.class);
    mngCtx.close(mngCtx.getCurrentId());
  }
}
