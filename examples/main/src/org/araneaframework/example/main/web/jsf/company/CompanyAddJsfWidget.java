package org.araneaframework.example.main.web.jsf.company;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.integration.jsf.JsfWidget;


public class CompanyAddJsfWidget extends TemplateBaseWidget {
	protected void init() throws Exception {
		setViewSelector("jsf/generic");
		addWidget("jsfCompanyAdd", new CompanyJsfWidget("/jsf/company/companyAdd.jsp") {

		});
	}
	
	public String getJsfWidget() {
		return "jsfCompanyAdd";
	}
	
	public class CompanyJsfWidget extends JsfWidget {
		private CompanyMO company = new CompanyMO();
		
		public CompanyJsfWidget(String s) {
			super(s);
			company.setName("Default");
		}
		
		public CompanyMO getCompany() {
			return company;
		}

		public void handleEventEndFlow(String param) {
			super.handleEventEndFlow(param);
			getFlowCtx().finish(company);
		}
	}
}
