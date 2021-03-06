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

package org.araneaframework.example.main.web.demo.simple;

import org.araneaframework.InputData;
import org.araneaframework.Message;
import org.araneaframework.OutputData;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.message.LoginAndMenuSelectMessage;
import org.araneaframework.example.main.message.PopupMessageFactory;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.MountContext;
import org.araneaframework.http.support.PopupWindowProperties;
import org.araneaframework.uilib.core.PopupFlowWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class SamplePopupWidget extends TemplateBaseWidget {

  private int count = 1;

  public SamplePopupWidget() {}

  protected SamplePopupWidget(int count) {
    this.count = count;
  }

  @Override
  protected void init() throws Exception {
    putViewData("title", "#" + this.count + ". Popup Example");
    setViewSelector("demo/simple/samplePopup");
  }

  public void handleEventCreateThread() throws Exception {
    getMessageCtx().showInfoMessage("common.popupBlockMsg2");
    getPopupCtx().open(new LoginAndMenuSelectMessage("Demos.Simple.Simple_Form"), new PopupWindowProperties(), null);
  }

  public void handleEventOpenUrl() throws Exception {
    getMessageCtx().showInfoMessage("common.popupBlockMsg2");
    getPopupCtx().open("http://www.slashdot.org", new PopupWindowProperties());
  }

  public void handleEventOpenMountedPopup() throws Exception {
    String url = getMountCtx().mount(getInputData(), "my/very/own/mounted/path", new MountContext.MessageFactory() {

      public Message buildMessage(String url, String suffix, InputData input, OutputData output) {
        return new LoginAndMenuSelectMessage("Demos.Simple.Simple_Form");
      }
    });
    getPopupCtx().openMounted(url, new PopupWindowProperties());
  }

  public void handleEventOpenNewCustomFlow() throws Exception {
    getMessageCtx().showInfoMessage("common.popupBlockMsg2");

    PopupWindowProperties p = new PopupWindowProperties();
    p.setHeight("600");
    p.setWidth("1000");
    p.setScrollbars("yes");
    PopupFlowWidget pfw = new PopupFlowWidget(new NameWidget(), p, new PopupMessageFactory());
    getFlowCtx().start(pfw, new SampleHandler());
  }

  public void handleEventOpenImmediatelyReturningCustomFlow() throws Exception {
    PopupWindowProperties p = new PopupWindowProperties();
    p.setHeight("600");
    p.setWidth("1000");
    p.setScrollbars("yes");
    PopupFlowWidget pfw = new PopupFlowWidget(new NameWidget(true), p, new PopupMessageFactory());
    getFlowCtx().start(pfw, new SampleHandler());
  }

  public void handleEventEndFlow() {
    getFlowCtx().finish("Funky end for SamplePopupWidget!");
  }

  class SampleHandler implements FlowContext.Handler<String> {

    public void onCancel() throws Exception {}

    public void onFinish(String returnValue) throws Exception {
      getFlowCtx().replace(new InvisibleElementFormWidget(returnValue), null);
    }
  }
}
