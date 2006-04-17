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

package org.araneaframework.jsp.tag.basic;

import java.io.Writer;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.jsp.PageContext;
import org.araneaframework.OutputData;
import org.araneaframework.framework.router.StandardThreadServiceRouterService;
import org.araneaframework.framework.router.StandardTopServiceRouterService;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.aranea.UiAraneaRootTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.PopupWindowContext;
import org.araneaframework.servlet.filter.StandardPopupFilterWidget;

/**
 * Popup registering and rendering tag -- opens popups when HTML file BODY has onload event processing enabled.
 * @author Taimo Peelo (taimo@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "registerPopups"
 *   body-content = "empty"
 *   description = "Registers popups present in current popupcontext for opening. For this tag to work, produced HTML file BODY should have attribute onload='processLoadEvents()'".
 */
public class UiPopupRegistrationTag extends UiBaseTag {
	
  protected int after(Writer out) throws Exception {
    OutputData output = (OutputData) readAttribute(UiAraneaRootTag.OUTPUT_DATA_KEY, PageContext.REQUEST_SCOPE);

    Object popups = output.getAttribute(PopupWindowContext.POPUPS_KEY);
    if (popups != null && !((Map)popups).isEmpty()) {
      UiUtil.writeOpenStartTag(out, "script");
      UiUtil.writeAttribute(out, "type", "text/javascript");
      UiUtil.writeAttribute(out, "language", "JavaScript1.2");
      UiUtil.writeCloseStartTag(out);
      out.write("addSystemLoadEvent(processPopups);\n");
      
      addPopups(out, (Map)popups);
      UiUtil.writeEndTag(out, "script");
    }

    return super.after(out);
  }

  protected void addPopups(Writer out, Map popups) throws Exception {
    String systemFormId = (String) readAttribute(UiSystemFormTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
	for (Iterator i = popups.entrySet().iterator(); i.hasNext(); ) {
	  Map.Entry current = (Map.Entry)i.next();
	  String serviceId = (String)current.getKey();
	  StandardPopupFilterWidget.PopupServiceInfo serviceInfo = (StandardPopupFilterWidget.PopupServiceInfo)current.getValue();
	  
	  StringBuffer urlSuffix = new StringBuffer("?" + StandardTopServiceRouterService.TOP_SERVICE_KEY + "=").append(serviceInfo.getTopServiceId());
	  if (serviceInfo.getThreadId() != null) {
        urlSuffix.append("&" + StandardThreadServiceRouterService.THREAD_SERVICE_KEY + "="); 
        urlSuffix.append(serviceInfo.getThreadId());
	  }
	  
	  out.write("addPopup('"  + serviceId + "', '" + 
			  (serviceInfo.getPopupProperties() != null ? serviceInfo.getPopupProperties().toString():"")+ 
			  "', '"  + urlSuffix.toString() + "', '" + systemFormId + "');\n");
	}
  }
}
