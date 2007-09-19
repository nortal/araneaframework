package org.araneaframework.jsp.tag.uilib.newtab;

import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.jsp.JspException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.araneaframework.core.Assert;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.StyledTagInterface;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.newtab.TabContainerWidget;
import org.araneaframework.uilib.newtab.TabWidget;

/**
 * @jsp.tag 
 *  name = "newtabContainer" 
 * 	body-content =  "empty" 
 *  description = "Displays all tabs of the current tab container"
 *
 *  @since 1.1
 */
public class TabContainerTag extends BaseWidgetTag implements StyledTagInterface {
	protected String style = null;
	protected String styleClass = null;
	protected String baseStyleClass = "aranea-tabs";
	
    public static final String TAB_CLASS_SELECTED = "aranea-active-tab";
    public static final String TAB_CLASS_PASSIVE = null;
    public static final String TAB_CLASS_DISABLED = "aranea-disabled-tab";
    
    public static final String TAB_LINK_CLASS = "aranea-tab-link";

	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		Assert.isInstanceOf(TabContainerWidget.class, widget, "<ui:tabContainer> must be used only for referring to TabContainerWidget");
		return SKIP_BODY;
	}
	
	protected int doEndTag(Writer out) throws Exception {
		writeTabsDivStart(out);

		TabContainerWidget tabContainerWidget = (TabContainerWidget) widget;
		Collection tabs = CollectionUtils.select(tabContainerWidget.getChildren().values(), new InstanceofPredicate(TabWidget.class));

		for (Iterator i = tabs.iterator(); i.hasNext();) {
			TabWidget tabwidget = (TabWidget) i.next();

			JspUtil.writeOpenStartTag(out, "div");
			JspUtil.writeAttribute(out, "class", getTabStyleClass(tabwidget));

			writeTablink(out, tabwidget);

			JspUtil.writeEndTag(out, "div");
		}

		writeClearanceDiv(out);
		
		writeTabsDivEnd(out);
		return super.doEndTag(out);
	}
	
	protected void writeClearanceDiv(Writer out) throws Exception {
		out.write("<div class=\"aranea-clear\">&nbsp;</div>");
	}

	protected void writeTablink(Writer out, TabWidget tab) throws Exception {
		JspUtil.writeStartTag(out, "div");
		
	    if (tab.isTabDisabled()) {
	        JspUtil.writeOpenStartTag(out, "a");
	        JspUtil.writeCloseStartTag_SS(out);
	        JspUtil.writeEscaped(out, tab.getLabel());
	        JspUtil.writeEndTag(out, "a");
	      } else {
	        JspUtil.writeOpenStartTag(out, "a");
	        JspUtil.writeAttribute(out, "href", "#"); //TODO: support for open in new window?
	        JspUtil.writeAttribute(out, "class", TAB_LINK_CLASS);

	        UiUpdateEvent event = new UiUpdateEvent(TabContainerWidget.TAB_SELECT_EVENT_ID, fullId, tab.getScope().getId().toString());
	        JspUtil.writeEventAttributes(out, event);

	        JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");

//	        if (tab.getTooltip() != null) {
//	          JspUtil.writeAttribute(out, "arn-toolTip", JspUtil.getResourceString(pageContext, tab.getTooltip()));
//	        }

	        JspUtil.writeCloseStartTag_SS(out);
	        JspUtil.writeEscaped(out, tab.getLabel());
	        JspUtil.writeEndTag_SS(out, "a");
	      }
	    JspUtil.writeEndTag(out, "div");
	}

	protected String getTabStyleClass(TabWidget tabwidget) {
		return 
		tabwidget.isActive() ?
				TAB_CLASS_SELECTED : tabwidget.isTabDisabled() ?
								TAB_CLASS_DISABLED : TAB_CLASS_PASSIVE;
	}

	protected void writeTabsDivEnd(Writer out) throws Exception {
		JspUtil.writeEndTag(out, "div");
	}

	protected void writeTabsDivStart(Writer out) throws Exception {
	    JspUtil.writeOpenStartTag(out, "div");
	    JspUtil.writeAttribute(out, "class", getStyleClass());
	    JspUtil.writeCloseStartTag(out);
	}

	/* *************************   StyledTagInterface  ************************** */
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false" 
	 *   description = "Inline style for HTML tag."
	 */
	public void setStyle(String style) throws JspException {
		this.style = (String) evaluate("style", style, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false" 
	 *   description = "CSS class for tag"
	 */
	public void setStyleClass(String styleClass) throws JspException {
		if (styleClass != null)
			this.styleClass = (String) evaluate("styleClass", styleClass, String.class);
	}

	protected String getStyleClass() {
		return PresentationTag.calculateStyleClass(baseStyleClass, styleClass);
	}
}
