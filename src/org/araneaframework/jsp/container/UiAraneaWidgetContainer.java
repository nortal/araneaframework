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

import java.util.List;
import java.util.Map;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.core.Custom;
import org.araneaframework.core.Custom.CustomWidget;
import org.araneaframework.jsp.util.UiUpdateRegionUtil;
import org.araneaframework.servlet.filter.StandardJspFilterService;

public class UiAraneaWidgetContainer implements UiWidgetContainer {
  protected Custom.CustomWidget rootWidget;
  private StandardJspFilterService.JspConfiguration conf;
  
  public UiAraneaWidgetContainer(CustomWidget rootWidget, StandardJspFilterService.JspConfiguration conf) {
    this.rootWidget = rootWidget;
    this.conf = conf;
  }

  public Map getWidgets() {
    return rootWidget._getComposite().getChildren();
  }

  public String scopeWidgetFullId(PageContext pageContext, String fullWidgetId) throws JspException {
    return fullWidgetId;
  }

  public String buildWidgetCall(String systemFormId, String fullWidgetId, String eventId, String eventParam, List updateRegions) throws JspException {
    StringBuffer result = new StringBuffer();
    result.append("araneaSubmitEvent(document.");
    result.append(systemFormId);
    result.append(", '");
    result.append(fullWidgetId);
    result.append("', '");
    result.append(eventId);  
    result.append("', '");
    result.append(eventParam); 
    result.append("', ");
    result.append(UiUpdateRegionUtil.formatUpdateRegionsJS(updateRegions));       
    result.append(");");
    return result.toString();
  }

  public Map getTagMapping(PageContext pageContext) throws JspException {
    return conf.getTagMapping();
  }

}
