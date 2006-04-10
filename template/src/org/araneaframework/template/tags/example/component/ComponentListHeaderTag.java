package org.araneaframework.template.tags.example.component;

import java.io.Writer;
import java.util.Iterator;
import javax.servlet.jsp.PageContext;
import org.araneaframework.jsp.tag.UiPresentationTag;
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
 * @author Oleg Mürk
 * @author Taimo Peelo (taimo@webmedia.ee)
 * @jsp.tag
 *   name = "componentListHeader"
 *   body-content = "empty"
 *   description = "Inside this tag list header should be written out." 
 */

public class ComponentListHeaderTag extends UiPresentationTag {
	public final static String ORDER_EVENT_ID = "order";
	public final static String COMPONENT_LIST_STYLE_CLASS = "data";
	
	protected void init() {
		super.init();
		styleClass = ComponentListTag.COMPONENT_LIST_STYLE_CLASS;
	}
	
	protected int before(Writer out) throws Exception {
		super.before(out);
		
		UiUtil.writeOpenStartTag(out, "tr");
		UiUtil.writeAttribute(out, "class", getStyleClass());
		UiUtil.writeCloseStartTag(out);
		
		writeHeader(out);
		
		return EVAL_BODY_INCLUDE;
	}
	
	protected void writeHeader(Writer out) throws Exception {
		// Get system form id
		String systemFormId = (String)readAttribute(UiSystemFormTag.ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get list data
		String listId = (String)readAttribute(UiListTag.LIST_FULL_ID_KEY_REQUEST, PageContext.REQUEST_SCOPE);    
		ListWidget.ViewModel viewModel = (ListWidget.ViewModel)readAttribute(UiListTag.LIST_VIEW_MODEL_KEY_REQUEST, PageContext.REQUEST_SCOPE);
		
		// Get order data
		ListStructure.ViewModel listStructureViewModel = viewModel.getListStructure();
		OrderInfo.ViewModel orderInfoViewModel = viewModel.getOrderInfo();
		
		for(Iterator i = listStructureViewModel.getColumnList().iterator(); i.hasNext();) {
			ListColumn.ViewModel columnViewModel = (ListColumn.ViewModel)i.next();
			
			// Write cell
			UiUtil.writeOpenStartTag(out, "th");
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
			UiUtil.writeEndTag(out, "th");
		}
		UiUtil.writeEndTag(out, "tr");
	}
}
