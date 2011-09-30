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

package org.araneaframework.framework;

import org.araneaframework.Widget;

/**
 * A {@link Widget} that is {@link FlowContext}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public interface FlowContextWidget extends FlowContext, Widget {

  /**
   * The event ID that flow context widget can process to cancel the given number of last flows. The event parameter is
   * a positive integer indicating how many steps back the flow should return. If the integer is greater than the number
   * of opened flows, all child flows will be closed, keeping just the flow container (the menu). If the integer is zero
   * or less, no changes will be done.
   * 
   * @since 2.0
   */
  String FLOW_CANCEL_EVENT = "cancelFlow";

  /**
   * The setter for controlling whether {@link #FLOW_CANCEL_EVENT} is allowed to be processed. By default, depending on
   * the implementation, the event should be allowed. This method can be called to turn it off, e.g. for security
   * purposes.
   * 
   * @param allowFlowCancelEvent A Boolean indicating whether {@link #FLOW_CANCEL_EVENT} is allowed.
   * @since 2.0
   */
  void setAllowFlowCancelEvent(boolean allowFlowCancelEvent);

}
