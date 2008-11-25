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

package org.araneaframework.framework.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.SystemFormContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.framework.util.TransactionHelper;

/**
 * Filters <code>update(InputData)</code>,
 * <code>event(Path, InputData)</code> and <code>render(OutputData)</code>
 * based on the transaction ID. If the transaction ID is consistent, the
 * mentionend actions get called on the child service, otherwise they do not.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardTransactionFilterWidget extends BaseFilterWidget
  implements TransactionContext {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(
      StandardTransactionFilterWidget.class);

  private TransactionHelper transHelper;

  private boolean consistent = true;

  public boolean isConsistent() {
    return this.consistent;
  }

  public Object getTransactionId() {
    return transHelper.getCurrentTransactionId();
  }

  protected void init() throws Exception {
    transHelper = new TransactionHelper();
    super.init();
  }

  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(super.getChildWidgetEnvironment(),
        TransactionContext.class, this);
  }

  protected void destroy() throws Exception {
    super.destroy();
  }

  // Template
  protected void update(InputData input) throws Exception {
    consistent = isConsistent(input);
    if (isConsistent()) {
      childWidget._getWidget().update(input);
    } else {
      log.debug("Transaction id '" + getTransactionId(input)
          + "' not consistent for routing update().");
    }
  }

  protected void event(Path path, InputData input) throws Exception {
    if (isConsistent()) {
      childWidget._getWidget().event(path, input);
    } else {
      log.debug("Transaction id '" + getTransactionId(input)
          + "' not consistent for routing event().");
    }
  }

  /**
   * Generates a new transaction ID and pushes it as an attribute to the output.
   * The children can access it via
   * {@link TransactionContext#TRANSACTION_ID_KEY} from their OutputData.
   */
  protected void render(OutputData output) throws Exception {
    // CONFIRM: when transactionid was overriden in request, new transaction id
    // should not be generated
    if (transHelper.getCurrentTransactionId() == null
        || !transHelper.isOverride(getTransactionId(getInputData()))) {
      transHelper.resetTransactionId();
    }

    SystemFormContext systemFormContext = getEnvironment().requireEntry(SystemFormContext.class);
    systemFormContext.addField(TRANSACTION_ID_KEY, getTransactionId().toString());

    log.debug("New transaction id '" + getTransactionId() + "'.");

    childWidget._getWidget().render(output);
  }

  /**
   * Returns <code>true</code>, if the transaction ID is consistent. Current
   * implementation uses an instance of {@link TransactionHelper} for
   * determining the consistency. Can be overridden.
   * 
   * @param input the request data.
   * @return <code>true</code>, if current request is consistent.
   */
  protected boolean isConsistent(InputData input) throws Exception {
    return transHelper.isConsistent(getTransactionId(input));
  }

  /**
   * Extracts the transaction ID from the input's global data with the key
   * {@link TransactionContext#TRANSACTION_ID_KEY}.
   * 
   * @param input the request data.
   * @return the transaction ID from the request.
   */
  protected Object getTransactionId(InputData input) throws Exception {
    return input.getGlobalData().get(TransactionContext.TRANSACTION_ID_KEY);
  }

  public Long getNextTransactionId() {
    return transHelper.getNextTransactionId();
  }
}
