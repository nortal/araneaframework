package org.araneaframework.integration.jsf.components;

import java.io.IOException;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;

public class AraneaJsfHtmlForm extends HtmlForm {
	public String getRendererType() {
		return "AraneaFormRenderer";
	}

	public void encodeBegin(FacesContext context) throws IOException {
	}

	public void encodeChildren(FacesContext context) throws IOException {

	}

	public void encodeEnd(FacesContext context) throws IOException {
	}

	public void decode(FacesContext context) {

	}
	
	

}
