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
import org.araneaframework.http.PopupServiceInfo;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.form.BaseSystemFormHtmlTag;
import org.araneaframework.jsp.tag.updateRegion.UpdateRegionHtmlTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * Popup registering and rendering tag -- opens popups when HTML file BODY has onload event processing enabled.
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "registerPopups"
 *   body-content = "empty"
 *   description = "Registers popups present in current popupcontext for opening."
 */
public class PopupRegistrationHtmlTag extends BaseTag {
  public static final String POPUP_REGISTRATION_REGION  = "popupRegistrationRegion";
	
  protected int doEndTag(Writer out) throws Exception {
    Object popups = getOutputData().getAttribute(PopupWindowContext.POPUPS_KEY);
    
    UpdateRegionHtmlTag updateRegionTag = new UpdateRegionHtmlTag();
    updateRegionTag.setGlobalId(POPUP_REGISTRATION_REGION);
    registerAndExecuteStartTag(updateRegionTag);

    if (popups != null && !((Map)popups).isEmpty()) {
      JspUtil.writeOpenStartTag(out, "script");
      JspUtil.writeAttribute(out, "type", "text/javascript");
      JspUtil.writeCloseStartTag(out);
      out.write("_ap.addSystemLoadEvent(processPopups);\n");

      addPopups(out, (Map)popups);
      JspUtil.writeEndTag(out, "script");
    }

    executeEndTagAndUnregister(updateRegionTag);
    return super.doEndTag(out);
  }

  protected void addPopups(Writer out, Map popups) throws Exception {
    String systemFormId = (String) requireContextEntry(BaseSystemFormHtmlTag.ID_KEY);
	for (Iterator i = popups.entrySet().iterator(); i.hasNext(); ) {
	  addPopup(out, systemFormId, (Map.Entry)i.next());
	}
  }

  protected void addPopup(Writer out, String systemFormId, Map.Entry popup) throws Exception {
	String serviceId = (String)popup.getKey();
    PopupServiceInfo serviceInfo = (PopupServiceInfo)popup.getValue();

    out.write("addPopup('"  + serviceId + "'");
    out.write(", '");
    out.write(serviceInfo.getPopupProperties() != null ? serviceInfo.getPopupProperties().toString():"");
    out.write("', '");
    out.write(serviceInfo.toURL());
    out.write("');");
  }
}
