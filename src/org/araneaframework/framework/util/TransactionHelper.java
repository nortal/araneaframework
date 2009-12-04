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
 * Helper class for determining if transaction id is consistent. Transaction
 * id is considered consistent when it equals {@link TransactionContext#OVERRIDE_KEY}
 * or current transaction id. If current transaction id is not yet set,
 * any transaction id is considered consistent.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class TransactionHelper implements Serializable {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private Long currentTransactionId;
  private Long nextTransactionId;
  private Random random = new Random(System.currentTimeMillis());
  
  {
    resetTransactionId();
  }
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  /**
   * Generates a new current transaction id.
   */
  public void resetTransactionId() {
    currentTransactionId = nextTransactionId; 
    nextTransactionId = new Long(random.nextLong());
  }
  
  /**
   * Returns the current transaction id.
   */
  public Object getCurrentTransactionId() {
    return currentTransactionId;  
  }
  
  public Long getNextTransactionId() {
    return nextTransactionId;
  }
  
  /**
   * Returns true if current transaction id is null or transactionId 
   * equals the current transaction id or transactionId has been
   * overriden.
   */
  public boolean isConsistent(Object transactionId) {
    if (currentTransactionId == null)
      return true;

    if (isOverride(transactionId))
      return true;

    return currentTransactionId.toString().equals(transactionId);
  }

  /**
   * Returns true if current transaction id is null or transactionId does not
   * equal the current transaction id. 
   */
  public boolean isOverride(Object transactionId) {
    return TransactionContext.OVERRIDE_KEY.equals(transactionId);
  }
}
