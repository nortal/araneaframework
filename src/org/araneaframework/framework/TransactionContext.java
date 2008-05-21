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

/**
 * <code>TransactionContext</code> filters routing of duplicate requests. The
 * detection of duplicate requests is achieved through defining a transaction ID
 * <code>by getTransactionId()</code> and checking its consistency with
 * <code>isConsistent()</code>.
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public interface TransactionContext extends Serializable {

  /**
   * The key in the request, under which is the transaction ID.
   */
  public static final String TRANSACTION_ID_KEY = "araTransactionId";

  /**
   * The key in the request that indicates situation where transaction ID.
   * was overrriden on purpose. 
   */
  public static final String OVERRIDE_KEY = "override";

  /**
   * Specifies whether the current request is consistent with the expected
   * transaction ID.
   * 
   * @return <code>true</code>, if current request is consistent.
   */
  public boolean isConsistent();

  /**
   * Provides the transaction ID of the current request.
   * 
   * @return the current transaction ID.
   */
  public Object getTransactionId();

  /**
   * Provides the generated transaction ID that will be expected in the next
   * request.
   * 
   * @return the new transaction ID.
   * @since 1.1
   */
  public Long getNextTransactionId();

}
