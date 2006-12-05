package org.araneaframework.integration.jsf.components;

import java.io.IOException;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;

public class AraneaJsfHtmlForm extends HtmlForm {
	public String getRendererType() {
		return "AraneaFormRenderer";
	}
	
	public void setId(String id) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		String widgetId = (String)request.getAttribute("widgetId");
		widgetId = widgetId.replaceAll("\\.", "_");

		super.setId(widgetId+"_" + id);
	}

	public String getClientId(FacesContext context) {
		return super.getClientId(context);
	}
	
	public String getContainerClientId(FacesContext context) {
		return super.getContainerClientId(context);
	}

	public void encodeBegin(FacesContext context) throws IOException {
		super.encodeBegin(context);
	}
//
//	public void encodeChildren(FacesContext context) throws IOException {
//
//	}
//
//	public void encodeEnd(FacesContext context) throws IOException {
//	}

//	public void decode(FacesContext context) {
//
//	}

	protected Renderer getRenderer(FacesContext context) {
		return super.getRenderer(context);
	}
	
	

}
