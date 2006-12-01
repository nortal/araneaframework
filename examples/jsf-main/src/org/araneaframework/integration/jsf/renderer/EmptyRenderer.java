package org.araneaframework.integration.jsf.renderer;

import java.io.IOException;
import java.util.Map;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

public class EmptyRenderer extends Renderer {
	public void decode(FacesContext facesContext, UIComponent component) {
	    UIForm htmlForm = (UIForm)component;

        Map paramMap = facesContext.getExternalContext().getRequestParameterMap();
        String submittedValue = (String)paramMap.get(component.getClientId(facesContext));
        
        htmlForm.setSubmitted(submittedValue != null);
	}

	public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        UIForm htmlForm = (UIForm)component;

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = htmlForm.getClientId(facesContext);
        String actionURL = getActionUrl(facesContext);
         
        writer.startElement("input", component);
        writer.writeAttribute("type", "hidden", "type");
        writer.writeAttribute("name",clientId,
                              "clientId");
        writer.writeAttribute("value", clientId, "value");
        writer.endElement("input");        
        writer.write('\n');
        
        facesContext.getApplication().getViewHandler().writeState(facesContext);
	}

	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		super.encodeChildren(context, component);
	}

	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		super.encodeEnd(context, component);
	}
	
    /**
     * @param facesContext
     * @return String A String representing the action URL
     */
    protected String getActionUrl(FacesContext facesContext)
    {
        ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
        String viewId = facesContext.getViewRoot().getViewId();
        return viewHandler.getActionURL(facesContext, viewId);
    }

}
