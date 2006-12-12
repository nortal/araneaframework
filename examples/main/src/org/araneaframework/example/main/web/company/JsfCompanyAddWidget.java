/**
 * 
 */
package org.araneaframework.example.main.web.company;

import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.integration.jsf.JsfWidget;

public class JsfCompanyAddWidget extends JsfWidget {
	private CompanyMO company = new CompanyMO();
	
	public JsfCompanyAddWidget() {
		super("/jsf/company/companyAdd.jsp");
	}
	
	public CompanyMO getCompany() {
		return company;
	}

	public void handleEventEndFlow(String param) {
    //Closes Faces context, should be removed later
    closeFacesContext();
    
		getFlowCtx().finish(company);
	}
}