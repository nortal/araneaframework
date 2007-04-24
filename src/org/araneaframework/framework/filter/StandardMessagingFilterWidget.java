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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections.set.ListOrderedSet;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.JsonArray;
import org.araneaframework.http.util.JsonObject;

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
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class StandardMessagingFilterWidget extends BaseFilterWidget implements MessageContext {

  public static final String MESSAGE_REGION_KEY = "messages";

  protected Map permanentMessages;
  protected Map messages;
  
  protected void init() throws Exception {
    super.init();
    UpdateRegionContext updateRegionContext = (UpdateRegionContext) getEnvironment().getEntry(UpdateRegionContext.class);
    if (updateRegionContext != null) {
      updateRegionContext.addRegionHandler(MESSAGE_REGION_KEY, new MessageRegionHandler());
    }
  }

  protected void update(InputData input) throws Exception {
    clearMessages();

    super.update(input);
  }
  
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), MessageContext.class, this);
  }
  
  /** Stores message of given type in given messageMap (created if <code>null</code> at invocation). */
  protected Map storeMessage(final String type, final String message, Map messageMap) {
    Assert.notEmptyParam(type, "type");
    Assert.notEmptyParam(message, "message");
    
    if (messageMap == null)
      messageMap = new LinkedMap();

    Collection messages = (Collection)messageMap.get(type);

    if (messages == null) {
      messages = ListOrderedSet.decorate(new HashSet());
      messageMap.put(type, messages);
    }

    messages.add(message);
    
    return messageMap;
  }
  
  /** Removes the given message from given messageMap. */
  protected Map removeMessage(final String message, Map messageMap) {
    Assert.notEmptyParam(message, "message");

    if (messageMap == null)
      return null;

    for (Iterator i = messageMap.entrySet().iterator(); i.hasNext(); ) {
      Collection messages = (Collection)((Map.Entry)i.next()).getValue();
      messages.remove(message);
    }

    return messageMap;
  }
  
  /** 
   * Adds current permanent messages to given message map.
   * @return given message map with permanent messages added. 
   */
  protected Map addPermanentMessages(Map msgs) {
    if (msgs == null && permanentMessages.size() > 0)
      msgs = new LinkedMap();

    for (Iterator i = permanentMessages.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry)i.next();
      Collection typedMessages = (Collection)msgs.get(entry.getKey());

      if (typedMessages == null)
        msgs.put(entry.getKey(), entry.getValue());
      else
        typedMessages.addAll((Collection)entry.getValue());
    }
    return msgs;
  }

  public void showMessage(String type, final String message) {
    messages = storeMessage(type, message, messages);
  }

  public void showPermanentMessage(String type, final String message) {
    permanentMessages = storeMessage(type, message, permanentMessages);
  }
  
  public void hidePermanentMessage(String message) {
    permanentMessages = removeMessage(message, permanentMessages);
  }

  public void showErrorMessage(String message) {
    showMessage(ERROR_TYPE, message);
  }

  public void showInfoMessage(String message) {
    showMessage(INFO_TYPE, message);
  }
  
  public void showWarningMessage(String message) {
    showMessage(WARNING_TYPE, message);
  }
  
  public void clearMessages() {
    if (messages != null)
      messages.clear();
  }

  public void clearPermanentMessages() {
    if (permanentMessages != null)
      permanentMessages.clear();
  }
  
  public void clearAllMessages() {
    clearMessages();
    clearPermanentMessages();
  }

  public Map getMessages() {
    if (permanentMessages != null) {
      // add permanent messages to-one time messages for rendering
      messages = addPermanentMessages(messages);
    }
    if (messages == null) {
      return null;
    }
    return Collections.unmodifiableMap(messages);
  }

  /* ************************************************************************************
   * Internal inner classes
   * ************************************************************************************/

  protected class MessageRegionHandler implements UpdateRegionContext.RegionHandler {

    public String getContent() throws Exception {
      Map messageMap = getMessages();
      if (messageMap == null || messageMap.isEmpty())
        return null;

      JsonObject messagesByType = new JsonObject();
      for (Iterator i = messageMap.entrySet().iterator(); i.hasNext(); ) {
        Map.Entry entry = (Map.Entry) i.next();
        if (entry.getValue() == null) {
          continue;
        }
        String type = (String) entry.getKey();
        JsonArray messages = new JsonArray();
        for (Iterator j = ((Collection) entry.getValue()).iterator(); j.hasNext(); ) {
          String message = (String) j.next();
          messages.appendString(message);
        }
        messagesByType.setProperty(type, messages.toString());
      }
      return messagesByType.toString();
    }

  }

}
