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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections.set.ListOrderedSet;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.core.BaseFilterWidget;
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
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandardMessagingFilterWidget extends BaseFilterWidget implements MessageContext {
  protected Map permanentMessages;
  protected Map messages;

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
  
  /** 
   * Removes the given <code>message</code> from given message <code>type</code> in <code>messageMap</code>. 
   * When given <code>type</code> is <code>NULL</code>, removes the given <code>message</code> from all types. */
  protected Map removeMessage(String type, final String message, Map messageMap) {
    Assert.notEmptyParam(message, "message");

    if (messageMap == null)
      return null;

    for (Iterator i = messageMap.entrySet().iterator(); i.hasNext(); ) {
      Map.Entry next = (Map.Entry)i.next();
      if (type == null || next.getKey().equals(type)) {
        Collection messages = (Collection)(next).getValue();
        messages.remove(message);
        if (type != null) 
          break;
      }
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

  public void showMessages(String type, Set messages) {
    Assert.notNullParam(messages, "messages");
    for (Iterator i = messages.iterator(); i.hasNext(); ) {
      showMessage(type, (String)i.next());
    }
  }

  public void hideMessage(String type, String message) {
    Assert.notEmptyParam(type, "type");
    removeMessage(type, message, messages);
  }
  
  public void hideMessages(String type, Set messages) {
    Assert.notNullParam(messages, "messages");
    for (Iterator i = messages.iterator(); i.hasNext(); ) {
      hideMessage(type, (String)i.next());
    }
  }

  public void showPermanentMessage(String type, final String message) {
    permanentMessages = storeMessage(type, message, permanentMessages);
  }
  
  public void hidePermanentMessage(String message) {
    permanentMessages = removeMessage(null, message, permanentMessages);
  }

  public void showErrorMessage(String message) {
    showMessage(ERROR_TYPE, message);
  }
  
  public void hideErrorMessage(String message) {
    hideMessage(ERROR_TYPE, message);    
  }

  public void showInfoMessage(String message) {
    showMessage(INFO_TYPE, message);
  }
  
  public void hideInfoMessage(String message) {
    hideMessage(INFO_TYPE, message); 
  }
  
  public void showWarningMessage(String message) {
    showMessage(WARNING_TYPE, message);
  }
  
  public void hideWarningMessage(String message) {
    hideMessage(WARNING_TYPE, message);
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

  /**
   * @since 1.1
   */
  public Map getRegions() {
    JsonObject messagesByType = new JsonObject();
    Map messageMap = getMessages();
    if (messageMap != null) {
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
    }
    Map regions = new HashMap(1);
    regions.put(MessageContext.MESSAGE_REGION_KEY, messagesByType.toString());
    return regions;
  }

}
