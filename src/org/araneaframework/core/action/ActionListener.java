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

package org.araneaframework.core.action;

import java.io.Serializable;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;

/**
 * An action listener for a composite service. Services can have several action listeners (each for a different action).
 * <p>
 * Just to remind, action listener depends on input (global) data, see
 * {@link org.araneaframework.core.ApplicationService} for more information about the parameters.
 * 
 * @see org.araneaframework.core.ApplicationService
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.0.4
 */
public interface ActionListener extends Serializable {

  /**
   * Action processing method. Implementation should be able to handle different <code>actionId</code>s.
   * 
   * @param actionId The ID of the incoming action (may be <code>null</code>).
   * @param input The input data.
   * @param output The output data.
   */
  void processAction(String actionId, InputData input, OutputData output);
}
