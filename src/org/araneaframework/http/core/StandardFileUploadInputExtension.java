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

package org.araneaframework.http.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.araneaframework.core.Assert;
import org.araneaframework.http.FileUploadInputExtension;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class StandardFileUploadInputExtension implements FileUploadInputExtension {  private Exception uploadException;
  private Map<String, ? extends FileItem> fileItems = new HashMap<String, FileItem>();

  public StandardFileUploadInputExtension(Map<String, ? extends FileItem> fileItems, Exception uploadException) {
    Assert.isTrue(fileItems != null || uploadException != null, "FileItems should be present or uploadException non-null.");

    this.fileItems = fileItems;
    this.uploadException = uploadException;
  }

  public FileItem getUploadedFile(String fieldName) {
    return fileItems.get(fieldName);
  }

  public Map<String, FileItem> getUploadedFiles() {
    return Collections.unmodifiableMap(fileItems);
  }
  
  public boolean uploadSucceeded() {
    return uploadException == null;
  }
  
  public Exception getUploadException() {
    return uploadException;
  }
}
