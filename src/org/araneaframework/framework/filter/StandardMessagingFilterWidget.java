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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.core.BaseFilterWidget;

/**
 * Adds a {@link org.araneaframework.framework.MessageContext} implementation to the environment that can
 * be used to add messages for later output.
 *<p>
 * An example how to add messages to the context follows:
 * <pre>
 * <code>
 * ...
 * MessageContext messageContext = (MessageContext)getEnvironment().get(CallContext.class);
 * messageContext.addMessage(MessageContext.INFO_TYPE, "Hello message!");
 * </code>
 * </pre>
 *</p>
 *
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardMessagingFilterWidget extends BaseFilterWidget implements MessageContext {
  private Map messages = new LinkedMap();

  protected void init() throws Exception {
    Map entries = new HashMap();
    entries.put(MessageContext.class, this);
    
    childWidget._getComponent().init(new StandardEnvironment(getChildWidgetEnvironment(), entries));
  }
  
  protected void update(InputData input) throws Exception {
    messages.clear();
    
    childWidget._getWidget().update(input);
  }
  
  /**
   * Adds all the messages to the output as Map under the key MESSAGE_KEY. The keys
   * of the Map are the different message types encountered so far and under the keys
   * are the messages in a List.
   *<p>
   * A child service should do as follows to access the messages
   * <pre>
   * <code>
   * ...
   * Map map = output.getAttribute(MESSAGE_KEY);
   * List list = (List)map.get(MessageContext.ERROR_TYPE); // list contains all the error messages
   * </code>
   * </pre>
   * The map could be null if this service was not used. The list is null if no errors have
   * been added to the messages. 
   *</p>
   */
  protected void render(OutputData output) throws Exception {
    Map typedMessages = new HashMap();
    
    for (Iterator i = messages.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      
      Collection typeCol = (Collection) typedMessages.get(entry.getValue());
      
      if (typeCol == null) {
        typeCol = new ArrayList();
        typedMessages.put(entry.getValue(), typeCol);
      }
      
      typeCol.add(entry.getKey());
    }
    
    output.pushAttribute(MessageContext.MESSAGE_KEY, typedMessages);
    
    try {
      childWidget._getWidget().render(output);
    }
    finally {
      output.popAttribute(MessageContext.MESSAGE_KEY);
    }
  }
  
  public void showMessage(String type, final String message) {
    messages.put(message, type);
  }

  public void showErrorMessage(String message) {
    showMessage(ERROR_TYPE, message);
  }

  public void showInfoMessage(String message) {
    showMessage(INFO_TYPE, message);
  }
  
  public void clearMessages() {
	  messages.clear();
  }
}
