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
package org.apache.struts.apps.mailreader.actions;

import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.apps.mailreader.Constants;
import org.apache.struts.apps.mailreader.dao.UserDatabase;
import org.apache.struts.util.MessageResources;
import org.araneaframework.Environment;
import org.araneaframework.example.main.web.company.CompanyEditWidget;
import org.araneaframework.example.main.web.demo.DemoDisplayableEditableList;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.integration.struts.AraneaUtil;


/**
 * <p>
 * Confirm required resources are available before displaying initial page.
 * </p><p>
 * If a resource is missing,
 * forward to "failure". Otherwise, forward to "success", where
 * success is usually the "welcome" page.
 * </p><p>
 * Since "required resources" includes the application MessageResources
 * the failure page must not use the standard error or message tags.
 * Instead, it display the Strings stored in an ArrayList stored under
 * the request attribute "ERROR".
 * </p>
 *
 * @version $Rev: 360442 $ $Date$
 */
public final class WelcomeAction extends BaseAction {

    // See superclass for Javadoc
    public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            final HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

        // Setup message array in case there are errors
        ArrayList messages = new ArrayList();

        // Confirm message resources loaded
        MessageResources resources = getResources(request);
        if (resources == null) {
            messages.add(Constants.ERROR_MESSAGES_NOT_LOADED);
        }

        // Confirm database loaded
        UserDatabase userDatabase = doGetUserDatabase();
        if (userDatabase == null) {
            messages.add(Constants.ERROR_DATABASE_NOT_LOADED);
        }

        // If there were errors, forward to our failure page
        if (messages.size() > 0) {
            request.setAttribute(Constants.ERROR_KEY, messages);
            return doFindFailure(mapping);
        }

        if (AraneaUtil.present(request)) {
          AraneaUtil au = AraneaUtil.get(request);
          au.addWidget("test", new DemoDisplayableEditableList());
          au.getFlowCtx().start(new CompanyEditWidget(), null, new CompanyHandler(au.getEnvironment()));
        }
        
        // Forward to our success page
        return doFindSuccess(mapping);
    }
    
    private static class CompanyHandler implements FlowContext.Handler {
      private Environment env;
      
      public CompanyHandler(Environment env) {
        this.env = env;
      }
      public void onFinish(Object returnValue) throws Exception {
        MessageContext msgCtx = (MessageContext) env.getEntry(MessageContext.class);
        msgCtx.showInfoMessage("Added company " + returnValue);
      }
      public void onCancel() throws Exception {
      }
    }

}
