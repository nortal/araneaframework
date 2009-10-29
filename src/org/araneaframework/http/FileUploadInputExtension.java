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
   * @param fieldName upload field name from which to read file content
   * @return file content and information
   */
  public FileItem getUploadedFile(String fieldName);
  
  /**
   * @return Map of &lt;fileName, fileContent&gt;
   */
  public Map<String, ? extends FileItem> getUploadedFiles();

  
  /**
   * Returns the exception that occurred when trying to parse file upload.
   * @return exception that occurred when trying to parse file upload
   */
  public Exception getUploadException();

  /**
   * Returns whether file upload succeeded, this should only be true when 
   * {@link FileUploadInputExtension#getUploadException()} is <code>null</code>.
   * @return whether upload was successful
   */
  public boolean uploadSucceeded();
}
