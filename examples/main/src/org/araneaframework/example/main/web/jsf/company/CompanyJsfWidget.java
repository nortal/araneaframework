/**
 * 
 */
package org.araneaframework.example.main.web.jsf.company;

import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.integration.jsf.JsfWidget;

public class CompanyJsfWidget extends JsfWidget {
	private CompanyMO company = new CompanyMO();
	
	public CompanyJsfWidget() {
		super("/jsf/company/companyAdd.jsp");
		company.setName("Default");
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