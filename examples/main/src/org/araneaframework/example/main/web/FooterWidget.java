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

package org.araneaframework.example.main.web;

import org.araneaframework.AraneaVersion;
import org.araneaframework.OutputData;
import org.araneaframework.example.common.framework.TemplateMenuContext;
import org.araneaframework.example.common.framework.TemplateMenuWidget;
import org.araneaframework.http.util.URLUtil;
import org.araneaframework.uilib.core.StandardPresentationWidget;

/**
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class FooterWidget extends StandardPresentationWidget {
  protected void init() throws Exception {
    super.init();
    putViewData("aranea-version", AraneaVersion.getVersion());
    setViewSelector("mainlayout/footer");
  }

   protected void render(OutputData output) throws Exception {
    TemplateMenuWidget menuWidget = (TemplateMenuWidget) getEnvironment().getEntry(TemplateMenuContext.class);

    /* widget source */
    String flowClassName = menuWidget.getFlowClassName();
    if (flowClassName != null) {
      String path = flowClassName.replaceAll("\\.class", "").replaceAll("\\.", "/");
      while (path.lastIndexOf('$') != -1) {
        int index = path.lastIndexOf('$');
        path = path.substring(0, index);
      }

      StringBuffer reqUrl = new StringBuffer(URLUtil.getContextRequestURL(getCurrentInput()));
      reqUrl.append("/src/");
      reqUrl.append(path);
      reqUrl.append(".javas");
      
      StringBuffer windowOpen = new StringBuffer("window.open('").append(reqUrl).append("', 'widgetSource', 'width=900,height=800,scrollbars=yes')");
      
      StringBuffer srcLink = new StringBuffer("<a href=\"javascript:\" onclick=\"").append(windowOpen).append("; return false;\">");
      srcLink.append("Widget source").append("</a>");

      putViewDataOnce("srcLink", srcLink.toString());
    }
    
    /* view source */
    String viewSelector = menuWidget.getFlowViewSelector();
    if (viewSelector != null) {
      StringBuffer reqUrl = new StringBuffer(URLUtil.getContextRequestURL(getCurrentInput()));
      reqUrl.append("/jsp/");
      reqUrl.append(viewSelector);
      reqUrl.append(".xmls");
      
      StringBuffer windowOpen = new StringBuffer("window.open('").append(reqUrl).append("', 'templateSource', 'width=900,height=800,scrollbars=yes')");
      
      StringBuffer templateSrcLink = new StringBuffer("<a href=\"javascript:\" onclick=\"").append(windowOpen).append("; return false;\">");
      templateSrcLink.append("Template source").append("</a>");
      
      putViewDataOnce("templateSrcLink", templateSrcLink.toString());
    }

	super.render(output);
  }
}
