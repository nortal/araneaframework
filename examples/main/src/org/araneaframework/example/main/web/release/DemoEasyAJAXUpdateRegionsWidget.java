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

package org.araneaframework.example.main.web.release;

import org.araneaframework.example.main.web.demo.advanced.DemoComplexForm;

import org.araneaframework.example.main.TemplateBaseWidget;

/**
 * @author Taimo Peelo
 */
public class DemoEasyAJAXUpdateRegionsWidget extends TemplateBaseWidget {

  @Override
  protected void init() throws Exception {
    setViewSelector("release/easyAjax/easyAjaxUpdateRegions");
    addWidget("1", new EasyAjaxDemoWidget("release/easyAjax/plainForm", "easyajax.form.plain"));
    addWidget("2", new EasyAjaxDemoWidget("release/easyAjax/ajaxForm", "easyajax.form.ajax"));
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
