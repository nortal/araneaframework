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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.release.model.ExampleData;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * A simple demo showing how to use AutoCompleteControl.
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoInputSuggestWidget extends TemplateBaseWidget {

  private FormWidget form;

  @Override
  public void init() throws Exception {
    setViewSelector("release/demoAutoCompletion");
    addWidget("testform", createForm());
  }

  public FormWidget createForm() {
    this.form = new FormWidget();
    AutoCompleteTextControl actc = new AutoCompleteTextControl();

    actc.setDataProvider(new DemoACDataProvider(new LocalizationContextProvider() {

      public LocalizationContext getL10nCtx() {
        return DemoInputSuggestWidget.this.getL10nCtx();
      }
    }));

    actc.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() {
        DemoInputSuggestWidget.this.form.convertAndValidate();
        getMessageCtx().showInfoMessage("release.ac.onchangetext",
            DemoInputSuggestWidget.this.form.getValueByFullName("acinput"));
      }
    });

    this.form.addElement("acinput", "common.Country", actc, new StringData());
    return this.form;
  }

  public void handleEventTest() throws Exception {
    if (this.form.convertAndValidate()) {
      getMessageCtx().showInfoMessage("release.ac.submitvalid", this.form.getValueByFullName("acinput"));
    }
  }

  public static interface LocalizationContextProvider extends Serializable {

    public LocalizationContext getL10nCtx();
  }

  public static final class DemoACDataProvider implements AutoCompleteTextControl.DataProvider {

    private List<String> allSuggestions = new ArrayList<String>();

    private String language;

    private final LocalizationContextProvider locCtxProvider;

    public DemoACDataProvider(LocalizationContextProvider locCtxProvider) {
      this.locCtxProvider = locCtxProvider;
      fetchData();
    }

    private void fetchData() {
      Locale locale = this.locCtxProvider.getL10nCtx().getLocale();
      this.allSuggestions.clear();
      this.language = ExampleData.getLanguage(locale);
      this.allSuggestions = ExampleData.getCountries(locale);
    }

    private boolean isLanguageChanged() {
      Locale locale = this.locCtxProvider.getL10nCtx().getLocale();
      return !StringUtils.equals(this.language, ExampleData.getLanguage(locale));
    }

    public List<String> getSuggestions(String input) {
      List<String> results = new ArrayList<String>();
      if (input != null) {
        if (isLanguageChanged()) {
          fetchData();
        }

        for (String suggestion : this.allSuggestions) {
          if (StringUtils.startsWithIgnoreCase(suggestion, input)) {
            results.add(suggestion);
          }
        }
      }
      return results;
    }
  }
}
