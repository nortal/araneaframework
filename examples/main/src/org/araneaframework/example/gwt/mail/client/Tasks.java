/*
 * Copyright 2006 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.araneaframework.example.gwt.mail.client;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * TODO(jgw)
 */
public class Tasks extends Composite {

  public Tasks() {
    VerticalPanel panel = new VerticalPanel();
    panel.add(new CheckBox("Get groceries"));
    panel.add(new CheckBox("Walk the dog"));
    panel.add(new CheckBox("Start Web 2.0 company"));
    panel.add(new CheckBox("Write cool app in GWT"));
    panel.add(new CheckBox("Get funding"));
    panel.add(new CheckBox("Take a vacation"));
    initWidget(panel);
    setStyleName("mail-Tasks");
  }

}
