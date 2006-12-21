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

package org.araneaframework.framework;

import java.io.Serializable;
import org.araneaframework.Widget;

/**
 * TransactionContext filters routing of duplicate requests. The detection of
 * duplicate requests is achieved through defining a transaction id
 * <code>by getTransactionId()</code> and checking its consistency with
 * <code>isConsistent()</code>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface TransactionContext extends Serializable {
  /**
   * The key in the request, under which is the transaction id.
   */
  public static final String TRANSACTION_ID_KEY = "transactionId";
  
  /**
   * The key in the request that indicates situation where transaction id
   * was overrriden on purpose. 
   */
  public static final String OVERRIDE_KEY = "override";

  /**
   * The key in the request that indicates that {@link Widget.Interface#process()}
   * should be called even when transaction id is not consistent.
   * 
   * @since 1.0.4
   */
  public static final String ALWAYS_PROCESS_KEY = "allowInconsistentProcessing";

  /**
   * Returns true if current request is consistent (not a rerecurring one).
   */
  public boolean isConsistent();
  
  /**
   * Returns the transaction id of the current request.
   */
  public Object getTransactionId();
}
