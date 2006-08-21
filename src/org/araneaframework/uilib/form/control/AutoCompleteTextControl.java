package org.araneaframework.uilib.form.control;

import java.io.Serializable;
import java.util.List;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ActionListener;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.jsp.util.JspStringUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.araneaframework.uilib.support.TextType;

/**
 * TextControl with AJAX autocompletion support.
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class AutoCompleteTextControl extends TextControl {
  public static final String LISTENER_NAME = "autocomplete";

  protected long minCompletionLength = 1;
  protected DataProvider dataProvider;

  public AutoCompleteTextControl() {}

  /**
   * @param minCompletionLength number of chars that must be input before suggestions are provided
   */
  public AutoCompleteTextControl(long minCompletionLength) {
    this.minCompletionLength = minCompletionLength;
  }

  public AutoCompleteTextControl(TextType textType) {
    super(textType);
  }

  /**
   * @param minCompletionLength number of chars that must be input before suggestions are provided
   */
  public AutoCompleteTextControl(TextType textType, long minCompletionLength) {
    super(textType);
    this.minCompletionLength = minCompletionLength;
  }

  protected void init() throws Exception {
    super.init();
    addActionListener(LISTENER_NAME, new AutoCompleteActionListener());
  }

  public void setDataProvider(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  public interface DataProvider extends Serializable {
    public List getSuggestions(String input);
  }

  private class AutoCompleteActionListener implements ActionListener {
    public void processAction(Object actionId, InputData input, OutputData output) throws Exception {
      String str = innerData == null ? null : ((String[]) innerData)[0];
      List suggestions = dataProvider.getSuggestions(str);
      
      ConfigurationContext confCtx = 
        (ConfigurationContext) getEnvironment().requireEntry(ConfigurationContext.class);
      
      ResponseBuilder responseBuilder = 
        (ResponseBuilder) confCtx.getEntry(ConfigurationContext.AUTO_COMPLETE_RESPONSE_BUILDER);
      if (responseBuilder == null)
        responseBuilder = new DefaultResponseBuilder();
      
      HttpOutputData httpOutput = (HttpOutputData) output;
      String xml = responseBuilder.getResponseContent(suggestions);

      httpOutput.setContentType(responseBuilder.getResponseContentType());
      httpOutput.getWriter().write(xml);
    }
  }

  /**
   * Autocompletion response builder interface.
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  public interface ResponseBuilder extends Serializable {
    /**
     * Returns response content with <code>suggestions</code> appropriately set. 
     * @param suggestions suggested completions that should be included in response
     * @return appropriate response content
     */
    public String getResponseContent(List suggestions);
    /**
     * Returns response content type. 
     * @return response content type
     */
    public String getResponseContentType();
  }
  
  /**
   * @author Steven Jentson (steven@webmedia.ee)
   */
  public static class DefaultResponseBuilder implements ResponseBuilder {
	public String getResponseContent(List suggestions) {
  	  StringBuffer xml = new StringBuffer();
        xml.append("<ul>");
        for (int i = 0; i < suggestions.size(); i++) {
  		  xml.append("<li>");
  		  //XXX: uilib should not depend on Aranea JSP
  		  xml.append(JspStringUtil.escapeHtmlEntities((String) suggestions.get(i)));
  		  xml.append("</li>");
        }
        xml.append("</ul>");
        return xml.toString();
    }
	
    public String getResponseContentType() {
      return "text/xml";
    }
  }
  
  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************  	
  public class ViewModel extends TextControl.ViewModel {
    private long minCompletionLength;
    
    public ViewModel() {
      this.minCompletionLength = AutoCompleteTextControl.this.minCompletionLength;
    }

    public long getMinCompletionLength() {
      return minCompletionLength;
    }
  }
}