package org.araneaframework.uilib.form.control;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ActionListener;
import org.araneaframework.http.HttpOutputData;
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
  protected ResponseBuilder responseBuilder;

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

  @Override
  protected void init() throws Exception {
    super.init();
    addActionListener(LISTENER_NAME, new AutoCompleteActionListener());
  }

  public void setDataProvider(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }
  
  /** @since 1.0.4 */
  public void setResponseBuilder(ResponseBuilder responseBuilder) {
    this.responseBuilder = responseBuilder;
  }
  
  /**
   * @return {@link ResponseBuilder} that will be used to build response with suggestions.
   * @since 1.0.4 */
  public ResponseBuilder getResponseBuilder() {
    return resolveResponseBuilder();
  }

  public interface DataProvider extends Serializable {
    public List<String> getSuggestions(String input);
  }

  private class AutoCompleteActionListener implements ActionListener {
    public void processAction(Object actionId, InputData input, OutputData output) throws Exception {
      String str = innerData == null ? null : ((String[]) innerData)[0];
      List<String> suggestions = dataProvider.getSuggestions(str);

      ResponseBuilder responseBuilder = resolveResponseBuilder();

      HttpOutputData httpOutput = (HttpOutputData) output;
      String xml = responseBuilder.getResponseContent(suggestions);

      httpOutput.setContentType(responseBuilder.getResponseContentType());
      httpOutput.getWriter().write(xml);
    }
  }
  
  /** @since 1.0.4 */
  protected ResponseBuilder resolveResponseBuilder() {
    ResponseBuilder result = this.responseBuilder;
    if (result == null) {
      ConfigurationContext confCtx = 
        getEnvironment().getEntry(ConfigurationContext.class);
      if (confCtx != null)
        result = (ResponseBuilder)confCtx.getEntry(ConfigurationContext.AUTO_COMPLETE_RESPONSE_BUILDER);
    }
    
    if (result == null)
      result = new DefaultResponseBuilder();
    
    return result;
  }

  /**
   * Autocompletion response builder interface.
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  public static interface ResponseBuilder extends Serializable {
    /**
     * Returns response content with <code>suggestions</code> appropriately set. 
     * @param suggestions suggested completions that should be included in response
     * @return appropriate response content
     */
    public String getResponseContent(List<String> suggestions);
    /**
     * Returns response content type. 
     * @return response content type
     */
    public String getResponseContentType();
  }
  
  /**
   * Default {@link AutoCompleteTextControl.ResponseBuilder} used when {@link AutoCompleteTextControl} does not have
   * its {@link AutoCompleteTextControl.ResponseBuilder} set and {@link ConfigurationContext#AUTO_COMPLETE_RESPONSE_BUILDER}
   * does not specify application-wide {@link AutoCompleteTextControl.ResponseBuilder}.
   * 
   * @author Steven Jentson (steven@webmedia.ee)
   */
  public static class DefaultResponseBuilder implements ResponseBuilder {
	public String getResponseContent(List<String> suggestions) {
  	  StringBuffer xml = new StringBuffer();
        xml.append("<ul>");
        for (int i = 0; i < suggestions.size(); i++) {
  		  xml.append("<li>");
  		  xml.append(StringEscapeUtils.escapeHtml(suggestions.get(i)));
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
  @Override
  public ViewModel getViewModel() {
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
