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

package org.araneaframework.uilib.support;

import java.io.Serializable;
import org.apache.commons.fileupload.FileItem;


/**
 * This file represents a file info received from a {@link org.araneaframework.uilib.form.control.FileUploadControl}. Besides
 * common file attributes (name, original name, size and type) this class also provides a {@link #readFileContent()} method, 
 * which allows to read the file content as a <code>byte[]</code>
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class FileInfo implements Serializable {
  private transient FileItem item;
  
  public FileInfo(FileItem item) {
    this.item = item;
  }

  /**
   * Returns the content MIME type.
   * @return the content MIME type.
   */
  public String getContentType() {
    return item.getContentType();
  }

  /**
   * Returns the original filename (user side).
   * @return the original filename (user side).
   */
  public String getOriginalFilename() {
    return item.getName();
  }
  
  /**
   * Returns the file size.
   * @return the file size.
   */
  public long getSize() {
    return item.getSize();
  }
  
  /**
   * Returns file content. This method actually reads the content
   * of the file, not just returns the saved value.
   * 
   * @return File content.
   */
  public byte[] readFileContent() {
    return item.get();
  }

}
