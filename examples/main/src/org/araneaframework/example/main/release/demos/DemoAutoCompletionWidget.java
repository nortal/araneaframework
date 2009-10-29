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

package org.araneaframework.example.main.release.demos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.araneaframework.example.main.TemplateBaseWidget;
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
public class DemoAutoCompletionWidget extends TemplateBaseWidget {

  private FormWidget form;

  public void init() throws Exception {
    setViewSelector("release/demos/demoAutoCompletion");
    addWidget("testform", createForm());
  }

  public FormWidget createForm() {
    this.form = new FormWidget();
    AutoCompleteTextControl actc = new AutoCompleteTextControl();
    actc.setDataProvider(new DemoACDataProvider(new LocalizationContextProvider() {

      public LocalizationContext getL10nCtx() {
        return DemoAutoCompletionWidget.this.getL10nCtx();
      }
    }));
    actc.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() throws Exception {
        form.convertAndValidate();
        getMessageCtx().showInfoMessage(t("release.ac.onchangetext") + " " + form.getValueByFullName("acinput"));
      }
    });
    this.form.addElement("acinput", "common.Country", actc, new StringData(), false);
    return this.form;
  }

  public void handleEventTest() throws Exception {
    if (this.form.convertAndValidate()) {
      getMessageCtx().showInfoMessage(t("release.ac.submitvalid") + " " + this.form.getValueByFullName("acinput"));
    }
  }

  public static interface LocalizationContextProvider extends Serializable {

    public LocalizationContext getL10nCtx();
  }

  public static final class DemoACDataProvider implements AutoCompleteTextControl.DataProvider {

    private List<String> allSuggestions = new ArrayList<String>();

    private String language;

    private LocalizationContext localeCtx;

    public DemoACDataProvider(LocalizationContextProvider locCtxProvider) {
      this.localeCtx = locCtxProvider.getL10nCtx();
      fetchData();
    }

    private void fetchData() {
      this.allSuggestions.clear();
      this.language = this.localeCtx.getLocale().getLanguage();
      for (String country : Arrays.asList(Locale.getISOCountries())) {
        this.allSuggestions.add(new Locale(this.language, country).getDisplayCountry(this.localeCtx.getLocale()));
      }
    }

    private boolean isLanguageChanged() {
      return !this.language.equals(this.localeCtx.getLocale().getLanguage());
    }

    public List<String> getSuggestions(String input) {
      List<String> results = new ArrayList<String>();
      if (input != null) {
        if (isLanguageChanged()) {
          fetchData();
        }
        for (String suggestion : this.allSuggestions) {
          if (suggestion.length() >= input.length() && suggestion.regionMatches(true, 0, input, 0, input.length())) {
            results.add(suggestion);
          }
        }
      }
      return results;
    }
  }
}
