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

package org.araneaframework.example.common.tags.uilib.list;

import java.io.Writer;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.tag.BaseTag;
import org.araneaframework.jsp.tag.form.BaseSystemFormHtmlTag;
import org.araneaframework.jsp.tag.uilib.list.ListTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.contrib.OrderInfo;
import org.araneaframework.uilib.list.contrib.OrderInfoField;
import org.araneaframework.uilib.list.contrib.structure.ListColumn;
import org.araneaframework.uilib.list.contrib.structure.ListStructure;


/**
 * List widget title row tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "listTitleRow"
 *   body-content = "JSP"
 */
public class SampleListTitleRowTag extends BaseTag {
	public final static String ORDER_EVENT_ID = "order";
	
	//
	// Implementation
	//
	
	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		// Get system form id
		String systemFormId = (String)requireContextEntry(BaseSystemFormHtmlTag.ID_KEY);
		
		// Get list data
		String listId = (String)requireContextEntry(ListTag.LIST_FULL_ID_KEY);    
		ListWidget.ViewModel viewModel = (ListWidget.ViewModel)requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY);
		
		// Get order data
		ListStructure.ViewModel listStructureViewModel = viewModel.getListStructure();
		OrderInfo.ViewModel orderInfoViewModel = viewModel.getOrderInfo();
		
		JspUtil.writeStartTag(out, "b");
		
		// Write
		JspUtil.writeOpenStartTag(out, "tr");
		JspUtil.writeCloseStartTag(out);
		
		for(Iterator i = listStructureViewModel.getColumnList().iterator(); i.hasNext();) {
			ListColumn.ViewModel columnViewModel = (ListColumn.ViewModel)i.next();
			
			// Write cell
			JspUtil.writeOpenStartTag(out, "td");
			JspUtil.writeCloseStartTag(out);
			
			// Write link if needed
			if (listStructureViewModel.isColumnOrdered(columnViewModel.getId())) {
				// Draw column ordering if needed        
				for(Iterator j = orderInfoViewModel.getFields().iterator(); j.hasNext();) {
					OrderInfoField.ViewModel orderInfoFieldViewModel = (OrderInfoField.ViewModel)j.next();
					
					if (orderInfoFieldViewModel.getId().equals(columnViewModel.getId())) {
						StringBuffer url = ((HttpServletRequest)pageContext.getRequest()).getRequestURL();
						// Found
						if (orderInfoFieldViewModel.isAscending()) {
							JspUtil.writeOpenStartTag(out, "img");
							JspUtil.writeAttribute(out, "src", 
									url.append(FileImportUtil.getImportString("gfx/ico_sortup.gif")));
							JspUtil.writeCloseStartEndTag(out);
						}
						else {
							JspUtil.writeOpenStartTag(out, "img");
							JspUtil.writeAttribute(out, "src", 
									url.append(FileImportUtil.getImportString("gfx/ico_sortdown.gif")));
							JspUtil.writeCloseStartEndTag(out);
						}
						out.write("&nbsp;");
						break;
					}
				}
				
				// Write link        
				JspUtil.writeOpenStartTag(out, "a");
				JspUtil.writeAttribute(out, "href", "javascript:");        
				JspWidgetCallUtil.writeEventAttributeForEvent(
						pageContext,
						out, 
						"onclick", 
						systemFormId, 
						listId, 
						ORDER_EVENT_ID, 
						columnViewModel.getId(),
						null);
				JspUtil.writeCloseStartTag_SS(out);
			}
			if (columnViewModel.getLabel() != null)
				JspUtil.writeEscaped(out, JspUtil.getResourceString(pageContext, columnViewModel.getLabel()));
			
			// Write link if needed
			if (listStructureViewModel.isColumnOrdered(columnViewModel.getId()))
				JspUtil.writeEndTag(out, "a");
			
			// Write cell
			JspUtil.writeEndTag(out, "td");
		} 
		JspUtil.writeEndTag(out, "tr");     
		JspUtil.writeEndTag(out, "b");
		
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
}
