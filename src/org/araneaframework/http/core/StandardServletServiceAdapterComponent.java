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

package org.araneaframework.http.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Service;
import org.araneaframework.core.BaseComponent;
import org.araneaframework.core.BaseEnvironment;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.ServletServiceAdapterComponent;

/**
 * <p>
 * Creates a StandardServletInputData and StandardServletOutputData from the HttpServletRequest and HttpServletResponse
 * respectively and routes the request to the child services using a null Path.
 * </p>
 * <p>
 * Since <emphasis>1.0.3</emphasis> this adapter makes {@link OutputData} and {@link InputData} accessible from
 * {@link Component}'s {@link Environment}:
 * 
 * <p>
 * <code>
 *   InputData input = (InputData)getEnvironment().getEntry(InputData.class);<br>
 *   OutputData input = (OutputData)getEnvironment().getEntry(OutputData.class);
 * </code>
 * </p>
 * 
 * which allows access to request from {@link BaseComponent}'s initialization callback&mdash;<code>init()</code>. </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardServletServiceAdapterComponent extends BaseComponent implements ServletServiceAdapterComponent {

  private static final ThreadLocal<InputData> localInput = new ThreadLocal<InputData>();

  private static final ThreadLocal<OutputData> localOutput = new ThreadLocal<OutputData>();

  private Service childService;

  private boolean useFullURL = true;

  @Override
  protected void init() throws Exception {
    this.childService._getComponent().init(getScope(), new BaseEnvironment() {

      @SuppressWarnings("unchecked")
      public <T> T getEntry(Class<T> key) {
        if (InputData.class.equals(key)) {
          return (T) localInput.get();
        }
        if (OutputData.class.equals(key)) {
          return (T) localOutput.get();
        }
        return getEnvironment().getEntry(key);
      }
    });
  }

  public void setChildService(Service service) {
    this.childService = service;
  }

  @Override
  protected void destroy() throws Exception {
    this.childService._getComponent().destroy();
  }

  public void service(HttpServletRequest request, HttpServletResponse response) {
    HttpInputData input = new StandardServletInputData(request);
    input.setUseFullURL(this.useFullURL);
    localInput.set(input);
    HttpOutputData output = new StandardServletOutputData(request, response);
    localOutput.set(output);

    try {
      request.setAttribute(InputData.INPUT_DATA_KEY, input);
      request.setAttribute(OutputData.OUTPUT_DATA_KEY, output);

      this.childService._getService().action(null, input, output);
    } finally {
      localInput.set(null);
      localOutput.set(null);
    }
  }

  /**
   * Allows to specify whether the URL returned by {@link HttpInputData#getContainerURL()} is a full URL or not. In the
   * latter case the URL starts with a slash and context path.
   * 
   * @param useFullURL A Boolean indicating whether the path should be absolute or relative to the host.
   * @see HttpInputData#setUseFullURL(boolean)
   * @since 1.2.3
   */
  public void setUseFullURL(boolean useFullURL) {
    this.useFullURL = useFullURL;
  }
}
