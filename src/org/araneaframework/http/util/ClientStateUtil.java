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

package org.araneaframework.http.util;

import org.araneaframework.Environment;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.http.ClientStateContext;

/**
 * Utility class for setting system form's fields.
 * 
 * @author Toomas Römer <toomas@webmedia.ee>
 */
public abstract class ClientStateUtil {
  
  public static Object getTopServiceId(Environment env) {
    TopServiceContext topServiceContext = (TopServiceContext) env.getEntry(TopServiceContext.class);
    if (topServiceContext == null)
      return null;
    return topServiceContext.getCurrentId();
  }
  
  public static Object getThreadServiceId(Environment env) {
    ThreadContext threadContext = (ThreadContext) env.getEntry(ThreadContext.class);
    if (threadContext == null)
      return null;
    return threadContext.getCurrentId();
  }
  
  public static Object getTransactionId(Environment env) {
    TransactionContext transactionContext = (TransactionContext) env.getEntry(TransactionContext.class);
    if (transactionContext == null)
      return null;
    return transactionContext.getTransactionId();
  }
  
  public static String getClientState(Environment env) {
    ClientStateContext clientStateContext = (ClientStateContext) env.getEntry(ClientStateContext.class);
    if (clientStateContext == null)
      return null;
    return clientStateContext.getClientState();
  }
  
  public static String getClientStateVersion(Environment env) {
    ClientStateContext clientStateContext = (ClientStateContext) env.getEntry(ClientStateContext.class);
    if (clientStateContext == null)
      return null;
    return clientStateContext.getClientStateVersion();
  }
  
  public static Object requireTopServiceId(Environment env) {
    TopServiceContext topServiceContext = (TopServiceContext) env.requireEntry(TopServiceContext.class);
    return topServiceContext.getCurrentId();
  }
  
  public static Object requireThreadServiceId(Environment env) {
    ThreadContext threadContext = (ThreadContext) env.requireEntry(ThreadContext.class);
    return threadContext.getCurrentId();
  }
  
  public static Object requireTransactionId(Environment env) {
    TransactionContext transactionContext = (TransactionContext) env.requireEntry(TransactionContext.class);
    return transactionContext.getTransactionId();
  }
  
  public static String requireClientState(Environment env) {
    ClientStateContext clientStateContext = (ClientStateContext) env.requireEntry(ClientStateContext.class);
    return clientStateContext.getClientState();
  }
  
  public static String requireClientStateVersion(Environment env) {
    ClientStateContext clientStateContext = (ClientStateContext) env.requireEntry(ClientStateContext.class);
    return clientStateContext.getClientStateVersion();
  }
}
