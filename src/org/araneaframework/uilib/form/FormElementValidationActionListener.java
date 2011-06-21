/*
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
 */

package org.araneaframework.uilib.form;

import java.io.Writer;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.UpdateRegionProvider;
import org.araneaframework.http.filter.StandardUpdateRegionFilterWidget;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.JsonObject;

/**
 * The default implementation of an action listener that must validate the given form element.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class FormElementValidationActionListener<C, D> extends StandardActionListener {

  public static final String FORM_VALIDATION_REGION_KEY = "aranea-formvalidation";

  protected static final Log LOG = LogFactory.getLog(FormElementValidationActionListener.class);

  private FormElement<C, D> baseFormElement;

  public FormElementValidationActionListener(FormElement<C, D> baseFormElement) {
    Assert.notNullParam(this, baseFormElement, "baseFormElement");
    this.baseFormElement = baseFormElement;
  }

  @Override
  public void processAction(String actionId, String actionParam, InputData input, OutputData output) throws Exception {
    if (!isValidationEnabled()) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("Validation listener of '" + this.baseFormElement.getScope()
            + "' was invoked although validation not enbled. Skipping response.");
      }
      return;
    }

    // Let's gather data first:

    Writer out = ((HttpOutputData) output).getWriter();

    boolean valid = this.baseFormElement.convertAndValidate();

    String formElementId = this.baseFormElement.getScope().toString();
    String ajaxRequestId = output.getInputData().getGlobalData().get(
        StandardUpdateRegionFilterWidget.AJAX_REQUEST_ID_KEY);

    // Process error messages indirectly attached to validated form element
    String clientRenderText = this.baseFormElement.getFormElementValidationErrorRenderer().getClientRenderText(
        this.baseFormElement);

    // Now let's write the gathered data to output stream:
    writeValidationStatus(out, ajaxRequestId, formElementId, valid, clientRenderText);

    // Let's also try writing out messages region:

    Environment env = this.baseFormElement.getEnvironment();
    UpdateRegionProvider messageRegion = EnvironmentUtil.getMessageContext(env);

    if (messageRegion != null) {
      LocalizationContext locCtx = env.getEntry(LocalizationContext.class);
      // TODO: general mechanism for writing out UpdateRegions from actions
      String messageRegionContent = messageRegion.getRegions(locCtx).get(MessageContext.MESSAGE_REGION_KEY).toString();
      writeRegion(out, MessageContext.MESSAGE_REGION_KEY, messageRegionContent);
    }
  }

  private boolean isValidationEnabled() {
    return this.baseFormElement.isBackgroundValidation();
  }

  /**
   * This method was added in Aranea 2.0 to separate writing to output stream logic from resolving the values to write.
   * Therefore, it should be easier to customize the process. The parameters are filled in with correct values that
   * should be returned to the client side. This method handles writing them in proper way.
   * 
   * @param out The output stream writer to write to.
   * @param ajaxRequestId The current AJAX request ID that also should be returned.
   * @param formElementId The form element ID for which the validation status applies.
   * @param valid A boolean indicating the form element validation status.
   * @param clientRenderText The text to show as provided by {@link FormElementValidationErrorRenderer}.
   * @throws Exception Any exception that may occur during writing.
   * @since 2.0
   */
  protected void writeValidationStatus(Writer out, String ajaxRequestId, String formElementId, boolean valid,
      String clientRenderText) throws Exception {

    JsonObject object = new JsonObject();
    object.setStringProperty("formElementId", formElementId);
    object.setProperty("valid", String.valueOf(valid));

    if (StringUtils.isNotBlank(clientRenderText)) {
      object.setStringProperty("clientRenderText", clientRenderText);
    }

    out.write(StringUtils.defaultString(ajaxRequestId));
    out.write("\n");
    writeRegion(out, FORM_VALIDATION_REGION_KEY, object.toString());
  }

  /**
   * Handles the standard way of writing an update region to output stream.
   *  
   * @param out The output stream writer to write to.
   * @param name The update region name.
   * @param content The update region content.
   * @throws Exception Any exception that may occur during writing.
   */
  protected void writeRegion(Writer out, String name, String content) throws Exception {
    out.write(name);
    out.write("\n");
    out.write(Integer.toString(content.length()));
    out.write("\n");
    out.write(content);
  }
}
