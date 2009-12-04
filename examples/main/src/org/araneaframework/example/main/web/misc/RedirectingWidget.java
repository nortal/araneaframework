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

package org.araneaframework.example.main.web.misc;

import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.message.LoginAndMenuSelectMessage;
import org.araneaframework.framework.MountContext;
import org.araneaframework.http.HttpOutputData;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class RedirectingWidget extends TemplateBaseWidget {

  @Override
  public void init() throws Exception {
    setViewSelector("misc/redirect");
  }

  public void handleEventRedirect() throws Exception {
    ((HttpOutputData) getOutputData()).sendRedirect("http://www.araneaframework.org");
  }
  
  public void handleEventMountAndRedirect() throws Exception {
    String url = getMountCtx().mount(getInputData(), "/mount/test",
        new MountContext.MessageFactory() {

          public Message buildMessage(String url, String suffix,
              InputData input, OutputData output) {
            return new LoginAndMenuSelectMessage("Demos.Simple.Simple_Form");
          }
        });

    ((HttpOutputData) getOutputData()).sendRedirect(url);
  }
}
