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

package org.araneaframework.framework.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.util.Assert;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.core.BaseFilterWidget;
import org.araneaframework.http.util.JsonArray;
import org.araneaframework.http.util.JsonObject;

/**
 * Filter service implementing messaging support. This service also exposes {@link MessageContext} to child components
 * through environment. It can be used to add messages for later output.
 * <p>
 * An example how to add messages to the context follows:
 * 
 * <pre>
 * MessageContext messageContext = getEnvironment().get(MessageContext.class);
 * messageContext.showMessage(MessageContext.INFO_TYPE, &quot;Hello message!&quot;);
 * // or:
 * messageContext.showInfoMessage(&quot;Hello message!&quot;);
 * </pre>
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class StandardMessagingFilterWidget extends BaseFilterWidget implements MessageContext {

  private Map<String, Collection<MessageData>> permanentMessages = new LinkedHashMap<String, Collection<MessageData>>();

  private Map<String, Collection<MessageData>> messages = new LinkedHashMap<String, Collection<MessageData>>();

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    clearMessages();
    super.action(path, input, output);
  }

  @Override
  protected void update(InputData input) throws Exception {
    clearMessages();
    super.update(input);
  }

  @Override
  protected Environment getChildWidgetEnvironment() {
    return new StandardEnvironment(getEnvironment(), MessageContext.class, this);
  }

  /**
   * {@inheritDoc}
   */
  public void showMessage(String type, final String message, final Object... params) {
    this.messages = storeMessage(this.messages, type, message, params);
  }

  /**
   * {@inheritDoc}
   */
  public void showMessage(String type, MessageData messageData) {
    this.messages = storeMessage(this.messages, type, messageData);
  }

  /**
   * {@inheritDoc}
   */
  public void hideMessage(String type, String message) {
    Assert.notEmptyParam(type, "type");
    removeMessage(this.messages, type, message);
  }

  /**
   * {@inheritDoc}
   */
  public void hideMessage(String type, MessageData messageData) {
    Assert.notEmptyParam(type, "type");
    removeMessage(this.messages, type, messageData);
  }

  /**
   * {@inheritDoc}
   */
  public void showMessages(String type, Set<String> messages) {
    Assert.notNullParam(messages, "messages");
    for (String message : messages) {
      showMessage(type, message);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void showMessagesData(String type, Set<MessageData> messagesData) {
    Assert.notNullParam(messagesData, "messagesData");
    for (MessageData message : messagesData) {
      showMessage(type, message);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void hideMessages(String type, Set<String> messages) {
    Assert.notNullParam(messages, "messages");
    for (String message : messages) {
      hideMessage(type, message);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void hideMessagesData(String type, Set<MessageData> messagesData) {
    Assert.notNullParam(messagesData, "messagesData");
    for (MessageData message : messagesData) {
      hideMessage(type, message);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void showPermanentMessage(String type, String message, Object... params) {
    this.permanentMessages = storeMessage(this.permanentMessages, type, message, params);
  }

  /**
   * {@inheritDoc}
   */
  public void hidePermanentMessage(String message) {
    removeMessage(this.permanentMessages, null, message);
  }

  /**
   * {@inheritDoc}
   */
  public void showErrorMessage(String message, Object... params) {
    showMessage(ERROR_TYPE, message, params);
  }

  /**
   * {@inheritDoc}
   */
  public void hideErrorMessage(String message) {
    hideMessage(ERROR_TYPE, message);
  }

  /**
   * {@inheritDoc}
   */
  public void showInfoMessage(String message, Object... params) {
    showMessage(INFO_TYPE, message, params);
  }

  /**
   * {@inheritDoc}
   */
  public void hideInfoMessage(String message) {
    hideMessage(INFO_TYPE, message);
  }

  /**
   * {@inheritDoc}
   */
  public void showWarningMessage(String message, Object... params) {
    showMessage(WARNING_TYPE, message, params);
  }

  /**
   * {@inheritDoc}
   */
  public void hideWarningMessage(String message) {
    hideMessage(WARNING_TYPE, message);
  }

  /**
   * {@inheritDoc}
   */
  public void clearMessages() {
    this.messages.clear();
  }

  /**
   * {@inheritDoc}
   */
  public void clearPermanentMessages() {
    this.permanentMessages.clear();
  }

  /**
   * {@inheritDoc}
   */
  public void clearAllMessages() {
    clearMessages();
    clearPermanentMessages();
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Collection<MessageData>> getMessages() {
    // add permanent messages to one-time messages for rendering all messages together
    return Collections.unmodifiableMap(addPermanentMessages(this.messages));
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, Collection<String>> getResolvedMessages(LocalizationContext locCtx) {
    Assert.notNullParam(locCtx, "locCtx");

    Map<String, Collection<String>> result = new HashMap<String, Collection<String>>();

    for (Map.Entry<String, Collection<MessageData>> msgs : getMessages().entrySet()) {
      if (CollectionUtils.isNotEmpty(msgs.getValue())) {
        List<String> results = new LinkedList<String>();
        result.put(msgs.getKey(), results);

        for (MessageData msg : msgs.getValue()) {
          results.add(locCtx.getMessage(msg.getMessage(), msg.getMessageParameters()));
        }
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public Map<String, String> getRegions(LocalizationContext locCtx) {
    JsonObject messagesByType = new JsonObject();
    Map<String, Collection<String>> messageMap = getResolvedMessages(locCtx);

    if (messageMap != null) {
      for (Map.Entry<String, Collection<String>> entry : messageMap.entrySet()) {
        if (entry.getValue() != null) {
          String type = entry.getKey();
          JsonArray jsonMessages = new JsonArray();
          for (String message : entry.getValue()) {
            jsonMessages.appendString(message);
          }
          messagesByType.setProperty(type, jsonMessages.toString());
        }
      }
    }

    return Collections.singletonMap(MESSAGE_REGION_KEY, messagesByType.toString());
  }

  /**
   * Stores message of given type in given messageMap (created if <code>null</code> at invocation).
   * 
   * @param messageMap The messages map where the message should be stored.
   * @param type The type of message to store (one of the constants in {@link MessageContext}).
   * @param message The message to store permanently until removed.
   * @param params Optional parameters to be resolved in the message.
   * @return The same message map taken as a parameter.
   */
  protected static Map<String, Collection<MessageData>> storeMessage(Map<String, Collection<MessageData>> messageMap,
      String type, String message, Object... params) {

    Assert.notEmptyParam(message, "message");
    return storeMessage(messageMap, type, new StandardMessageData(message, params));
  }

  /**
   * Stores message of given type in given messageMap (created if <code>null</code> at invocation).
   * 
   * @param messageMap The messages map where the message should be stored.
   * @param type The type of message to store (one of the constants in {@link MessageContext}).
   * @param messageData The message to store permanently until removed.
   * @return The same message map taken as a parameter.
   */
  protected static Map<String, Collection<MessageData>> storeMessage(Map<String, Collection<MessageData>> messageMap,
      String type, MessageData messageData) {

    Assert.notEmptyParam(type, "type");
    Assert.notNullParam(messageData, "messageData");
    Assert.notEmpty(messageData.getMessage(), "messageData.getMessage() returned null or empty string.");

    Collection<MessageData> messages = messageMap.get(type);
    if (messages == null) {
      messages = new LinkedHashSet<MessageData>();
      messageMap.put(type, messages);
    }

    messages.add(messageData);
    return messageMap;
  }

  /**
   * Removes the given <code>message</code> from given message <code>type</code> in <code>messageMap</code>. When given
   * <code>type</code> is <code>NULL</code>, removes the given <code>message</code> from all types.
   * 
   * @param messageMap The messages map where the message should be stored.
   * @param msgType The type of message to store (one of the constants in {@link MessageContext}).
   * @param message The message to store permanently until removed.
   */
  protected static void removeMessage(Map<String, Collection<MessageData>> messageMap, String msgType,
      final String message) {
    Assert.notEmptyParam(message, "message");

    for (Map.Entry<String, Collection<MessageData>> entry : messageMap.entrySet()) {
      if (msgType == null || entry.getKey().equals(msgType)) {
        Collection<MessageData> messages = entry.getValue();

        for (Iterator<MessageData> i = messages.iterator(); i.hasNext();) {
          if (ObjectUtils.equals(i.next().getMessage(), message)) {
            i.remove();
          }
        }

        if (msgType != null) {
          break;
        }
      }
    }
  }

  /**
   * Removes the given <code>message</code> from given message <code>type</code> in <code>messageMap</code>. When given
   * <code>type</code> is <code>NULL</code>, removes the given <code>message</code> from all types.
   * 
   * @param messageMap The messages map where the message should be stored.
   * @param msgType The type of message to store (one of the constants in {@link MessageContext}).
   * @param messageData The message to store permanently until removed.
   */
  protected static void removeMessage(Map<String, Collection<MessageData>> messageMap, String msgType,
      MessageData messageData) {

    for (Map.Entry<String, Collection<MessageData>> entry : messageMap.entrySet()) {
      if (msgType == null || entry.getKey().equals(msgType)) {
        Collection<MessageData> messages = entry.getValue();

        for (Iterator<MessageData> i = messages.iterator(); i.hasNext();) {
          if (ObjectUtils.equals(i.next(), messageData)) {
            i.remove();
          }
        }

        if (msgType != null) {
          break;
        }
      }
    }
  }

  /**
   * Adds current permanent messages to given message map.
   * 
   * @param msgs The messages map where the message should be stored.
   * @return given message map with permanent messages added.
   */
  protected Map<String, Collection<MessageData>> addPermanentMessages(Map<String, Collection<MessageData>> msgs) {
    for (Map.Entry<String, Collection<MessageData>> entry : this.permanentMessages.entrySet()) {
      Collection<MessageData> destMessages = msgs.get(entry.getKey());

      if (destMessages == null) {
        msgs.put(entry.getKey(), entry.getValue());
      } else {
        destMessages.addAll(entry.getValue());
      }
    }

    return msgs;
  }

  /**
   * Standard implementation of {@link MessageData}.
   * 
   * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
   * @since 2.0
   */
  public static class StandardMessageData implements MessageData {

    private String message;

    private Object[] parameters;

    /**
     * Creates a new message data.
     * 
     * @param message The message code (required).
     * @param parameters Optional parameters to the message.
     */
    public StandardMessageData(String message, Object... parameters) {
      this.message = message;
      this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage() {
      return this.message;
    }

    /**
     * {@inheritDoc}
     */
    public Object[] getMessageParameters() {
      return this.parameters;
    }
  }
}
