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
      path = StringUtils.substringBefore(path, "$");

      putViewDataOnce("srcLink", createLink(path, "src", "javas", "widgetSource", "Widget source"));
    }
  }

  private void putJspSourceLinkData(TemplateMenuWidget menuWidget) {
    String viewSelector = menuWidget.getFlowViewSelector();
    if (viewSelector != null) {
      putViewDataOnce("templateSrcLink", createLink(viewSelector, "jsp", "xmls", "templateSource", "Template source"));
    }
  }

  private String createLink(String viewSelector, String dir, String ext, String event, String label) {
    StringBuffer result = new StringBuffer("<a href=\"javascript:\" onclick=\"window.open('");
    result.append(((HttpInputData) getInputData()).getContextURL());
    result.append('/');
    result.append(dir);
    result.append('/');
    result.append(viewSelector);
    result.append('.');
    result.append(ext);
    result.append("','");
    result.append(event);
    result.append("','width=900,height=800,scrollbars=yes');return false\">");
    result.append(label);
    result.append("</a>");
    return result.toString();
  }
}
