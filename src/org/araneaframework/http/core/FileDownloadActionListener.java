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
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.filter.StandardPopupFilterWidget;
import org.araneaframework.http.service.FileDownloaderService;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.http.widget.FileDownloaderWidget.FileDownloadStreamCallback;
import org.araneaframework.uilib.support.FileInfo;

/**
 * An action listener for downloading files with AJAX. Register this listener in your widget, and provide a callback or
 * some data to it. In JSP, write a script like this:
 * <code>return Aranea.Page.downloadFile('download','${widgetId}','${rowRequestId}');</code>. The
 * {@link FileDownloadActionListener.FileDownloadHandler} is the callback used to retrieve file data to upload it to the
 * client once file upload is requested.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
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

  public FileDownloadActionListener(PopupWindowContext popupCtx, Widget fileDownloaderWidget) {
    this(popupCtx, new FileDownloadHandler(fileDownloaderWidget));
  }

  @Override
  public void processAction(String actionId, String actionParam, InputData input, OutputData output)
      throws Exception {

    if (this.lastPopupId != null) {
      this.popupCtx.close(this.lastPopupId);
    }

    Widget widget = new AsynchronousFileDownloadWidget(actionId, actionParam, this.downloadHandler);
    this.lastPopupId = this.popupCtx.open(widget, new PopupWindowProperties(), null);

    String url = ERROR_CODE;

    try {
      // If this is called then the popup will be removed later.
      // This is important because we don't want to open previously opened popups.
      this.popupCtx.renderPopup(this.lastPopupId);
      url = this.popupCtx.getPopups().get(this.lastPopupId).toURL();

    } finally {
      // We return the URL of the popup to the client.
      // If no popup found then "error" will be returned.
      ServletUtil.getResponse(output).getOutputStream().print(url);
    }
  }

  /**
   * A temporary widget that invokes the handler's service upon request to commence file downloading. The idea of this
   * widget is to delay the creation of file download widget until it is really necessary. Since this widget is used
   * inside popup context, the request that comes in when actual file downloading should begin. Therefore, this widget
   * indeed procrastinates creating file downloading widget until it is really needed.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  protected static class AsynchronousFileDownloadWidget extends BaseApplicationWidget {

    private FileDownloadHandler handler;

    private String actionId;

    private String actionParam;

    
    public AsynchronousFileDownloadWidget(String actionId, String actionParam, FileDownloadHandler handler) {
      this.actionId = actionId;
      this.actionParam = actionParam;
      this.handler = handler;
    }

    @Override
    protected void action(Path path, InputData input, OutputData output) throws Exception {
      Widget widget = this.handler.getWidget(this.actionId, this.actionParam, input, output);
      if (!widget.isAlive()) {
        addWidget("service", widget);
      }
      widget._getService().action(path, input, output);
    }
  }

  public static class FileDownloadHandler implements Serializable {

    protected byte[] file;

    protected String fileName;

    protected String contentType;

    protected Widget fileDownloaderWidget;

    protected FileDownloadStreamCallback fileStreamCallback;

    public FileDownloadHandler() {}

    public FileDownloadHandler(byte[] file, String fileName, String contentType) {
      this.file = file;
      this.fileName = fileName;
      this.contentType = contentType;
    }

    public FileDownloadHandler(FileInfo file) {
      readData(file);
    }

    public FileDownloadHandler(Widget fileDownloaderWidget) {
      this.fileDownloaderWidget = fileDownloaderWidget;
    }

    public FileDownloadHandler(FileDownloadStreamCallback fileStreamCallback) {
      this.fileStreamCallback = fileStreamCallback;
    }

    protected void readData(FileInfo file) {
      this.file = file.readFileContent();
      this.fileName = file.getFileName();
      this.contentType = file.getContentType();
    }

    public Widget getWidget(String actionId, String actionParam, InputData input, OutputData output) {
      Widget widget = this.fileDownloaderWidget;
      if (widget == null) {
        widget = createService(actionId, actionParam, input, output);
      }
      return widget;
    }

    /**
     * Called before the file data is requested to notify the file handler about the request data.
     * 
     * @param actionId The action identifier.
     * @param actionParam The action parameter.
     * @param input The request data.
     * @param output The response data.
     */
    protected Widget createService(String actionId, String actionParam,
        InputData input, OutputData output) {

      prepareServiceData(actionId, actionParam, input, output);

      FileDownloaderService service = null;

      if (this.fileStreamCallback != null) {
        service = new FileDownloaderService(this.fileStreamCallback);
      } else {
        service = new FileDownloaderService(this.file, this.contentType, this.fileName);
      }

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
