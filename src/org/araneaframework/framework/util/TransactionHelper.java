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

package org.araneaframework.framework.util;

import java.io.Serializable;
import java.util.Random;
import org.araneaframework.framework.TransactionContext;

/**
 * Helper class for determining if an id is consistent. An id is considered cosnistent
 * if it is either null or does not equal the previously saved id.
 *  
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class TransactionHelper implements Serializable {
  //*******************************************************************
  // FIELDS
  //*******************************************************************
  private Long currentTransactionId;
  private Random random = new Random(System.currentTimeMillis());
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  /**
   * Generates a new current transaction id.
   */
  public void resetTransactionId() {
    currentTransactionId = new Long(random.nextLong());
  }
  
  /**
   * Returns the current transaction id.
   */
  public Object getCurrentTransactionId() {
    return currentTransactionId;  
  }
  
  /**
   * Returns true if current transaction id is null or transactionId does not
   * equal the current transaction id. 
   */
  public boolean isConsistent(Object transactionId) {
    if (currentTransactionId == null)
      return true;

    if (TransactionContext.OVERRIDE_KEY.equals(transactionId))
      return true;

    return currentTransactionId.toString().equals(transactionId);
  }
}
