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

package org.araneaframework.framework.context;

import org.araneaframework.framework.FlowContext.Handler;

/**
 * A default implementation of <code>Handler</code> that does not have any functionality. Instead it is more useful when
 * implementing <code>Handler</code> and (only) one of its methods.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.1
 */
public class DefaultHandler<T> implements Handler<T> {

  /**
   * An empty implementation for <code>onCancel</code> event.
   */
  public void onCancel() {}

  /**
   * An empty implementation for <code>onFinish</code> event.
   */
  public void onFinish(T returnValue) {}
}
