
package org.araneaframework.example.main.release.demos;

import java.util.LinkedList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoAutoCompletionWidget extends TemplateBaseWidget {

  private static final long serialVersionUID = 1L;

  private FormWidget form;

  public void init() throws Exception {
    setViewSelector("release/demos/demoAutoCompletion");
    this.form = new FormWidget();

    AutoCompleteTextControl actc = new AutoCompleteTextControl(createData(getL10nCtx(), null));
    actc.setDataProvider(new DemoACDataProvider(new LocalizationContextProvider() {

      public LocalizationContext getL10nCtx() {
        return DemoAutoCompletionWidget.this.getL10nCtx();
      }
    }));

    actc.addOnChangeEventListener(new OnChangeEventListener() {

      public void onChange() throws Exception {
        form.convertAndValidate();
        getMessageCtx().showInfoMessage(
            t("release.ac.onchangetext") + " " + form.getValueByFullName("acinput"));
      }
    });

    this.form.addElement("acinput", "common.Country", actc, new StringData(), false);

    addWidget("testform", this.form);
  }

  public void handleEventTest() throws Exception {
    if (this.form.convertAndValidate()) {
      getMessageCtx().showInfoMessage(
          t("release.ac.submitvalid") + " " + this.form.getValueByFullName("acinput"));
    }
  }

  public static interface LocalizationContextProvider extends Serializable {

    public LocalizationContext getL10nCtx();
  }

  public static List createData(LocalizationContext locCtx, String language) {
    List results = new LinkedList();
    if (language == null) {
      language = locCtx.getLocale().getLanguage();
    }

    for (Iterator i = Arrays.asList(Locale.getISOCountries()).iterator(); i.hasNext();) {
      results.add(new Locale(language, (String) i.next()).getDisplayCountry(locCtx.getLocale()));
    }

    return results;
  }

  public static final class DemoACDataProvider implements AutoCompleteTextControl.DataProvider {

    private static final long serialVersionUID = 1L;

    private List allSuggestions = new ArrayList();

    private String language;

    private LocalizationContextProvider locCtxProvider;

    public DemoACDataProvider(LocalizationContextProvider locCtxProvider) {
      this.locCtxProvider = locCtxProvider;
      fetchData();
    }

    private void fetchData() {
      this.language = this.locCtxProvider.getL10nCtx().getLocale().getLanguage();
      this.allSuggestions.clear();
      this.allSuggestions.addAll(createData(this.locCtxProvider.getL10nCtx(), this.language));
    }

    private boolean isLanguageChanged() {
      return !this.language.equals(this.locCtxProvider.getL10nCtx().getLocale().getLanguage());
    }

    public List getSuggestions(String input) {
      List results = new ArrayList();
      if (input == null) {
        return results;
      }

      if (isLanguageChanged()) {
        fetchData();
      }

      for (Iterator i = this.allSuggestions.iterator(); i.hasNext();) {
        String suggestion = (String) i.next();
        if (suggestion.length() >= input.length()
            && suggestion.regionMatches(true, 0, input, 0, input.length())) {
          results.add(suggestion);
        }
      }
      return results;
    }
  }
}
