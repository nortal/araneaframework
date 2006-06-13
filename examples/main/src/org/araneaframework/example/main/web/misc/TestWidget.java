package org.araneaframework.example.main.web.misc;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ActionListener;
import org.araneaframework.jsp.util.StringUtil;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.uilib.core.StandardPresentationWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.AutoCompleteTextControl;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Steven Jentson (steven@webmedia.ee)
 */
public class TestWidget extends StandardPresentationWidget {
  private static Logger log = Logger.getLogger(TestWidget.class.getName());

  FormWidget form;

  FormElement acElement;

  AutoCompleteTextControl actc;

  public void init() throws Exception {
    setViewSelector("misc/test");
    form = new FormWidget();
    actc = new AutoCompleteTextControl();
    actc.setDataProvider(new AutoCompleteTextControl.DataProvider() {
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
        List sl = new ArrayList();
        if (input == null)
          return sl;
        Iterator iter = allSuggestions.iterator();
        while (iter.hasNext()) {
          String suggestion = (String) iter.next();
          if (suggestion.startsWith(input)
              && suggestion.length() > input.length())
            sl.add(suggestion);
        }
        return sl;
      }
    });

    acElement = form.createElement("#Textbox", actc, new StringData(), false);
    form.addElement("autocompletedTextBox1", acElement);
    
    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(new ProxyOnClickEventListener(this, "test"));    
    form.addElement("button", "#Button", button, null, false);
    
    addWidget("testform", form);
  }

  public void handleEventTest() throws Exception {
    if (form.convertAndValidate()) {
      log.debug("\nTEST EVENT\nTEXT=" + acElement.getValue());
    }
  }
}
