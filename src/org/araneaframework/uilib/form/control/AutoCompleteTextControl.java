package org.araneaframework.uilib.form.control;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ActionListener;
import org.araneaframework.jsp.util.StringUtil;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.uilib.support.TextType;

/**
 * TextControl with Ajax autocompletion support
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * 
 */
public class AutoCompleteTextControl extends TextControl {
  private static final Logger log = Logger.getLogger(AutoCompleteTextControl.class);
  protected DataProvider dataProvider;

  public AutoCompleteTextControl() throws Exception {
    super();
  }

  public AutoCompleteTextControl(TextType textType) throws Exception {
    super(textType);
  }

  protected void init() throws Exception {
    super.init();
    //XXX: make constant
    addActionListener("autocomplete", new AutoCompleteActionListener());
  }

  public void setDataProvider(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  public interface DataProvider {
    public List getSuggestions(String input);
  }

  private class AutoCompleteActionListener implements ActionListener {
    public void processAction(Object actionId, InputData input,
        OutputData output) throws Exception {
      String val = innerData == null ? null : ((String[]) innerData)[0];
      List suggestions = dataProvider.getSuggestions((String) val);

      //XXX: outputting some hardcoded HTML from here is not good
      StringBuffer xml = new StringBuffer();
      xml.append("<ul>");
      for (int i = 0; i < suggestions.size(); i++) {
        xml.append("<li>");
        xml.append(StringUtil.escapeHtmlEntities((String) suggestions.get(i)));
        xml.append("</li>");
      }
      xml.append("</ul>");

      log.debug("Writing output: " + xml.toString());
      HttpServletResponse response = ((ServletOutputData) output).getResponse();
      response.setContentType("text/xml");
      response.getWriter().write(xml.toString());
    }
  }
}
