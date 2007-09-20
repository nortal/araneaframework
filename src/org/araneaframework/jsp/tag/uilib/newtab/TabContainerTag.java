package org.araneaframework.jsp.tag.uilib.newtab;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.servlet.jsp.JspException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.InstanceofPredicate;
import org.araneaframework.core.Assert;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.StyledTagInterface;
import org.araneaframework.jsp.tag.uilib.BaseWidgetTag;
import org.araneaframework.jsp.tag.updateregion.UpdateRegionHtmlTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.newtab.TabContainerWidget;
import org.araneaframework.uilib.newtab.TabWidget;
import org.araneaframework.uilib.util.NameUtil;

/**
 * @jsp.tag 
 *  name = "newtabContainer" 
 * 	body-content =  "JSP" 
 *  description = "Displays all tabs of the current tab container"
 *  
 *  @author Nikita Salnikov-Tarnovski (<a href="mailto:nikem@webmedia.ee">nikem@webmedia.ee</a>)
 *  @author Taimo Peelo (taimo@araneaframework.org)
 *  
 *  @since 1.1
 */
public class TabContainerTag extends BaseWidgetTag implements StyledTagInterface {
	protected String style = null;
	protected String styleClass = null;
	protected String baseStyleClass = "aranea-tabs";
	protected boolean useUpdateRegions = false;
	protected UpdateRegionHtmlTag updateRegionTag;
	
	/** Context entry key for {@link TabContainerWidget} rendered by this tag. */
	public static final String TAB_CONTAINER_WIDGET = "tabContainerWidget";
	public static final String TAB_CONTAINER_UPDATE_REGION_NAME = "tcur";
	
    public static final String TAB_CLASS_SELECTED = "aranea-active-tab";
    public static final String TAB_CLASS_PASSIVE = null;
    public static final String TAB_CLASS_DISABLED = "aranea-disabled-tab";
    
    public static final String TAB_LINK_CLASS = "aranea-tab-link";
    

	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		Assert.isInstanceOf(TabContainerWidget.class, widget, "<ui:tabContainer> must be used only for referring to TabContainerWidget");
		addContextEntry(TAB_CONTAINER_WIDGET, widget);
		
		// optionally write out update region tag start
		writeUpdateRegionStart();

		// WRITE OUT TABS
		writeTabsDivStart(out);

		TabContainerWidget tabContainerWidget = (TabContainerWidget) widget;
		// TODO: better
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

		return EVAL_BODY_INCLUDE;
	}
	
	protected int doEndTag(Writer out) throws Exception {
		writeUpdateRegionEnd();
		return super.doEndTag(out);
	}

	protected void writeUpdateRegionStart() throws JspException {
		if (useUpdateRegions) {
			updateRegionTag = new UpdateRegionHtmlTag();
			registerSubtag(updateRegionTag);
			updateRegionTag.setGlobalId(NameUtil.getFullName(fullId, TAB_CONTAINER_UPDATE_REGION_NAME));
			executeStartSubtag(updateRegionTag);
		}
	}

	protected void writeUpdateRegionEnd() throws JspException {
		if (useUpdateRegions) {
			executeEndTagAndUnregister(updateRegionTag);
		}
		updateRegionTag = null;	
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
	        
	        UiUpdateEvent event;
	        if (useUpdateRegions) {
	        	List updateRegionNames = new ArrayList(1);
	        	updateRegionNames.add(NameUtil.getFullName(fullId, TAB_CONTAINER_UPDATE_REGION_NAME));
	        	event = new UiUpdateEvent(TabContainerWidget.TAB_SELECT_EVENT_ID, fullId, tab.getScope().getId().toString(), updateRegionNames);
	        } else {
	        	event = new UiUpdateEvent(TabContainerWidget.TAB_SELECT_EVENT_ID, fullId, tab.getScope().getId().toString()); 
	        }
	        JspUtil.writeEventAttributes(out, event);

	        JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");

	        JspUtil.writeCloseStartTag_SS(out);
	        JspUtil.writeEscaped(out, tab.getLabel());
	        JspUtil.writeEndTag_SS(out, "a");
	      }
	    JspUtil.writeEndTag(out, "div");
	}

	protected String getTabStyleClass(TabWidget tabwidget) {
		return 
		tabwidget.isSelected() ?
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

	/**
	 *  @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Boolean specifying whether the tab selection events should invoke partial render or full render. Default is false (full render)."
	 */
	public void setUseUpdateRegions(String useUpdateRegions) throws JspException {
		this.useUpdateRegions = ((Boolean) evaluate("useupdateRegions", useUpdateRegions, Boolean.class)).booleanValue();
	}
}
