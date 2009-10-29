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

import org.araneaframework.uilib.util.ConfigurationUtil;

import org.araneaframework.core.Assert;

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

  public static final int DEFAULT_MIN_COMPLETEION_LENGTH = 1;

  protected long minCompletionLength = DEFAULT_MIN_COMPLETEION_LENGTH;

  protected DataProvider dataProvider;

  protected ResponseBuilder responseBuilder;

  /**
   * Constructs a new <code>AutoCompleteTextControl</code> with default minimum completion length (1) and default input
   * text type.
   * 
   * @see #setDataProvider(DataProvider)
   */
  public AutoCompleteTextControl() {}

  /**
   * Constructs a new <code>AutoCompleteTextControl</code> with given minimum completion length and with default input
   * text type.
   * 
   * @param minCompletionLength number of chars that must be input before suggestions are provided.
   * @see #setDataProvider(DataProvider)
   */
  public AutoCompleteTextControl(long minCompletionLength) {
    this.minCompletionLength = minCompletionLength;
  }

  /**
   * Constructs a new <code>AutoCompleteTextControl</code> with default minimum completion length (1) and with given
   * input text type.
   * 
   * @param textType The given text type is used for validating input string.
   * @see #setDataProvider(DataProvider)
   */
  public AutoCompleteTextControl(TextType textType) {
    this(textType, DEFAULT_MIN_COMPLETEION_LENGTH);
  }

  /**
   * Constructs a new <code>AutoCompleteTextControl</code> with given input text type and with given minimum completion
   * length (1).
   * 
   * @param textType The given text type is used for validating input string.
   * @param minCompletionLength Number of chars that must be inserted before suggestions are provided.
   * @see #setDataProvider(DataProvider)
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

  /**
   * Sets the data provider that is used to retrieve the data depending on the input text. Note that data provider is
   * needed once a request comes in. Therefore the parameter must not be <code>null</code>, and should be set after
   * creating this control!
   * 
   * @param dataProvider The data provider that should search and provide suggestions to show depending on the input.
   *          Must not be <code>null</code>!
   */
  public void setDataProvider(DataProvider dataProvider) {
    Assert.notNullParam(this, dataProvider, "dataProvider");
    this.dataProvider = dataProvider;
  }

  /**
   * Sets the builder for the suggestions fetch responses. The builder should compose HTML presentation of given data
   * that will be shown to the user. If there are no results, and empty string or null should be returned.
   * <p>
   * Note that if no response builders are set, the default builder will be used. However, the parameter to this
   * function must not be <code>null</code>!
   * 
   * @param responseBuilder The builder to render following suggestions responses. Must not be <code>null</code>!
   * @since 1.0.4
   */
  public void setResponseBuilder(ResponseBuilder responseBuilder) {
    Assert.notNullParam(this, dataProvider, "responseBuilder");
    this.responseBuilder = responseBuilder;
  }

  /**
   * Provides the response builder that will be used for response suggestions text composing. This method will return
   * either the response builder provided through {@link #setResponseBuilder(ResponseBuilder)}, or the one specified in
   * {@link ConfigurationContext#AUTO_COMPLETE_RESPONSE_BUILDER}, or if neither provided anything, the default
   * implementation.
   * 
   * @return <code>ResponseBuilder</code> that will be used to build response with suggestions.
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
      result = ConfigurationUtil.getResponseBuilder(confCtx);
    }
    return result == null ? new DefaultResponseBuilder() : result;
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

    private long minCompletionLength;

    public ViewModel() {
      this.minCompletionLength = AutoCompleteTextControl.this.minCompletionLength;
    }

    public long getMinCompletionLength() {
      return this.minCompletionLength;
    }
  }
}
