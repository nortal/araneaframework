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

import org.apache.commons.lang.StringUtils;

import java.io.Writer;
import org.araneaframework.http.WindowScrollPositionContext;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.util.JspUtil;
/**
 * Tag that registers functions dealing with window scroll position storing and restoring.
 * @author Taimo Peelo (taimo@araneaframework.org)
 * 
 * @jsp.tag
 *   name = "registerScrollHandler"
 *   body-content = "empty"
 *   description = "Registers popups present in current popup-context for opening. For this tag to work, produced HTML file BODY should have attribute onload='processLoadEvents()'".
 */
public class ScrollHandlerRegistrationHtmlTag extends BaseTag {

  protected static final String DEFAULT_POS = "0";

  @Override
  protected int doEndTag(Writer out) throws Exception {
    WindowScrollPositionContext scrollHandler = getEnvironment().getEntry(WindowScrollPositionContext.class);

    if (scrollHandler != null) {
      registerScrollHandler(out, scrollHandler);
    }

    return EVAL_PAGE;
  }

  protected void registerScrollHandler(Writer out, WindowScrollPositionContext scrollHandler) throws Exception {
    String x = StringUtils.isNumeric(scrollHandler.getX()) ? scrollHandler.getX() : DEFAULT_POS;
    String y = StringUtils.isNumeric(scrollHandler.getY()) ? scrollHandler.getY() : DEFAULT_POS;

    JspUtil.writeHiddenInputElement(out, WindowScrollPositionContext.WINDOW_SCROLL_X_KEY, x);
    JspUtil.writeHiddenInputElement(out, WindowScrollPositionContext.WINDOW_SCROLL_Y_KEY, y);

    // ensure restoration of scroll position:
    out.write("<script type=\"text/javascript\">Aranea.Util.setWindowCoordinates(");
    out.write(x);
    out.write(",");
    out.write(y);
    out.write(");</script>");
  }
}
