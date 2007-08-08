/**
 * Copyright 2006 Webmedia Group Ltd. Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 */
package org.araneaframework.jsp.tag.uilib.tab;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.jsp.tag.updateregion.UpdateRegionHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.tab.Tab;
import org.araneaframework.uilib.tab.TabContext;
import org.araneaframework.uilib.util.NameUtil;

/**
 * @jsp.tag name = "tabContainer" body-content = "JSP" description = "Displays all tabs of the current tab container"
 * @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 */
public class TabContainerTag extends BaseWidgetTag {

  public static final String TABS_SELECT_EVENT_ID = "select";
  public static final String TABS_CONTAINER_UPDATE_REGION_ID = "tab-container";
  public static final String TABS_CONTAINER_KEY = "tabContainerId";

  private static final String TAB_STYLE_SELECTED = "item-active";
  private static final String TAB_STYLE_NORMAL = "item";
  private static final String TAB_STYLE_DISABLED = "item-passive";

  protected UpdateRegionHtmlTag updateRegion;

  public int doStartTag(final Writer out) throws Exception {
    super.doStartTag(out);

    addContextEntry(TABS_CONTAINER_KEY, id);

    updateRegion = new UpdateRegionHtmlTag();
    registerSubtag(updateRegion);
    updateRegion.setGlobalId(NameUtil.getFullName(fullId,TABS_CONTAINER_UPDATE_REGION_ID));
    executeStartSubtag(updateRegion);

    // start div class tabs
    JspUtil.writeOpenStartTag(out, "div");
    JspUtil.writeAttribute(out, "class", "tabs");
    JspUtil.writeCloseStartTag(out);

    final TabContext tabContext = (TabContext) widget;
    final LinkedMap tabs = tabContext.getTabs();
    final Tab selectedTab = tabContext.getSelectedTab();
    for (int i = 0; i < tabs.size(); i++) {
      final Tab tab = (Tab) tabs.getValue(i);
      final boolean selected = tab.equals(selectedTab);

      JspUtil.writeOpenStartTag(out, "div");
      JspUtil.writeAttribute(out, "class", getTabStyle(tab.isEnabled(), selected));
      JspUtil.writeCloseStartTag(out);
      JspUtil.writeStartTag(out, "div");

      writeEventButtonTag(out, tab);

      JspUtil.writeEndTag(out, "div");
      JspUtil.writeEndTag(out, "div");
    }

    out.write("<div class=\"clear1\">&nbsp;</div>");
    out.write("</div>");// tabs

    return EVAL_BODY_INCLUDE;
  }

  public int doEndTag(Writer out) throws Exception {
    executeEndTagAndUnregister(updateRegion);
    return EVAL_PAGE;
  }

  private static String getTabStyle(final boolean enabled, final boolean selected) {
    if (selected) {
      return TAB_STYLE_SELECTED;
    }
    return enabled ? TAB_STYLE_NORMAL : TAB_STYLE_DISABLED;
  }

  protected void writeEventButtonTag(final Writer out, final Tab tab) throws Exception {
    if (!tab.isEnabled()) {
      // disabled tab
      JspUtil.writeOpenStartTag(out, "a");
      JspUtil.writeCloseStartTag_SS(out);
      JspUtil.writeEscaped(out, JspUtil.getResourceStringOrNull(pageContext, tab.getLabelId()));
      JspUtil.writeEndTag(out, "a");
    } else {

      JspUtil.writeOpenStartTag(out, "a");
      JspUtil.writeAttribute(out, "href", "javascript:");

      List updateRegionNames = new ArrayList();
      updateRegionNames.add(NameUtil.getFullName(fullId,TABS_CONTAINER_UPDATE_REGION_ID));

      UiUpdateEvent event = new UiUpdateEvent(TABS_SELECT_EVENT_ID, fullId, tab.getId(), updateRegionNames);
      JspUtil.writeEventAttributes(out, event);

      JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");
      out.write(JspWidgetCallUtil.getSubmitScriptForEvent());

      if (tab.getTooltip() != null) {
        JspUtil.writeAttribute(out,
                               "onMouseOver",
                               "if (typeof(aranea_showTooltip) == \"function\") aranea_showTooltip('"
                                   + JspUtil.getResourceString(pageContext, tab.getTooltip()) + "', this, event)");
        JspUtil.writeAttribute(out,
                               "onMouseOut",
                               "if (typeof(aranea_hideTooltip) == \"function\") aranea_hideTooltip()");
      }

      JspUtil.writeCloseStartTag_SS(out);
      out.write(JspUtil.getResourceString(pageContext, tab.getLabelId()));
      JspUtil.writeEndTag_SS(out, "a");

    }
  }
}
