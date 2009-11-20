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

package org.araneaframework.example.main.web;

import org.apache.commons.lang.StringUtils;
import org.araneaframework.AraneaVersion;
import org.araneaframework.OutputData;
import org.araneaframework.example.common.framework.TemplateMenuWidget;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.util.UilibEnvironmentUtil;

/**
 * The widget that handles the footer logic of rendered pages.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FooterWidget extends BaseUIWidget {

  @Override
  protected void init() throws Exception {
    putViewData("aranea-version", AraneaVersion.getVersion());
    setViewSelector("mainlayout/footer");
  }

  @Override
  protected void render(OutputData output) throws Exception {
    TemplateMenuWidget menuWidget = (TemplateMenuWidget) UilibEnvironmentUtil.getMenuContext(getEnvironment());
    putJavaSourceLinkData(menuWidget);
    putJspSourceLinkData(menuWidget);
    super.render(output);
  }

  private void putJavaSourceLinkData(TemplateMenuWidget menuWidget) {
    String flowClassName = menuWidget.getFlowClassName();

    if (flowClassName != null) {
      String[] replaceFrom = { ".class", "." };
      String[] replaceTo = { "", "/" };
      String path = StringUtils.replaceEach(flowClassName, replaceFrom, replaceTo);

      while (StringUtils.contains(path, '$')) {
        path = StringUtils.substringBefore(path, "$");
      }

      StringBuffer reqUrl = new StringBuffer(((HttpInputData) getInputData()).getContextURL());
      reqUrl.append("/src/");
      reqUrl.append(path);
      reqUrl.append(".javas");

      StringBuffer srcLink = new StringBuffer("<a href=\"javascript:\" onclick=\"window.open('");
      srcLink.append(reqUrl);
      srcLink.append("', 'widgetSource', 'width=900,height=800,scrollbars=yes'); return false;\">Widget source</a>");

      putViewDataOnce("srcLink", srcLink.toString());
    }
  }

  private void putJspSourceLinkData(TemplateMenuWidget menuWidget) {
    String viewSelector = menuWidget.getFlowViewSelector();
    if (viewSelector != null) {
      StringBuffer reqUrl = new StringBuffer(((HttpInputData) getInputData()).getContextURL());
      reqUrl.append("/jsp/");
      reqUrl.append(viewSelector);
      reqUrl.append(".xmls");

      StringBuffer windowOpen = new StringBuffer("window.open('").append(reqUrl).append(
          "', 'templateSource', 'width=900,height=800,scrollbars=yes')");

      StringBuffer templateSrcLink = new StringBuffer("<a href=\"javascript:\" onclick=\"").append(windowOpen).append(
          "; return false;\">");
      templateSrcLink.append("Template source").append("</a>");

      putViewDataOnce("templateSrcLink", templateSrcLink.toString());
    }
  }
}
