package org.araneaframework.example.main.release.demos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.event.OnChangeEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoAutoCompletionWidget extends BaseUIWidget {
  private static final long serialVersionUID = 1L;
  private FormWidget form;
  
  public void init() throws Exception {
    setViewSelector("demo/demoAutoCompletion");
    form = new FormWidget();
    
    AutoCompleteTextControl actc = new AutoCompleteTextControl();
    actc.setDataProvider(new DemoACDataProvider(new LocalizationContextProvider() {
		public LocalizationContext getL10nCtx() {
			return DemoAutoCompletionWidget.this.getL10nCtx();
		}
    }));
    actc.addOnChangeEventListener(
    	new OnChangeEventListener() {
    		public void onChange() throws Exception {
    			form.convertAndValidate();
    			getMessageCtx().showInfoMessage("Changed, value is " + form.getValueByFullName("acinput") );
    		}
    	}
    );

    form.addElement("acinput", "#Country", actc, new StringData(), false);

    addWidget("testform", form);
  }
  
  public void handleEventTest() throws Exception {
    if (form.convertAndValidate()) {
      getMessageCtx().showInfoMessage("Country submitted: " + form.getValueByFullName("acinput"));
    }
  }
  
  public static interface LocalizationContextProvider extends Serializable {
	  public LocalizationContext getL10nCtx();
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
      allSuggestions.clear();
      language = locCtxProvider.getL10nCtx().getLocale().getLanguage();
      for (Iterator i = Arrays.asList(Locale.getISOCountries()).iterator(); i.hasNext(); ) {
        allSuggestions.add(new Locale(language, (String)i.next()).getDisplayCountry(locCtxProvider.getL10nCtx().getLocale()));
      }
    }
    
    private boolean isLanguageChanged() {
      return !language.equals(locCtxProvider.getL10nCtx().getLocale().getLanguage());
    }

    public List getSuggestions(String input) {
      List results = new ArrayList();
      if (input == null)
        return results;
      
      if (isLanguageChanged())
    	  fetchData();

      for (Iterator i = allSuggestions.iterator(); i.hasNext();) {
        String suggestion = (String)i.next();
        if (
              suggestion.length() >= input.length() && 
              suggestion.regionMatches(true, 0, input, 0, input.length())
           )
          results.add(suggestion);
      }
      return results;
    }
  }
}
