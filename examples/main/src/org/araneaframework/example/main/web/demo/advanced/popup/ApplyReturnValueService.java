/*
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
 */

package org.araneaframework.example.main.web.demo.advanced.popup;

import org.araneaframework.http.util.EnvironmentUtil;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.BaseService;
import org.araneaframework.framework.ManagedServiceContext;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.http.util.ServletUtil;

/**
 * Sample service that applies the flow return value to opener window widget purely on client-side.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ApplyReturnValueService extends BaseService implements ClientSideReturnService<String> {

  private String value;

  private String widgetId;

  public ApplyReturnValueService(String widgetId) {
    this.widgetId = widgetId;
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);
    String script = "Aranea.Popup.applyReturnValue('" + this.value + "', '" + this.widgetId + "');"
        + "Aranea.Popup.delayedCloseWindow(50);";

    String scriptSrc = FileImportUtil.getImportString("js/aranea/aranea-popups.js", input);
    String responseStr = "<html><head><script type=\"text/javascript\" src=\"" + scriptSrc
        + "\"></script></head><body><script type=\"text/javascript\">" + script + "</script></body></html>";

    byte[] rsp = responseStr.getBytes();

    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    byteOutputStream.write(rsp);

    response.setContentType("text/html");
    response.setContentLength(byteOutputStream.size());

    OutputStream out = response.getOutputStream();
    byteOutputStream.writeTo(out);
    out.flush();

    ManagedServiceContext mngCtx = EnvironmentUtil.requireManagedService(getEnvironment());
    mngCtx.close(mngCtx.getCurrentId());
  }

  public String getResult() {
    return this.value;
  }

  public void setResult(String returnValue) {
    this.value = returnValue;
  }
}
