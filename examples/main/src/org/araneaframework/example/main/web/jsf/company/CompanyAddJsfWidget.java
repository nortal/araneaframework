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
		
		public void handleEventEndFlow(String param) {
			CompanyAddJsfWidget.this.getFlowCtx().finish(company);
		}
		
		public String getAddress() {
			return company.getAddress();
		}

		public void setAddress(String address) {
			company.setAddress(address);
		}

		public String getName() {
			return company.getName();
		}

		public void setName(String name) {
			company.setName(name);
		}
	}
}
