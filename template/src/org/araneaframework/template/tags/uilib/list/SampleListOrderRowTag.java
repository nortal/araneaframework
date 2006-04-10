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
import java.util.Iterator;
import javax.servlet.jsp.PageContext;
import org.apache.log4j.Logger;
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.tag.uilib.list.UiListTag;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.OrderInfoField;
import org.araneaframework.uilib.list.structure.ListColumn;
import org.araneaframework.uilib.list.structure.ListStructure;


/**
 * List widget multi-order row tag.
 * 
 * @jsp.tag
 *   name = "listOrderRow"
 *   body-content = "JSP"
 */
public class SampleListOrderRowTag extends UiBaseTag {
	private static final Logger log = Logger.getLogger(SampleListOrderRowTag.class);
	
	public final static String ORDER_EVENT_ID = "order";
	
	//
	// Implementation
	//
	
	public int before(Writer out) throws Exception {
		super.before(out);
		
		// Get system form id
		log.debug("Get system form id");
		String systemFormId = (String)readAttribute(UiSystemFormTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get list data
		log.debug("Get list data");
		String listId = (String)readAttribute(UiListTag.LIST_FULL_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);    
		ListWidget.ViewModel viewModel = (ListWidget.ViewModel)readAttribute(UiListTag.LIST_VIEW_MODEL_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get order data
		log.debug("Get order data");
		ListStructure.ViewModel listStructureViewModel = viewModel.getListStructure();
		OrderInfo.ViewModel orderInfoViewModel = viewModel.getOrderInfo();
		
		// Write
		log.debug("Write");
		UiUtil.writeStartTag(out, "tr");
		
		log.debug("Write ListColumns");
		for(Iterator i = listStructureViewModel.getColumnList().iterator(); i.hasNext();) {
			ListColumn.ViewModel columnViewModel = (ListColumn.ViewModel)i.next();
			log.debug("Write ListColumn " + columnViewModel.getId());
			
			// Write cell
			UiUtil.writeStartTag(out, "td");
			
			// Write link if needed
			if (listStructureViewModel.isColumnOrdered(columnViewModel.getId())) {
				// Draw column ordering if needed        
				for(Iterator j = orderInfoViewModel.getFields().iterator(); j.hasNext();) {
					OrderInfoField.ViewModel orderInfoFieldViewModel = (OrderInfoField.ViewModel)j.next();
					
					if (orderInfoFieldViewModel.getId().equals(columnViewModel.getId())) {
						// Found
						/*
						if (orderInfoFieldViewModel.isAscending())
							SampleImageTag.writeImage(out, "gfx/dot19.gif", "9", "7");
						else
							SampleImageTag.writeImage(out, "gfx/dot14.gif", "9", "7");*/
						out.write("&nbsp;");
						break;
					}
				}
				
				// Write link        
				UiUtil.writeOpenStartTag(out, "a");
			//	UiUtil.writeAttribute(out, "class", UiJwlfInnerLinkTag.DEFAULT_STYLE + UiJwlfInnerLinkTag.DEFAULT_STYLE);
				UiUtil.writeAttribute(out, "href", "javascript:");        
				UiStdWidgetCallUtil.writeEventAttributeForEvent(
						pageContext,
						out, 
						"onclick", 
						systemFormId, 
						listId, 
						ORDER_EVENT_ID, 
						columnViewModel.getId(),
						null);
				UiUtil.writeCloseStartTag_SS(out);       
			}
			if (columnViewModel.getLabel() != null)
				UiUtil.writeEscaped(out, UiUtil.getResourceString(pageContext, columnViewModel.getLabel()));
			
			// Write link if needed
			if (listStructureViewModel.isColumnOrdered(columnViewModel.getId()))
				UiUtil.writeEndTag(out, "a");  
			
			// Write cell
			UiUtil.writeEndTag(out, "td");
		} 
		
		// Write order button
		UiUtil.writeStartTag(out, "td");
		UiUtil.writeOpenStartTag(out, "a");
	//	UiUtil.writeAttribute(out, "class", UiJwlfInnerLinkTag.DEFAULT_STYLE + UiJwlfInnerLinkTag.DEFAULT_STYLE);
		UiUtil.writeAttribute(out, "href", "javascript:");        
		UiStdWidgetCallUtil.writeEventAttributeForEvent(
				pageContext,
				out, 
				"onclick", 
				systemFormId, 
				listId, 
				ORDER_EVENT_ID, 
				"",
				null);
		UiUtil.writeCloseStartTag_SS(out);
		UiUtil.writeEscaped(out, "SORT");
		UiUtil.writeEndTag(out, "a");  
		UiUtil.writeEndTag(out, "td");
		
		UiUtil.writeEndTag(out, "tr");     
		
		// Continue
		log.debug("Continue");
		return EVAL_BODY_INCLUDE;		
	}
}
