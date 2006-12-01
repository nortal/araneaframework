package org.araneaframework.integration.jsf.components;

import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

public class AraneaJsfHtmlForm extends HtmlForm {
	public String getRendererType() {
		return "AraneaFormRenderer";
	}

//	public void encodeBegin(FacesContext context) throws IOException {
//	}
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
