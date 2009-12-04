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

package org.araneaframework.uilib.form.control;

import java.util.LinkedList;

import org.araneaframework.uilib.util.UilibEnvironmentUtil;
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

  public static final int DEFAULT_MIN_INPUT_LENGTH = 1;

  protected int minCompletionLength = DEFAULT_MIN_INPUT_LENGTH;

  protected DataProvider dataProvider;

  protected ResponseBuilder responseBuilder;

  protected List<String> localData = new LinkedList<String>();

  /**
   * The default constructor for creating autocomplete input. If the local data nor data provider are not specified, the
   * user will not get any suggestions. The default minimum character count {@value #DEFAULT_MIN_INPUT_LENGTH} (before
   * suggestions are shown) will be used.
   */
  public AutoCompleteTextControl() {}

  /**
   * Constructor for creating autocomplete input with data that is sent to the client side when the control is rendered.
   * Therefore, no AJAX requests will be done when local data is provided. If the parameter <code>localData</code> is an
   * empty list or <code>null</code>, the user will not get any suggestions (unless a data provider is not
   * <code>null</code>). The default minimum character count {@value #DEFAULT_MIN_INPUT_LENGTH} (before suggestions are
   * shown) will be used.
   * 
   * @param localData A list of <code>String</code>s that will be suggested to the user.
   * @since 1.2.3
   */
  public AutoCompleteTextControl(List<String> localData) {
    this.localData = localData;
  }

  /**
   * Constructor for creating autocomplete input with the minimum number of characters before the suggestions are shown.
   * If the local data nor data provider are not specified, the user will not get any suggestions.
   * 
   * @param minCompletionLength Number of chars that must be input before suggestions are provided.
   */
  public AutoCompleteTextControl(int minCompletionLength) {
    this.minCompletionLength = minCompletionLength;
  }

  /**
   * Constructor for creating autocomplete input with data that is sent to the client side when the control is rendered,
   * and with the minimum number of characters before the suggestions are shown. No AJAX requests will be done when
   * local data is provided. If the parameter <code>localData</code> is an empty list or <code>null</code>, the user
   * will not get any suggestions (unless a data provider is not <code>null</code>).
   * 
   * @param localData A list of <code>String</code>s that will be suggested to the user.
   * @param minCompletionLength Number of chars that must be typed before suggestions are provided.
   * @since 1.2.3
   */
  public AutoCompleteTextControl(List<String> localData, int minCompletionLength) {
    this(minCompletionLength);
    this.localData = localData;
  }

  /**
   * Constructor for creating autocomplete input with the given input type. The latter is used to validate the input
   * data. The default type is {@link TextType#TEXT}, which is basically not validated. If the local data nor data
   * provider are not specified, the user will not get any suggestions. The default minimum character count
   * {@value #DEFAULT_MIN_INPUT_LENGTH} (before suggestions are shown) will be used.
   * 
   * @param textType The type of the input text ({@link TextType#TEXT}, {@link TextType#EMAIL},
   *          {@link TextType#NUMBER_ONLY}).
   */
  public AutoCompleteTextControl(TextType textType) {
    super(textType);
  }

  /**
   * Constructor for creating autocomplete input with data that is sent to the client side when the control is rendered,
   * and with the given input type. The latter is used to validate the input data. The default type is
   * {@link TextType#TEXT}, which is basically not validated. If the local data nor data provider are not specified, the
   * user will not get any suggestions. The default minimum character count {@value #DEFAULT_MIN_INPUT_LENGTH} (before
   * suggestions are shown) will be used.
   * 
   * @param localData A list of <code>String</code>s that will be suggested to the user.
   * @param textType The type of the input text ({@link TextType#TEXT}, {@link TextType#EMAIL},
   *          {@link TextType#NUMBER_ONLY}).
   * @since 1.2.3
   */
  public AutoCompleteTextControl(List<String> localData, TextType textType) {
    this(textType);
    this.localData = localData;
  }

  /**
   * Constructor for creating autocomplete input with the given input type, and with the minimum number of characters
   * before the suggestions are shown. The second parameter is used to validate the input data. The default type is
   * {@link TextType#TEXT}, which is basically not validated. If the local data nor data provider are not specified, the
   * user will not get any suggestions.
   * 
   * @param textType The type of the input text ({@link TextType#TEXT}, {@link TextType#EMAIL},
   *          {@link TextType#NUMBER_ONLY}).
   * @param minCompletionLength Number of chars that must be input before suggestions are provided.
   */
  public AutoCompleteTextControl(TextType textType, int minCompletionLength) {
    this(textType);
    this.minCompletionLength = minCompletionLength;
  }

  /**
   * Constructor for creating autocomplete input with data that is sent to the client side when the control is rendered,
   * and with the given input type, and with the minimum number of characters before the suggestions are shown. The
   * former is used to validate the input data. The default type is {@link TextType#TEXT}, which is basically not
   * validated. If the local data nor data provider are not specified, the user will not get any suggestions.
   * 
   * @param localData A list of <code>String</code>s that will be suggested to the user.
   * @param textType The type of the input text ({@link TextType#TEXT}, {@link TextType#EMAIL},
   *          {@link TextType#NUMBER_ONLY}).
   * @param minCompletionLength Number of chars that must be input before suggestions are provided.
   * @since 1.2.3
   */
  public AutoCompleteTextControl(List<String> localData, TextType textType, int minCompletionLength) {
    this(textType, minCompletionLength);
    this.localData = localData;
  }

  @Override
  protected void init() throws Exception {
    super.init();
    addActionListener(LISTENER_NAME, new AutoCompleteActionListener());
  }

  /**
   * Sets the data provider for this autocomplete. If the local data is also provided, this data provider won't be used.
   * If the local data nor data provider are not specified, the user will not get any suggestions.
   */
  public void setDataProvider(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  /**
   * Sets the data that is sent to the client side when the control is rendered. If it is provided, the data provider
   * won't be used. If the local data nor data provider are not specified, the user will not get any suggestions.
   * 
   * @param localData A list of <code>String</code>s that are suggested to the user.
   * @since 1.2.3
   */
  public void setLocalData(List<String> localData) {
    this.localData = localData;
  }

  /** @since 1.0.4 */
  public void setResponseBuilder(ResponseBuilder responseBuilder) {
    this.responseBuilder = responseBuilder;
  }

  /**
   * @return {@link ResponseBuilder} that will be used to build response with suggestions.
   * @since 1.0.4
   */
  public ResponseBuilder getResponseBuilder() {
    return resolveResponseBuilder();
  }

  public interface DataProvider extends Serializable {

    public List<String> getSuggestions(String input);
  }

  private class AutoCompleteActionListener implements ActionListener {

    public void processAction(String actionId, InputData input, OutputData output) throws Exception {
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
      ConfigurationContext confCtx = UilibEnvironmentUtil.getConfiguration(getEnvironment());
      if (confCtx != null)
        result = (ResponseBuilder) confCtx.getEntry(ConfigurationContext.AUTO_COMPLETE_RESPONSE_BUILDER);
    }

    if (result == null)
      result = new DefaultResponseBuilder();

    return result;
  }

  /**
   * Autocompletion response builder interface.
   * 
   * @author Taimo Peelo (taimo@araneaframework.org)
   */
  public static interface ResponseBuilder extends Serializable {

    /**
     * Returns response content with <code>suggestions</code> appropriately set.
     * 
     * @param suggestions suggested completions that should be included in response
     * @return appropriate response content
     */
    public String getResponseContent(List<String> suggestions);

    /**
     * Returns response content type.
     * 
     * @return response content type
     */
    public String getResponseContentType();
  }

  /**
   * Default {@link AutoCompleteTextControl.ResponseBuilder} used when {@link AutoCompleteTextControl} does not have its
   * {@link AutoCompleteTextControl.ResponseBuilder} set and {@link ConfigurationContext#AUTO_COMPLETE_RESPONSE_BUILDER}
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

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************
  public class ViewModel extends TextControl.ViewModel {

    private int minCompletionLength;

    private List<String> localData;

    private boolean dataProviderExists;

    public ViewModel() {
      this.minCompletionLength = AutoCompleteTextControl.this.minCompletionLength;
      this.localData = AutoCompleteTextControl.this.localData;
      this.dataProviderExists = AutoCompleteTextControl.this.dataProvider != null;
    }

    public long getMinCompletionLength() {
      return minCompletionLength;
    }

    public List<String> getLocalData() {
      return this.localData;
    }

    public boolean isDataProviderExists() {
      return this.dataProviderExists;
    }
  }
}
