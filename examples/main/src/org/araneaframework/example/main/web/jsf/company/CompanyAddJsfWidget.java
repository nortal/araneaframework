package org.araneaframework.example.main.web.jsf.company;

import org.araneaframework.OutputData;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.http.util.ServletUtil;
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
		private boolean doRender = true;
		private CompanyMO company = new CompanyMO();
		
		public CompanyJsfWidget(String s) {
			super(s);
			company.setName("Default");
		}
		
		public CompanyMO getCompany() {
			return company;
		}

		public void handleEventEndFlow(String param) {
			doRender = false;
			
			ServletUtil.getRequest(getInputData()).setAttribute("widget", this);

			facesContext = initFacesContext();
			getJSFContext().getLifecycle().execute(facesContext);
			
			restoreRequest(getInputData());
			restoreResponse(getOutputData(), response);
			
			destroyFacesContext();
			
			getFlowCtx().finish(company);
		}

		protected void render(OutputData output) throws Exception {
			if (doRender)
				super.render(output);
		}
	}
}
