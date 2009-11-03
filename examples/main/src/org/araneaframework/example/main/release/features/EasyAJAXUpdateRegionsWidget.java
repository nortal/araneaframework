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

package org.araneaframework.example.main.release.features;

import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.demo.DemoComplexForm;

/**
 * @author Taimo Peelo
 */
public class EasyAJAXUpdateRegionsWidget extends TemplateBaseWidget {

  @Override
  protected void init() throws Exception {
    addWidget("1", new EasyAjaxDemoWidget("release/features/easyAjax/HttpRequest", "Complex_Form_HTTP"));
    addWidget("2", new EasyAjaxDemoWidget("release/features/easyAjax/XMLHttpRequest", "Complex_Form"));
    setViewSelector("release/features/easyAjax/easyAjaxUpdateRegions");
  }

  private static class EasyAjaxDemoWidget extends DemoComplexForm {

    private String customViewSelector;

    private String label;

    public EasyAjaxDemoWidget(String customViewSelector, String label) {
      this.customViewSelector = customViewSelector;
      this.label = label;
    }

    @Override
    protected void init() throws Exception {
      super.init();
      setViewSelector(this.customViewSelector);
      putViewData("formLabel", this.label);
    }
  }
}
