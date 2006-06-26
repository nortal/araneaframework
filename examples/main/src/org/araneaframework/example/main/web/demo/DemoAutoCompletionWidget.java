package org.araneaframework.example.main.web.demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.uilib.core.StandardPresentationWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Steven Jentson (steven@webmedia.ee)
 */
public class DemoAutoCompletionWidget extends StandardPresentationWidget {
	private static final Logger log = Logger.getLogger(DemoAutoCompletionWidget.class);
	
	FormWidget form;
	
	public void init() throws Exception {
		super.init();
		
		setViewSelector("demo/demoAutoCompletion");
		form = new FormWidget();
		
		AutoCompleteTextControl actc = new AutoCompleteTextControl();
		actc.setDataProvider(new DemoACDataProvider());
		
		form.addElement("acinput", "#Textbox", actc, new StringData(), false);

		addWidget("testform", form);
	}
	
	public void handleEventTest() throws Exception {
		if (form.convertAndValidate()) {
			log.debug("\nTEST EVENT\nTEXT=" + form.getValueByFullName("acinput"));
		}
	}
	
	private final class DemoACDataProvider implements AutoCompleteTextControl.DataProvider {
		private List allSuggestions;
		
		{
			allSuggestions = new ArrayList();
			allSuggestions.add("aaaaaaaaa");
			allSuggestions.add("aaaabbbbb");
			allSuggestions.add("aaaaaacccc");
			allSuggestions.add("aaaaaadddd");
			allSuggestions.add("aaaaaarrr");
			allSuggestions.add("aaaaaeee");
			allSuggestions.add("aaaxxxxx");
			allSuggestions.add("aaacccccccc");
			allSuggestions.add("aaaaaccddddddd");
		}
		
		public List getSuggestions(String input) {
			List result = new ArrayList();
			if (input == null)
				return result;
			Iterator iter = allSuggestions.iterator();
			while (iter.hasNext()) {
				String suggestion = (String) iter.next();
				if (suggestion.startsWith(input)
						&& suggestion.length() > input.length())
					result.add(suggestion);
			}
			return result;
		}
	}
	
}
