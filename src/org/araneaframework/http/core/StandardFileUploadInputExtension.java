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

package org.araneaframework.http.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.araneaframework.core.Assert;
import org.araneaframework.http.FileUploadInputExtension;

/**
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardFileUploadInputExtension implements FileUploadInputExtension {

  private Map fileItems = new HashMap();

  public StandardFileUploadInputExtension(Map fileItems) {
    Assert.notNullParam(fileItems, "fileItems");
    
    this.fileItems = fileItems;
  }

  public FileItem getUploadedFile(String fieldName) {
    return (FileItem) fileItems.get(fieldName);
  }

  public Map getUploadedFiles() {
    return Collections.unmodifiableMap(fileItems);
  }
}
