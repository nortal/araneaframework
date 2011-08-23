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

package org.araneaframework;

/**
 * Output data contract for Aranea components. Components use it to return or store the response. Every output data is
 * associated with the related {@link InputData}, and <i>vice-versa</i>.
 * 
 * @see InputData
 * @see org.araneaframework.http.HttpOutputData
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public interface OutputData extends Extendable, Narrowable {

  /**
   * The key that can be used to store or retrieve <tt>OutputData</tt> (as an example, a request scope attribute).
   */
  String OUTPUT_DATA_KEY = "org.araneaframework.OutputData";

  /**
   * Provides the associated input data.
   * 
   * @return Associated input data, which is never <code>null</code>.
   */
  InputData getInputData();
}
