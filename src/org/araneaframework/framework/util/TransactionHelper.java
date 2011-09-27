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

package org.araneaframework.framework.util;

import java.io.Serializable;
import java.util.Random;
import org.araneaframework.framework.TransactionContext;

/**
 * Helper class for determining if transaction ID is consistent. Transaction ID is considered consistent when it equals
 * {@link TransactionContext#OVERRIDE_KEY} or current transaction ID. If current transaction ID is not yet set, any
 * transaction ID is considered consistent.
 * <p>
 * This class can be used to filter out unexpected requests, such as double submits.
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class TransactionHelper implements Serializable {

  private Long currentTransactionId;

  private Long nextTransactionId;

  private final Random random = new Random(System.currentTimeMillis());

  /**
   * Creates a new transaction helper, where previous transaction ID is <code>null</code> and the current transaction ID
   * is initialized with a new random number.
   */
  public TransactionHelper() {
    resetTransactionId();
  }

  /**
   * Generates and stores a new current transaction ID.
   * 
   * @see #getCurrentTransactionId()
   */
  public void resetTransactionId() {
    this.currentTransactionId = this.nextTransactionId;
    this.nextTransactionId = this.random.nextLong();
  }

  /**
   * Returns the current transaction ID.
   * 
   * @return The current transaction ID, which is never <code>null</code>.
   */
  public Long getCurrentTransactionId() {
    return this.currentTransactionId;
  }

  /**
   * Provides the transaction ID, which is expected from the following transaction.
   * 
   * @return The transaction ID for the following request.
   */
  public Long getNextTransactionId() {
    return this.nextTransactionId;
  }

  /**
   * Validates whether the provided transaction ID is consistent with the current transaction ID known to this helper.
   * 
   * @param transactionId The transaction ID value to check.
   * @return A Boolean that is <code>true</code> when the provided transaction ID is consistent with this helper.
   */
  public boolean isConsistent(String transactionId) {
    return this.currentTransactionId == null || isOverride(transactionId)
        || this.currentTransactionId.toString().equals(transactionId);
  }

  /**
   * Checks whether the provided transaction ID value matches the override key value.
   * 
   * @param transactionId The transaction ID value to check.
   * @return A Boolean that is <code>true</code> when the provided transaction ID value corresponds to the override key.
   * @see org.araneaframework.framework.TransactionContext#OVERRIDE_KEY
   */
  public boolean isOverride(Object transactionId) {
    return TransactionContext.OVERRIDE_KEY.equals(transactionId);
  }
}
