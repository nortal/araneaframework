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
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 * 
 * @jsp.tag
 *   name = "registerOverlay"
 *   body-content = "empty"
 *   description = "TODO"
 */
public class OverlayRegistrationHtmlTag extends BaseTag {

  protected int doEndTag(Writer out) throws Exception {
    OverlayContext overlayCtx = (OverlayContext) getEnvironment().requireEntry(OverlayContext.class);

    if (overlayCtx.isOverlayActive()) {
      JspUtil.writeOpenStartTag(out, "script");
      JspUtil.writeAttribute(out, "type", "text/javascript");
      JspUtil.writeCloseStartTag(out);
      out.write("_ap.addClientLoadEvent(aranea_showModalBox);\n");
      JspUtil.writeEndTag(out, "script");
    }

    return super.doEndTag(out);
  }

}
