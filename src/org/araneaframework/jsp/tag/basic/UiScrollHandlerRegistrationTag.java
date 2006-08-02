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
import org.araneaframework.OutputData;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.aranea.UiAraneaRootTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.core.WindowScrollPositionContext;
/**
 * Tag that registers functions dealing with window scroll position storing and restoring.
 * @author Taimo Peelo (taimo@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "registerScrollHandler"
 *   body-content = "empty"
 *   description = "Registers popups present in current popupcontext for opening. For this tag to work, produced HTML file BODY should have attribute onload='processLoadEvents()'".
 */
public class UiScrollHandlerRegistrationTag extends UiBaseTag {
   protected int doEndTag(Writer out) throws Exception {
     OutputData output = (OutputData) requireContextEntry(UiAraneaRootTag.OUTPUT_DATA_KEY);
     
     WindowScrollPositionContext scrollHandler = (WindowScrollPositionContext)output.getAttribute(WindowScrollPositionContext.SCROLL_HANDLER_KEY);

     if (scrollHandler != null)
       registerScrollHandler(out, scrollHandler);
       
	 return EVAL_PAGE;
   }
   
   protected void registerScrollHandler(Writer out, WindowScrollPositionContext scrollHandler) throws Exception {
     // ensure that tag is used inside systemform 
     String systemFormId = (String)requireContextEntry(UiSystemFormTag.ID_KEY);
     String x = scrollHandler.getX();
     String y = scrollHandler.getY();

     if (x != null || y != null) {
       UiUtil.writeHiddenInputElement(out, WindowScrollPositionContext.WINDOW_SCROLL_X_KEY, x != null ? x : "0");
       UiUtil.writeHiddenInputElement(out, WindowScrollPositionContext.WINDOW_SCROLL_Y_KEY, y != null ? y : "0");
     }
     
     out.write("<script>");
     // ensure restoration of scroll position
     out.write("addSystemLoadEvent(function() {" +
     		"scrollToCoordinates("+
    		"document.forms." + systemFormId + "."+ WindowScrollPositionContext.WINDOW_SCROLL_X_KEY + ".value, " +
     		"document.forms." + systemFormId + "."+ WindowScrollPositionContext.WINDOW_SCROLL_Y_KEY + ".value)});");
     
     // ensure that the scroll coordinates are submitted with request
     out.write("uiAddSystemFormSubmitCallback("+
    		 "document.forms." + systemFormId + ", " +
    		 "function() {saveScrollCoordinates(document.forms." + systemFormId +")});"
    		 );
     out.write("</script>");
   }
}
