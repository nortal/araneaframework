/*
 * $Id$
 *
 * Copyright 2000-2004 Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.araneaframework.example.main.web.company;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.apps.mailreader.actions.BaseAction;
import org.araneaframework.example.main.business.model.CompanyMO;
import org.araneaframework.integration.struts.AraneaUtil;

/**
 * <p>
 * Validate a user logon.
 * </p>
 * 
 * @version $Rev: 360442 $ $Date$
 */
public final class CompanyAction extends BaseAction {

  /**
   * <p>
   * Use "username" and "password" fields from ActionForm to retrieve a User object from the database. If credentials
   * are not valid, or database has disappeared, post error messages and forward to input.
   * </p>
   * 
   * @param mapping
   *          The ActionMapping used to select this instance
   * @param form
   *          The optional ActionForm bean for this request (if any)
   * @param request
   *          The HTTP request we are processing
   * @param response
   *          The HTTP response we are creating
   * @throws Exception
   *           if the application business logic throws an exception
   */
  public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

      ActionMessages errors = new ActionMessages();
      
        // Retrieve user
        String name = doGet(form, "name");
        
        if (name == null || name.length() < 1)
          errors.add("error.name", new ActionMessage("error.name.required"));
        
        String address = doGet(form, "address");   
        
        if (address == null || address.length() < 1)
          errors.add("error.address", new ActionMessage("error.address.required"));
        
        // Report back any errors, and exit if any
        if (!errors.isEmpty()) {
            this.saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        
        CompanyMO company = new CompanyMO();
        company.setName(name);
        company.setAddress(address);
        
        if (AraneaUtil.present(request))
          AraneaUtil.get(request).getFlowCtx().finish(company);
         
        // Done
        return null;

    }
}
