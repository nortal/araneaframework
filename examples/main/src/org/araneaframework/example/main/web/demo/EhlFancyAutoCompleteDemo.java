package org.araneaframework.example.main.web.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;
import org.araneaframework.uilib.form.control.EhlFancyAutoCompleteControl;
import org.araneaframework.uilib.form.data.DisplayItemData;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class EhlFancyAutoCompleteDemo extends BaseUIWidget {
  private static final long serialVersionUID = 1L;
  private FormWidget form;
  
  public void init() throws Exception {
    setViewSelector("demo/demoAutoCompletion");
    form = new FormWidget();
    
    EhlFancyAutoCompleteControl actc = new EhlFancyAutoCompleteControl();
    actc.addItems(getCompletions());
    form.addElement("acinput", "#whatever", actc, new DisplayItemData(), false);

    addWidget("testform", form);
  }
  
  public void handleEventTest() throws Exception {
    if (form.convertAndValidate()) {
      DisplayItem item = (DisplayItem) form.getValueByFullName("acinput");
      if (item != null)
    	 getMessageCtx().showInfoMessage(item + " : [" + item.getValue() + ", " + item.getDisplayString() + "]");
    }
  }

  private List getCompletions() {
	 List result = new ArrayList();
	 result.add(new DisplayItem("value1", "string1"));
	 result.add(new DisplayItem("value2", "string2"));
	 result.add(new DisplayItem("value3", "string3"));
	 result.add(new DisplayItem("value4", "string4"));
	 return result;
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
