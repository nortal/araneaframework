package org.araneaframework.integration.jsf.components;

import java.io.IOException;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.jsp.UiUpdateEvent;
import org.araneaframework.jsp.util.JspWidgetCallUtil;

public class AraneaJsfHtmlCommandButton extends HtmlCommandButton {
	protected void prepareEncode(FacesContext context) {

		if (getType().toLowerCase().equals("submit")) {
			setType("button");

			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			String systemFormId = (String)request.getAttribute("systemFormId");
			
			String widgetId = (String)request.getAttribute("widgetId");
			
			// supposed to be submit button, so construct form action
			String formAction = context.getViewRoot().getViewId();
			
			// now we do not know in which widget we are running but this 
			// information could be put in and determined from FacesContext
			UiUpdateEvent event = new UiUpdateEvent("submit", widgetId, formAction);
			
			setOnclick(JspWidgetCallUtil.getSubmitScriptForEvent(systemFormId, event));
		}
	}
	
	protected Renderer getRenderer(FacesContext context) {
		Renderer renderer = super.getRenderer(context);
		return renderer;
	}

	public void encodeBegin(FacesContext context) throws IOException {
		prepareEncode(context);
		super.encodeBegin(context);
	}

	public void encodeAll(FacesContext context) throws IOException {
		prepareEncode(context);
		super.encodeAll(context);
	}
}
