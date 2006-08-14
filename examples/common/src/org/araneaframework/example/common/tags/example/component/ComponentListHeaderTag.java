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

package org.araneaframework.example.common.tags.example.component;

import java.io.Writer;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.DefaultEvent;
import org.araneaframework.jsp.Event;
import org.araneaframework.jsp.tag.layout.LayoutRowTag;
import org.araneaframework.jsp.tag.uilib.list.ListTag;
import org.araneaframework.jsp.util.JspUtil;
import org.araneaframework.jsp.util.JspWidgetCallUtil;
import org.araneaframework.uilib.list.ListWidget;
import org.araneaframework.uilib.list.OrderInfo;
import org.araneaframework.uilib.list.OrderInfoField;
import org.araneaframework.uilib.list.structure.ListColumn;
import org.araneaframework.uilib.list.structure.ListStructure;

/**
 * @author Oleg Mürk
 * @author Taimo Peelo (taimo@webmedia.ee)
 * @jsp.tag
 *   name = "componentListHeader"
 *   body-content = "empty"
 *   description = "Inside this tag list header should be written out." 
 */

public class ComponentListHeaderTag extends LayoutRowTag {
  public final static String ORDER_EVENT_ID = "order";
  public final static String COMPONENT_LIST_STYLE_CLASS = "data";
  
  public ComponentListHeaderTag() {
    styleClass = ComponentListTag.COMPONENT_LIST_STYLE_CLASS;
  }
  
  protected int doStartTag(Writer out) throws Exception {
    super.doStartTag(out);
    writeHeader(out);
    return EVAL_BODY_INCLUDE;
  }
  
  protected void writeHeader(Writer out) throws Exception {
    // Get list data
    String listId = (String)requireContextEntry(ListTag.LIST_FULL_ID_KEY);    
    ListWidget.ViewModel viewModel = (ListWidget.ViewModel)requireContextEntry(ListTag.LIST_VIEW_MODEL_KEY);
    
    // Get order data
    ListStructure.ViewModel listStructureViewModel = viewModel.getListStructure();
    OrderInfo.ViewModel orderInfoViewModel = viewModel.getOrderInfo();
    
    for(Iterator i = listStructureViewModel.getColumnList().iterator(); i.hasNext();) {
      ListColumn.ViewModel columnViewModel = (ListColumn.ViewModel)i.next();
      
      // Write cell
      JspUtil.writeOpenStartTag(out, "th");
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
        
        Event orderEvent = new DefaultEvent(ORDER_EVENT_ID, listId, columnViewModel.getId());
      
        JspUtil.writeOpenStartTag(out, "a");
        JspUtil.writeAttribute(out, "class", "aranea-link-button");  
        JspUtil.writeEventAttributes(out, orderEvent);
        JspWidgetCallUtil.writeSubmitScriptForEvent(out, "onclick");

        JspUtil.writeCloseStartTag_SS(out);
      }
      if (columnViewModel.getLabel() != null)
        JspUtil.writeEscaped(out, JspUtil.getResourceString(pageContext, columnViewModel.getLabel()));
      
      // Write link if needed
      if (listStructureViewModel.isColumnOrdered(columnViewModel.getId()))
        JspUtil.writeEndTag(out, "a");
      
      // Write cell
      JspUtil.writeEndTag(out, "th");
    }
    JspUtil.writeEndTag(out, "tr");
  }
}
