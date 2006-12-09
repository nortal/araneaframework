package org.araneaframework.integration.jsf.util;

import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import org.araneaframework.OutputData;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.util.ServletUtil;

public class AraneaFacesContextUtil {
	public static OutputData getOutputData(FacesContext facesContext) {
		return ServletUtil.getOutputData((ServletRequest)facesContext.getExternalContext().getRequest());
	}
	
	public static LocalizationContext getLocalizationContext(FacesContext facesContext) {
		return (LocalizationContext) getOutputData(facesContext).getAttribute(LocalizationContext.LOCALIZATION_CONTEXT_KEY);
	}
	
	public static JspContext getJspContext(FacesContext facesContext) {
		return (JspContext) getOutputData(facesContext).getAttribute(JspContext.JSP_CONFIGURATION_KEY);
	}
}
