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

package org.araneaframework.http.service;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.BaseService;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.filter.StandardPopupFilterWidget.StandardPopupServiceInfo;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.util.JspStringUtil;
import org.araneaframework.uilib.util.MessageUtil;

/**
 * Service that returns response that closes browser window that made the request; and if possible, reloads the opener
 * of that window.
 * <p>
 * In 2.0 version, this class was simplified for extending the default solution. Now the referenced JavaScript files can
 * be specified using the {@link #setScriptsImportFileNames(String[])} variable, the parent window reloading script can
 * be specified through {@link #setReloadParentScript(String)} (note that URL will be injected to the positional
 * parameter '{0}'!), and the popup close script can be customized through {@link #setDelayedCloseScript(String)}
 * variable. Using these three properties should be enough to avoid changing the HTML response entirely for most of the
 * cases.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Martti Tamm (martti@araneaframework.org)
 */
public class WindowClosingService extends BaseService {

  protected Environment closableComponentEnv;

  protected String[] scriptsImportFileNames = { "js/prototype/prototype.js", "js/aranea/src/aranea-popups.js" };

  protected String reloadParentScript = "Aranea.Popup.reloadParentWindow(''{0}'');";

  protected String delayedCloseScript = "Aranea.Popup.delayedCloseWindow(50);";

  public WindowClosingService(Environment closableComponentEnv) {
    this.closableComponentEnv = closableComponentEnv;
  }

  @Override
  public Environment getEnvironment() {
    return this.closableComponentEnv != null ? this.closableComponentEnv : super.getEnvironment();
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);

    PopupWindowContext popupCtx = EnvironmentUtil.getPopupWindowContext(getEnvironment());
    BaseApplicationWidget opener = null;

    if (popupCtx != null) {
      opener = (BaseApplicationWidget) popupCtx.getOpener();
    }

    StandardPopupServiceInfo serviceInfo = null;
    if (opener != null) {
      String threadId = EnvironmentUtil.requireThreadServiceId(opener.getEnvironment());
      String topserviceId = EnvironmentUtil.requireTopServiceId(opener.getEnvironment());
      String url = ServletUtil.getEncodedContainerUrl(getInputData());
      serviceInfo = new StandardPopupServiceInfo(topserviceId, threadId, null, url);
      serviceInfo.setTransactionOverride(false);
    }


    // Composing the BODY.onLoad script:
    StringBuffer script = new StringBuffer();
    if (serviceInfo != null && !StringUtils.isBlank(this.reloadParentScript)) {
      script.append(MessageUtil.format(this.reloadParentScript, serviceInfo.getUrl()));
    }
    script.append(StringUtils.defaultIfEmpty(this.delayedCloseScript, ""));


    // Composing the full HTML response:
    StringBuffer content = new StringBuffer("<html><head><title>Closing your window...</title>");

    for (String scriptSrc : this.scriptsImportFileNames) {
      if (!StringUtils.isBlank(scriptSrc)) {
        content.append("<script type=\"text/javascript\" src=\"");
        content.append(FileImportUtil.getImportString(scriptSrc, input));
        content.append("\"></script>");
      }
    }

    content.append("</head><body onload=\"");
    content.append(JspStringUtil.escapeHtmlEntities(script.toString()));
    content.append("\"></body></html>");


    // Writing to HTTP:
    response.setContentType("text/html");
    response.setContentLength(content.length());
    IOUtils.write(content, response.getOutputStream());

    ManagedServiceContext mngCtx = EnvironmentUtil.requireManagedService(getEnvironment());
    mngCtx.close(mngCtx.getCurrentId());
  }

  /**
   * The script that closes the popup, with delay (recommended). It is called right after {@link #reloadParentScript}.
   * 
   * @param delayedCloseScript The script that closes the popup, with delay (recommended). May be <code>null</code>.
   * @since 2.0
   */
  public void setDelayedCloseScript(String delayedCloseScript) {
    this.delayedCloseScript = delayedCloseScript;
  }

  /**
   * The script that reloads popup parent page. It is called right before {@link #delayedCloseScript}.
   * 
   * @param reloadParentScript The script that reloads popup parent page. May be <code>null</code>.
   * @since 2.0
   */
  public void setReloadParentScript(String reloadParentScript) {
    this.reloadParentScript = reloadParentScript;
  }

  /**
   * Sets the scripts that the file importer will load. These scripts should be necessary to close the popup.
   * 
   * @param scriptsImportFileNames The paths to javascript files that file-importer recognizes.
   * @since 2.0
   */
  public void setScriptsImportFileNames(String[] scriptsImportFileNames) {
    this.scriptsImportFileNames = scriptsImportFileNames;
  }
}
