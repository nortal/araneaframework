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

package org.araneaframework.uilib.core;

import org.araneaframework.Message;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.support.PopupWindowProperties;

/**
 * A (pseudo)widget that allows opening flows in popup windows by starting flows almost the usual way.
 * 
 * <pre>
 * <code>
 *   ((FlowContext)getEnvironment.getEntry(FlowContext.class)).
 *   	start(
 *   		new PopupFlowWidget(
 *   			new NiceWidget(),  // flow to start  
 *   			new PopupWindowProperties(), // exactly what it says
 *          	// Factory that constructs the initial {@link org.araneaframework.Message} sent to created session thread.
 *   			new ApplicationAndSituationSpecificMessageFactory()
 *   		), 
 *   		null, null);
 * </code>
 * </pre>
 * 
 * @author Taimo Peelo (taimo@webmedia.ee)
 */
public class PopupFlowWidget extends BaseApplicationWidget {
  protected Message msg;
  protected PopupWindowProperties properties;

  /**
   * Constructs flow that will open <code>widget</code> in a new popup window.
   * 
   * @param widget flow to start
   * @param properties properties for newly opened popup window
   * @param messageFactory {@link org.araneaframework.uilib.core.PopupFlowWidget.MessageFactory} that builds message
   *                       that is sent to the freshly created session-thread for opening requested flow.
   */
  public PopupFlowWidget(Widget widget, PopupWindowProperties properties, MessageFactory messageFactory) {
    this.msg = messageFactory.buildMessage(new PopupFlowWrapperWidget(widget));
    this.properties = properties;
  }

  protected void init() throws Exception {
    super.init();

    PopupWindowContext popupCtx = 
      (PopupWindowContext) getEnvironment().getEntry(PopupWindowContext.class);
    popupCtx.open(msg, properties, this);
  }

  /**
   * @author Taimo Peelo (taimo@webmedia.ee)
   */
  public interface MessageFactory {
    /** 
     * Constructs a {@link org.araneaframework.Message} which
     * starts <code>rootFlow</code> when it is sent to a freshly created session-thread
     * component graph.
     *  
     * @param rootFlow flow to start in a freshly created session thread
     * @return {@link org.araneaframework.Message} that starts  
     * 			<code>rootFlow</code> when it is sent to a freshly created session-thread
     */
    public Message buildMessage(Widget rootFlow);
  }
}
