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

package org.araneaframework.template.tags.uilib.list;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.tag.uilib.list.UiListTag;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.SequenceHelper;


/**
 * List widget info tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "listInfo"
 *   body-content = "JSP"
 */
public class SampleListInfoTag extends UiBaseTag {
	public final static String DEFAULT_TITLE_STRING_ID = "list.info.title";
	public final static String DEFAULT_RECORD_STRING_ID = "list.info.record";
	public final static String DEFAULT_NO_DATA_STRING_ID = "list.info.noData";
	
	public final static String SHOW_SLICE_EVENT_ID = "showSlice";
	public final static String SHOW_ALL_EVENT_ID = "showAll";
	
	protected String titleStringId;
	protected String recordStringId;
	protected String noDataStringId;

	protected void init() {
		super.init();
		this.titleStringId = DEFAULT_TITLE_STRING_ID;
		this.recordStringId = DEFAULT_RECORD_STRING_ID;
		this.noDataStringId = DEFAULT_NO_DATA_STRING_ID;
	}
	//
	// Attributes
	//
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Layout height." 
	 */
	public void setTitleStringId(String titleStringId) throws JspException {
		this.titleStringId = (String)evaluateNotNull("titleStringId", titleStringId, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Layout height." 
	 */
	public void setNoDataStringId(String noDataStringId) throws JspException {
		this.noDataStringId = (String)evaluateNotNull("noDataStringId", noDataStringId, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Layout height." 
	 */
	public void setRecordStringId(String recordStringId) throws JspException {
		this.recordStringId = (String)evaluateNotNull("showAll", recordStringId, String.class);
	}
	
	//
	// Implementation
	//  
	
	public int before(Writer out) throws Exception {
		super.before(out);
		
		// Get system form id 
		String systemFormId = (String)readAttribute(UiSystemFormTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get list data
		String listId = (String)readAttribute(UiListTag.LIST_FULL_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);    
		ListWidget.ViewModel viewModel = (ListWidget.ViewModel)readAttribute(UiListTag.LIST_VIEW_MODEL_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get sequnce data
		SequenceHelper.ViewModel sequenceViewModel = viewModel.getSequence();
		
		long totalItemCount = sequenceViewModel.getTotalItemCount().longValue();
		boolean allItemsShown = sequenceViewModel.getAllItemsShown().booleanValue();
		
		long pageFirstItem = sequenceViewModel.getPageFirstItem().longValue();
		long pageLastItem = sequenceViewModel.getPageLastItem().longValue();
		
		// Write
		UiUtil.writeOpenStartTag(out, "span");
		//UiUtil.writeAttribute(out, "class", UiStdStyleTag.DEFAULT_STYLE + "list-info");
		UiUtil.writeCloseStartTag(out);
		
		if (totalItemCount > 0) {
			UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, titleStringId));
			out.write("&nbsp;");
			UiUtil.writeStartTag(out, "b");
			UiUtil.writeEscaped(out, new Long(pageFirstItem).toString());
			out.write("-");   
			UiUtil.writeEscaped(out, new Long(pageLastItem).toString());
			UiUtil.writeEndTag(out, "b");
			out.write("&nbsp;[");
			UiUtil.writeOpenStartTag(out, "a");
			UiUtil.writeAttribute(out, "class", "aranea-link-button");
			UiUtil.writeAttribute(out, "href", "javascript:");
			UiStdWidgetCallUtil.writeEventAttributeForEvent(
					pageContext,
					out, 
					"onclick", 
					systemFormId, 
					listId, 
					allItemsShown ? SHOW_SLICE_EVENT_ID : SHOW_ALL_EVENT_ID, 
							null,
							null);

			UiUtil.writeCloseStartTag_SS(out);
			UiUtil.writeEscaped(out, new Long(totalItemCount).toString());
			out.write("&nbsp;");    
			UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, recordStringId));
			UiUtil.writeEndTag_SS(out, "a");
			out.write("]");
		}
		else
			UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, noDataStringId));
		
		UiUtil.writeEndTag(out, "span");
		
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
}
