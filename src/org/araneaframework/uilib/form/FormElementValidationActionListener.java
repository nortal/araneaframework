/**
 * Copyright 2007 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

package org.araneaframework.uilib.form;

import java.io.Writer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.UpdateRegionProvider;
import org.araneaframework.http.filter.StandardUpdateRegionFilterWidget;
import org.araneaframework.http.util.JsonObject;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class FormElementValidationActionListener extends StandardActionListener {
  public static final String FORM_VALIDATION_REGION_KEY = "aranea-formvalidation";
  protected static final Log log = LogFactory.getLog(FormElementValidationActionListener.class);

  private FormElement baseFormElement;
  public FormElementValidationActionListener(FormElement baseFormElement) {
    Assert.notNullParam(this, baseFormElement, "baseFormElement");
    this.baseFormElement = baseFormElement;
  }

  public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
    if (!isValidationEnabled() && log.isWarnEnabled()) {
      log.warn("Validation listener of '" + this.baseFormElement.getScope() + "' was invoked although validation not enbled. Skipping response.");
      return;
    }

    boolean valid = baseFormElement.convertAndValidate();
    Writer out = ((HttpOutputData) output).getWriter();

    String ajaxRequestId = (String) output.getInputData().getGlobalData().get(StandardUpdateRegionFilterWidget.AJAX_REQUEST_ID_KEY); 
    out.write(String.valueOf(ajaxRequestId) + "\n");

    JsonObject object = new JsonObject();
    object.setStringProperty("formElementId", baseFormElement.getScope().toString());
    object.setProperty("valid", String.valueOf(valid));
    
    // Process error messages indirectly attached to validated formelement
    String clientRenderText = baseFormElement.getFormElementValidationErrorRenderer().getClientRenderText(baseFormElement);
    if (clientRenderText != null && clientRenderText.length() > 0)
	  object.setStringProperty("clientRenderText", clientRenderText);

    writeRegion(out, FormElementValidationActionListener.FORM_VALIDATION_REGION_KEY, object.toString());
    
    MessageContext messageContext = (MessageContext) baseFormElement.getEnvironment().getEntry(MessageContext.class);
    if(messageContext != null) {
      UpdateRegionProvider messageRegion = messageContext;

      // TODO: general mechanism for writing out UpdateRegions from actions
      String messageRegionContent = messageRegion.getRegions().get(MessageContext.MESSAGE_REGION_KEY).toString();
      writeRegion(out, MessageContext.MESSAGE_REGION_KEY, messageRegionContent);
    }
  }
  
  private boolean isValidationEnabled() {
    return baseFormElement.isBackgroundValidation();
  }

protected void writeRegion(Writer out, String name, String content) throws Exception {
	out.write(name);
    out.write("\n");
    out.write(Integer.toString(content.length()));
    out.write("\n");
    out.write(content);
  }
}
