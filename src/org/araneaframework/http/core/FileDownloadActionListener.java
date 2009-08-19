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

import java.io.Serializable;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Service;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.http.PopupServiceInfo;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.filter.StandardPopupFilterWidget;
import org.araneaframework.http.service.FileDownloaderService;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.support.FileInfo;

/**
 * An action listener for downloading files with AJAX. Register this listener in your widget, and
 * provide a callback or some data to it. In JSP, write a script like this:
 * <code>return AraneaPage.downloadFile('download','${widgetId}','${rowRequestId}');</code>. The
 * {@link FileDownloadHandler} is the callback used to retrieve file data to upload it to the client
 * once file upload is requested.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public class FileDownloadActionListener extends StandardActionListener {

  public static final String ERROR_CODE = "error";

  protected StandardPopupFilterWidget popupCtx;

  protected FileDownloadHandler downloadHandler;

  protected String lastPopupId;

  public FileDownloadActionListener(PopupWindowContext popupCtx, byte[] file, String fileName,
      String contentType) {
    this(popupCtx, new FileDownloadHandler(file, fileName, contentType));
  }

  public FileDownloadActionListener(PopupWindowContext popupCtx, FileInfo file) {
    this(popupCtx, new FileDownloadHandler(file));
  }

  public FileDownloadActionListener(PopupWindowContext popupCtx, FileDownloadHandler fileHandler) {
    Assert.notNullParam(this, popupCtx, "popupCtx");
    Assert.notNullParam(this, fileHandler, "fileHandler");
    this.popupCtx = (StandardPopupFilterWidget) popupCtx;
    this.downloadHandler = fileHandler;
  }

  public FileDownloadActionListener(PopupWindowContext popupCtx, Service service) {
    Assert.notNullParam(this, popupCtx, "popupCtx");
    Assert.notNullParam(this, service, "service");
    this.popupCtx = (StandardPopupFilterWidget) popupCtx;
  }

  public void processAction(String actionId, String actionParam, InputData input, OutputData output)
      throws Exception {

    if (this.lastPopupId != null) {
      this.popupCtx.close(this.lastPopupId);
    }

    Service service = this.downloadHandler.getService(actionId, actionParam, input, output);
    this.lastPopupId = this.popupCtx.open(service, new PopupWindowProperties(), null);

    String url = ERROR_CODE;

    try {
      // If this is called then the popup will be removed later.
      // This is important because we don't want to open previously opened popups.
      this.popupCtx.renderPopup(this.lastPopupId);
      url = ((PopupServiceInfo) this.popupCtx.getPopups().get(this.lastPopupId)).toURL();

    } finally {
      // We return the URL of the popup to the client.
      // If no popup found then "error" will be returned.
      ServletUtil.getResponse(output).getOutputStream().print(url);
    }
  }

  public static class FileDownloadHandler implements Serializable {

    protected byte[] file;

    protected String fileName;

    protected String contentType;

    protected Service fileDownloaderService;

    public FileDownloadHandler() {}

    public FileDownloadHandler(byte[] file, String fileName, String contentType) {
      this.file = file;
      this.fileName = fileName;
      this.contentType = contentType;
    }

    public FileDownloadHandler(FileInfo file) {
      readData(file);
    }

    public FileDownloadHandler(Service fileDownloaderService) {
      this.fileDownloaderService = fileDownloaderService;
    }

    protected void readData(FileInfo file) {
      this.file = file.readFileContent();
      this.fileName = file.getFileName();
      this.contentType = file.getContentType();
    }

    public Service getService(String actionId, String actionParam, InputData input, OutputData output) {
      Service service = this.fileDownloaderService;
      if (service == null) {
        service = createService(actionId, actionParam, input, output);
      }
      return service;
    }

    /**
     * Called before the file data is requested to notify the file handler about the request data.
     * 
     * @param actionId The action identifier.
     * @param actionParam The action parameter.
     * @param input The request data.
     * @param output The response data.
     */
    protected Service createService(String actionId, String actionParam,
        InputData input, OutputData output) {

      prepareServiceData(actionId, actionParam, input, output);

      FileDownloaderService service = new FileDownloaderService(this.file, this.contentType,
          this.fileName);
      service.setContentDispositionInline(Boolean.FALSE);
      return service;
    }

    /**
     * Called before the file data is requested to notify the file handler about the request data.
     * 
     * @param actionId The action identifier.
     * @param actionParam The action parameter.
     * @param input The request data.
     * @param output The response data.
     */
    protected void prepareServiceData(String actionId, String actionParam,
        InputData input, OutputData output) {
    }
  }
}
