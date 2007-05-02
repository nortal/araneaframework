/**
 * Copyright 2006 Webmedia Group Ltd.
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
package org.araneaframework.example.main.web.misc;

import org.araneaframework.OutputData;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class AjaxRequestErrorWidget extends TemplateBaseWidget {
  private boolean renderError = false;

  public void init() throws Exception {
    setViewSelector("misc/ajaxError");
    addEventListener("error", new ProxyEventListener(this));
  }

  public void handleEventError() {
    throw new AraneaRuntimeException("Error while while responding to AJAX event.");
  }

  public void handleEventRenderError() {
    renderError = true;
  }
  
  public void handleEventFirst() {
    getMessageCtx().showInfoMessage("At first everything is well.");
  }
  
  public void handleEventSecond() {
    getMessageCtx().showInfoMessage("Still ok.");
  }

  protected void render(OutputData output) throws Exception {
    if (renderError) {
      renderError = false;
      throw new AraneaRuntimeException("Error while while rendering AJAX request.");
    }
    super.render(output);
  }
}
