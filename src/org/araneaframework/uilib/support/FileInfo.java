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

package org.araneaframework.uilib.support;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * This file represents a file info received from a {@link org.araneaframework.uilib.form.control.FileUploadControl}.
 * Besides common file attributes (name, original name, size and type) this class also provides a
 * {@link #readFileContent()} method, which allows to read the file content into memory as a <code>byte[]</code>
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class FileInfo implements Serializable {

  private FileItem item;

  public FileInfo(FileItem item) {
    Assert.notNull(item);
    this.item = item;
  }

  /**
   * Returns the content MIME type.
   * 
   * @return the content MIME type.
   */
  public String getContentType() {
    return this.item.getContentType();
  }

  /**
   * Returns the original filename (user side).
   * 
   * @return the original filename (user side).
   */
  public String getOriginalFilename() {
    return this.item.getName();
  }

  /**
   * Returns only the file name (without path). Usually it is the same result as in {@link #getOriginalFilename()},
   * except in IE, where path is generally also included.
   * 
   * @return the filename without path info.
   * @since 1.2.2
   */
  public String getFileName() {
    return this.item.getName() != null ? FilenameUtils.getName(this.item.getName()) : null;
  }

  /**
   * Returns the file size.
   * 
   * @return the file size.
   */
  public long getSize() {
    return this.item.getSize();
  }

  /**
   * Returns file content. This method actually reads the content of the file, not just returns the saved value.
   * 
   * @return File content.
   */
  public byte[] readFileContent() {
    return this.item.get();
  }

  /**
   * Provides whether the file has at least name and size greater than zero.
   * 
   * @return <code>true</code>, if the file has at least name and size greater than zero.
   * @since 2.0
   */
  public boolean isFilePresent() {
    return !StringUtils.isBlank(getOriginalFilename()) && getSize() > 0;
  }

  /**
   * Provides the stream of the input file. If file is not present, a <code>null</code> will be returned.
   * 
   * @return The stream of the input file, or <code>null</code>.
   * @since 2.0
   */
  public InputStream getFileStream() {
    try {
      return isFilePresent() ? this.item.getInputStream() : null;
    } catch (IOException e) {
      ExceptionUtil.uncheckException("Exception while opening file for streaming.", e);
      return null; // Not reached.
    }
  }

  /**
   * Provides the current file item object.
   * 
   * @return The current file item object
   * @since 2.0
   */
  public FileItem getFileItem() {
    return this.item;
  }
}
