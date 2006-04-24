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
import org.araneaframework.jsp.tag.UiBaseTag;
import org.araneaframework.jsp.tag.form.UiSystemFormTag;
import org.araneaframework.jsp.tag.uilib.list.UiListTag;
import org.araneaframework.jsp.util.UiStdWidgetCallUtil;
import org.araneaframework.jsp.util.UiUtil;
import org.araneaframework.servlet.filter.importer.ImageFileImporter;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.OrderInfoField;
import org.araneaframework.uilib.list.structure.ListColumn;
import org.araneaframework.uilib.list.structure.ListStructure;


/**
 * List widget title row tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "listTitleRow"
 *   body-content = "JSP"
 */
public class SampleListTitleRowTag extends UiBaseTag {
	public final static String ORDER_EVENT_ID = "order";
	
	//
	// Implementation
	//
	
	public int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		// Get system form id
		String systemFormId = (String)requireContextEntry(UiSystemFormTag.ID_KEY_REQUEST);
		
		// Get list data
		String listId = (String)requireContextEntry(UiListTag.LIST_FULL_ID_KEY_REQUEST);    
		ListWidget.ViewModel viewModel = (ListWidget.ViewModel)requireContextEntry(UiListTag.LIST_VIEW_MODEL_KEY_REQUEST);
		
		// Get order data
		ListStructure.ViewModel listStructureViewModel = viewModel.getListStructure();
		OrderInfo.ViewModel orderInfoViewModel = viewModel.getOrderInfo();
		
		UiUtil.writeStartTag(out, "b");
		
		// Write
		UiUtil.writeOpenStartTag(out, "tr");
		UiUtil.writeCloseStartTag(out);
		
		for(Iterator i = listStructureViewModel.getColumnList().iterator(); i.hasNext();) {
			ListColumn.ViewModel columnViewModel = (ListColumn.ViewModel)i.next();
			
			// Write cell
			UiUtil.writeOpenStartTag(out, "td");
			UiUtil.writeCloseStartTag(out);
			
			// Write link if needed
			if (listStructureViewModel.isColumnOrdered(columnViewModel.getId())) {
				// Draw column ordering if needed        
				for(Iterator j = orderInfoViewModel.getFields().iterator(); j.hasNext();) {
					OrderInfoField.ViewModel orderInfoFieldViewModel = (OrderInfoField.ViewModel)j.next();
					
					if (orderInfoFieldViewModel.getId().equals(columnViewModel.getId())) {
						// Found
						if (orderInfoFieldViewModel.isAscending()) {
							UiUtil.writeOpenStartTag(out, "img");
							UiUtil.writeAttribute(out, "src", ImageFileImporter.getImportString("gfx/ico_sortup.gif"));
							UiUtil.writeCloseStartEndTag(out);
						}
						else {
							UiUtil.writeOpenStartTag(out, "img");
							UiUtil.writeAttribute(out, "src", ImageFileImporter.getImportString("gfx/ico_sortdown.gif"));
							UiUtil.writeCloseStartEndTag(out);
						}
						out.write("&nbsp;");
						break;
					}
				}
				
				// Write link        
				UiUtil.writeOpenStartTag(out, "a");
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
		UiUtil.writeEndTag(out, "tr");     
		UiUtil.writeEndTag(out, "b");
		
		// Continue
		return EVAL_BODY_INCLUDE;		
	}
}
