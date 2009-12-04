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

package org.araneaframework.example.common.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.araneaframework.Widget;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.LocalizationContext.LocaleChangeListener;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.uilib.core.BaseMenuWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.list.util.FormUtil;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class TemplateMenuWidget extends BaseMenuWidget implements LocaleChangeListener {

  private FormWidget form;

  private FormElement<DisplayItem, String> langSelect;

  public TemplateMenuWidget(Widget topWidget) throws Exception {
    super(topWidget);
  }

  @Override
  protected void init() throws Exception {
    super.init();

    this.form = new FormWidget();
    this.langSelect = FormUtil.createElement("#", new DefaultSelectControl(), new StringData(), false);
    this.form.addElement("langSelect", this.langSelect);
    addWidget("form", this.form);

    createLangSelect();
    getL10nCtx().addLocaleChangeListener(this);
  }

  public void createLangSelect() throws Exception {
    DefaultSelectControl select = new DefaultSelectControl();
    select.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() throws Exception {
        if (TemplateMenuWidget.this.langSelect.convertAndValidate()) {
          String lang = TemplateMenuWidget.this.langSelect.getValue();
          getL10nCtx().setLocale(new Locale(lang, ""));
        }
      }
    });

    select.addItems(getLocales());
    this.langSelect.setControl(select);
    this.langSelect.setValue(getL10nCtx().getLocale().getLanguage());
  }

  public void onLocaleChange(Locale oldLocale, Locale newLocale) {
    String lang = this.langSelect.getValue();
    ((DefaultSelectControl) this.langSelect.getControl()).clearItems();
    ((DefaultSelectControl) this.langSelect.getControl()).addItems(getLocales());
    this.langSelect.setValue(lang);
  }

  public List<DisplayItem> getLocales() {
    List<DisplayItem> result = new ArrayList<DisplayItem>();
    result.add(new DisplayItem("en", "EnglishLang"));
    result.add(new DisplayItem("et", "EstonianLang"));
    return result;
  }

  protected LocalizationContext getL10nCtx() {
    return EnvironmentUtil.getLocalizationContext(getEnvironment());
  }

  // returns the name of currently running flow class, so that its source could be located and shown to user
  public String getFlowClassName() {
    String result = null;

    try {
      result = this.callStack.getFirst().getWidget().getClass().getName();
    } catch (Exception e) {}

    return result;
  }

  // returns the name of currently running flow's view selector,
  // so that its source could be located and shown to user
  public String getFlowViewSelector() {
    String result = null;
    try {
      result = ((ViewSelectorAware) callStack.getFirst().getWidget()).getViewSelector();
    } catch (Exception e) {}
    return result;
  }
}
