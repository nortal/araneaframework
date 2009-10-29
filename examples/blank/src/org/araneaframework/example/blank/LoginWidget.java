/*
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.example.blank;

import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * This is login widget. After receiving "successful" login event, it replaces itself on the call stack with real
 * application root widget.
 * 
 * @author Rein Raudjärv <reinra@ut.ee>
 */
public class LoginWidget extends BaseUIWidget {

  /*
   * A Widget that we will create and attach to this widget.
   */
  private FormWidget form;

  protected void init() throws Exception {
    setViewSelector("login");
    setGlobalEventListener(new ProxyEventListener(this));

    this.form = new FormWidget();
    this.form.addElement("username", "#User", new TextControl(), new StringData(), true);
    this.form.addElement("password", "#Password", new TextControl(), new StringData(), true);
    addWidget("loginForm", this.form);
  }

  public void handleEventLogin() throws Exception {
    if (this.form.convertAndValidate()) {
      // validate login, start root flow
    }
  }

  public void handleEventJustLetMeIn() throws Exception {
    getFlowCtx().replace(new RootWidget(), null);
  }
}
