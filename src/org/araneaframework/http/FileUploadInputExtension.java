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

package org.araneaframework.http;

import java.io.Serializable;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;

/**
 * Extension to {@link org.araneaframework.OutputData} that allows access the uploaded files.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface FileUploadInputExtension extends Serializable {

  /**
   * Provides an uploaded file by field name.
   * 
   * @param fieldName A field name that contained a file.
   * @return File content and information, or <code>null</code> when no file is associated with given field name.
   */
  FileItem getUploadedFile(String fieldName);

  /**
   * Provides a map of uploaded files by file upload field names.
   * 
   * @return An unmodifiable map of files by field names.
   */
  Map<String, ? extends FileItem> getUploadedFiles();

  /**
   * Provides the exception that occurred when trying to parse file upload.
   * 
   * @return Exception that occurred when trying to parse file upload, or <code>null</code> when no exception occurred.
   */
  Exception getUploadException();

  /**
   * Returns whether file upload succeeded, this should only be true when {@link #getUploadException()} is
   * <code>null</code>.
   * 
   * @return A Boolean that is <code>true</code> when file(s) upload was successful.
   */
  boolean uploadSucceeded();
}
