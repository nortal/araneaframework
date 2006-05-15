/**
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
**/

package org.araneaframework.framework.filter;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.framework.util.TransactionHelper;
import org.araneaframework.servlet.util.ClientStateUtil;

/**
 * Filters <code>update(InputData)</code>,  <code>event(Path, InputData)</code>, 
 * <code>process()</code> and <code>render(OutputData)</code> based on the transaction id. If
 * the transaction id is consistent, the mentionend actions get called on the child service,
 * otherwise they do not.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardTransactionFilterWidget extends BaseFilterWidget implements TransactionContext {
  /**
   * The key in the request, under which is the transaction id.
   */
  public static final String TRANSACTION_ID_KEY = "transactionId";
  
  private static final Logger log = Logger.getLogger(StandardTransactionFilterWidget.class);  
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
    
    Map entries = new HashMap();
    entries.put(TransactionContext.class, this);
    
    childWidget._getComponent().init(new StandardEnvironment(getChildWidgetEnvironment(), entries));
  }
  
  protected void destroy() throws Exception {
    super.destroy();
  }

  // Template   
  protected void update(InputData input) throws Exception {
    consistent = isConsistent(input);
    if (isConsistent()) {
      childWidget._getWidget().update(input); 
    }
    else {
      log.debug("Transaction id '"+getTransactionId()+"' not consistent for routing update().");
    }
  }
  
  protected void event(Path path, InputData input) throws Exception {
    if (isConsistent()) {
      childWidget._getWidget().event(path, input);
    } else {
      log.debug("Transaction id '"+getTransactionId()+"' not consistent for routing event().");
    }
  }
  
  protected void process() throws Exception {
    if (isConsistent()) {
      childWidget._getWidget().process();
    }
    else {
      log.debug("Transaction id '"+getTransactionId()+"' not consistent for routing process().");
    }
  }
  
  /**
   * Generates a new transaction id and pushes it as an attribute to the output. The children
   * can access it via TRANSACTION_ID_KEY from their OutputData.
   */
  protected void render(OutputData output) throws Exception {
    transHelper.resetTransactionId();
    output.pushAttribute(TRANSACTION_ID_KEY, transHelper.getCurrentTransactionId());
    ClientStateUtil.put(TRANSACTION_ID_KEY, transHelper.getCurrentTransactionId()+"", output);
    
	try {
	    log.debug("New transaction id '" + getTransactionId() + "'.");
	    childWidget._getWidget().render(output);
    }
    finally {
      output.popAttribute(TRANSACTION_ID_KEY);
    }
  }

  /**
   * Returns true, if the transaction id is consistent. Current implementation uses an instance
   * of {@link TransactionHelper} for determining the consistency. Can be overridden.
   */
  protected boolean isConsistent(InputData input) throws Exception {  	  	
    return getTransactionId(input) == null || transHelper.isConsistent(getTransactionId(input));
  }
  
  /**
   * Extracts the transaction id from the input's global data with the key TRANSACTION_ID_KEY.
   */
  protected Object getTransactionId(InputData input) throws Exception {
    return input.getGlobalData().get(TRANSACTION_ID_KEY);
  }
}
