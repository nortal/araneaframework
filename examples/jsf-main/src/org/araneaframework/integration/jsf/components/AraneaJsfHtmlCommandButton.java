package org.araneaframework.integration.jsf.components;

import java.io.IOException;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

public class AraneaJsfHtmlCommandButton extends HtmlCommandButton {

	public void encodeBegin(FacesContext context) throws IOException {
		String type = getType();
		if (type.toLowerCase().equals("submit")) {
			setType("button");

			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			String systemFormId = (String)request.getAttribute("systemFormId");
			
			String widgetId =  (String)request.getAttribute("widgetId");
			
			// get surrounding form, if any, figure out its action
			context.getViewRoot().getChildren();
			
			// now we do not know in which widget we are running but this 
			// information could be put in and determined from FacesContext
			UiUpdateEvent event = new UiUpdateEvent("submit", widgetId, null);
			
			setOnclick(JspWidgetCallUtil.getSubmitScriptForEvent(systemFormId, event));
		}

		super.encodeBegin(context);
	}

	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
	}

	public void encodeEnd(FacesContext context) throws IOException {

		super.encodeEnd(context);
	}

}
