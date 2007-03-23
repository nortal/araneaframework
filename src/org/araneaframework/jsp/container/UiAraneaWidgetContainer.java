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

package org.araneaframework.jsp.container;

import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.http.JspContext;

public class UiAraneaWidgetContainer implements UiWidgetContainer {
  protected ApplicationWidget rootWidget;
  private JspContext conf;
  
  public UiAraneaWidgetContainer(ApplicationWidget rootWidget, JspContext conf) {
    this.rootWidget = rootWidget;
    this.conf = conf;
  }

  public Map getWidgets() {
    return rootWidget._getComposite().getChildren();
  }

  public String scopeWidgetFullId(PageContext pageContext, String fullWidgetId) throws JspException {
    return fullWidgetId;
  }

  public Map getTagMapping(PageContext pageContext, String uri) {
    return conf.getTagMapping(uri);
  }

}
