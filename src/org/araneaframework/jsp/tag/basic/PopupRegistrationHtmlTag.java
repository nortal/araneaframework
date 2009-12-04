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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.http.PopupServiceInfo;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.filter.StandardPopupFilterWidget;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Popup registering and rendering tag -- opens popups when HTML file BODY has onload event processing enabled.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *  name = "registerPopups"
 *  body-content = "empty"
 *  description = "Registers popups present in current PopupContext for opening."
 */
public class PopupRegistrationHtmlTag extends BaseTag {

  @Override
  protected int doEndTag(Writer out) throws Exception {
    PopupWindowContext popupWindowContext = getEnvironment().requireEntry(PopupWindowContext.class);
    Map<String, PopupServiceInfo> popups = popupWindowContext.getPopups();

    if (popups != null && !popups.isEmpty()) {
      JspUtil.writeOpenStartTag(out, "script");
      JspUtil.writeAttribute(out, "type", "text/javascript");
      JspUtil.writeCloseStartTag(out);
      out.write("document.observe('dom:loaded', Aranea.Popup.processPopups);\n");
      addPopups(out, popups);
      JspUtil.writeEndTag(out, "script");
    }

    return super.doEndTag(out);
  }

  protected void addPopups(Writer out, Map<String, PopupServiceInfo> popups) throws Exception {
    PopupWindowContext popupWindowContext = getEnvironment().requireEntry(PopupWindowContext.class);
    for (Map.Entry<String, PopupServiceInfo> next : popups.entrySet()) {
      addPopup(out, next);
      ((StandardPopupFilterWidget) popupWindowContext).renderPopup(next.getKey());
    }
  }

  protected void addPopup(Writer out, Map.Entry<String, PopupServiceInfo> popup) throws Exception {
    out.write("Aranea.Popup.addPopup('");
    out.write(popup.getKey());
    out.write("', '");
    out.write(popup.getValue().toURL());
    out.write("', '");
    out.write(ObjectUtils.toString(popup.getValue().getPopupProperties()));
    out.write("');");
  }
}
