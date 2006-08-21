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

package org.araneaframework.uilib.form.control;

import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.araneaframework.http.FileUploadInputExtension;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.support.FileInfo;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;

/**
 * This class represents an HTML form file upload control.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 *  
 */
public class FileUploadControl extends BaseControl {

  protected List permittedMimeFileTypes;

  /**
   *  
   */
  public boolean isRead() {
    return innerData != null;
  }

  /**
   * Sets the MIME file types that will be permitted,
   * 
   * @param permittedMimeFileTypes the MIME file types that will be permitted.
   */
  public void setPermittedMimeFileTypes(List permittedMimeFileTypes) {
    this.permittedMimeFileTypes = permittedMimeFileTypes;
  }
  
  /**
   * Returns "FileInfo".
   * 
   * @return "FileInfo".
   */
  public String getRawValueType() {
    return "FileInfo";
  }
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  /**
   * Empty.
   */
  protected void prepareResponse() {
  //There is no response for file upload control.
  }

  /**
   * Reads the {@link FileInfo}data from request using the {@link MultipartWrapper}.
   */
  protected void readFromRequest(HttpInputData request) {
    FileUploadInputExtension fileUpload = 
      (FileUploadInputExtension) request.narrow(FileUploadInputExtension.class);    
    
  	if (fileUpload.getUploadedFile(request.getScope().toString())!= null) {
      FileItem file = fileUpload.getUploadedFile(request.getScope().toString());
      String mimeType = file.getContentType();

      if (permittedMimeFileTypes == null || permittedMimeFileTypes.contains(mimeType)) {

        innerData = new FileInfo(file);
      }
      else {
        addError(
            ErrorUtil.localizeAndFormat(
            UiLibMessages.FORBIDDEN_MIME_TYPE, 
            ErrorUtil.localize(getLabel(), getEnvironment()),
            getEnvironment()));        
      }
    }
  }

  /**
   *  
   */
  public void convert() {
    value = innerData;

    if (isMandatory() && !isRead()) {
      addError(
          ErrorUtil.localizeAndFormat(
          UiLibMessages.MANDATORY_FIELD, 
          ErrorUtil.localize(getLabel(), getEnvironment()),
          getEnvironment()));        
    }
  }
  
  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  } 

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************

  /**
   * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
   *  
   */
  public class ViewModel extends BaseControl.ViewModel {

    private List permittedMimeFileTypes;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.permittedMimeFileTypes = FileUploadControl.this.permittedMimeFileTypes;
    }

    /**
     * Returns the MIME file types that will be permitted,
     * 
     * @return the MIME file types that will be permitted.
     */
    public List getPermittedMimeFileTypes() {
      return permittedMimeFileTypes;
    }
  }

}
