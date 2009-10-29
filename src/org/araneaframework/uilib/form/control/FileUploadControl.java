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

package org.araneaframework.uilib.form.control;

import org.apache.commons.lang.StringUtils;

import org.araneaframework.uilib.support.DataType;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ActionListener;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.NoSuchNarrowableException;
import org.araneaframework.framework.FileUploadContext;
import org.araneaframework.http.FileUploadInputExtension;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.support.FileInfo;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * This class represents an HTML form file upload control.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class FileUploadControl extends BaseControl<FileInfo> {

  private static final Log LOG = LogFactory.getLog(FileUploadControl.class);

  public static final String LISTENER_NAME = "fileUpload";

  protected List<String> permittedMimeFileTypes;

  protected boolean uploadSucceeded = true;

  protected boolean mimeTypePermitted = true;

  protected boolean ajaxRequest;

  protected List<String> ajaxMessages = new LinkedList<String>();

  protected void init() throws Exception {
    super.init();
    addActionListener(LISTENER_NAME, new FileUploadActionListener());
  }

  public boolean isRead() {
    return this.innerData != null;
  }

  /**
   * Sets the MIME file types that will be permitted, or <code>null</code> to indicate that no MIME checks will be done.
   * 
   * @param permittedMimeFileTypes the MIME file types that will be permitted, or <code>null</code>.
   */
  public void setPermittedMimeFileTypes(List<String> permittedMimeFileTypes) {
    this.permittedMimeFileTypes = permittedMimeFileTypes;
  }

  public void setRawValue(FileInfo value) {
    super.setRawValue(value);
    this.innerData = value;
  }

  public DataType getRawValueType() {
    return new DataType(FileInfo.class);
  }

  protected void addError(String error) {
    if (this.ajaxRequest) {
      this.ajaxMessages.add(error);
    } else {
      super.addError(error);
    }
  }

  /**
   * Empty. There is no response for file upload control.
   */
  protected void prepareResponse() {}

  /**
   * Reads the {@link FileInfo} data from request {@link HttpInputData}.
   */
  @Override
  protected void readFromRequest(HttpInputData request) {
    FileUploadInputExtension fileUpload = getFileUploadInputExtension(request);
    // this is acceptable, see comments in getFileUploadInputExtension()
    if (fileUpload == null) {
      return;
    }

    // FIXME: unfortunately this conditional code is unreachable because when request
    // parsing fails then transaction ID is usually not set (inconsistent) and update() is never called
    this.uploadSucceeded = fileUpload.uploadSucceeded();
    if (!this.uploadSucceeded) {
      return;
    }

    if (fileUpload.getUploadedFile(getScope().toString()) != null) {
      FileItem file = fileUpload.getUploadedFile(getScope().toString());
      String mimeType = file.getContentType();

      this.mimeTypePermitted = this.permittedMimeFileTypes == null || this.permittedMimeFileTypes.contains(mimeType);
      if (this.mimeTypePermitted) {
        this.innerData = new FileInfo(file);
      }
    }
  }

  private FileUploadInputExtension getFileUploadInputExtension(HttpInputData request) {
    if (getEnvironment().getEntry(FileUploadContext.class) == null) {
      throw new AraneaRuntimeException(
          "It seems that attempt was made to use FileUploadControl, but upload filter is not present.");
    }

    FileUploadInputExtension fileUpload = null;

    // Motivation for try: when one opens FileUploadDemo in new window (cloning!), exception occurs b/c
    // FileUploadInputExtension extension does not exist in InputData which is
    // extended only when request is "multipart", while cloning filter always sends ordinary GET.
    try {
      fileUpload = request.narrow(FileUploadInputExtension.class);
    } catch (NoSuchNarrowableException e) {
      // If no file-upload extension is present and file-upload filter is enabled, control should
      // just sit there and be beautiful, otherwise just return null.
    }
    return fileUpload;
  }

  public void convert() {
    this.value = (FileInfo) this.innerData;

    if (!this.uploadSucceeded) {
      Long sizeLimit = (getEnvironment().getEntry(FileUploadContext.class)).getFileSizeLimit();
      addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.FILE_UPLOAD_FAILED, sizeLimit.toString()));
    }
  }

  public void validate() {
    boolean fieldFilled = false;
    FileInfo info = (FileInfo) this.innerData;
    fieldFilled = info != null && info.getSize() > 0 && !StringUtils.isBlank(info.getOriginalFilename());

    if (isMandatory() && !(isRead() && fieldFilled)) {
      addErrorWithLabel(UiLibMessages.MANDATORY_FIELD);
    }
    if (!this.mimeTypePermitted) {
      addErrorWithLabel(UiLibMessages.FORBIDDEN_MIME_TYPE);
    }
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * The default implementation for AJAX file upload listener.
   * 
   * @since 1.2.2
   */
  protected class FileUploadActionListener implements ActionListener {

    /**
     * The response that is used, when AJAX file upload is successful.
     */
    public static final String RESPONSE_OK = "OK";

    /**
     * The response that is used, when AJAX file upload fails.
     */
    public static final String RESPONSE_FAIL = "FAIL";

    public void processAction(String actionId, InputData input, OutputData output) throws Exception {
      FileInfo file = (FileInfo) FileUploadControl.this.innerData;
      FileUploadControl.this.ajaxRequest = true;
      convertAndValidate();

      if (file == null || file.isFilePresent()) {
        LOG.debug("Did not get a file!");
        PrintWriter out = ServletUtil.getResponse(output).getWriter();
        out.write(RESPONSE_FAIL);

        if (!FileUploadControl.this.ajaxMessages.isEmpty()) {
          out.print("(");
          boolean isFirst = true;
          for (String msg : FileUploadControl.this.ajaxMessages) {
            if (!isFirst) {
              out.print("\n");
            } else {
              isFirst = false;
            }
            out.print(msg);
          }
          out.print(")");
          FileUploadControl.this.ajaxMessages.clear();
        }
      } else {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Got file '" + file.getOriginalFilename() + "'");
        }
        ServletUtil.getResponse(output).getWriter().write(RESPONSE_OK);
      }

      FileUploadControl.this.ajaxRequest = false;
    }
  }

  /**
   * The view model implementation of <code>FileUploadControl</code>. The view model provides the data for tags to
   * render the control.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends BaseControl<FileInfo>.ViewModel {

    private List<String> permittedMimeFileTypes;

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
    public List<String> getPermittedMimeFileTypes() {
      return this.permittedMimeFileTypes;
    }
    @Override
    public String getControlType() {
      return super.getControlType();
    }
  }
}
