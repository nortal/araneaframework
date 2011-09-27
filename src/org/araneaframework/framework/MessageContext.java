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

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.araneaframework.http.UpdateRegionProvider;

/**
 * A context for adding messages to a central pool of messages for later output with a consistent look.
 * <p>
 * A good example is {@link org.araneaframework.framework.filter.StandardMessagingFilterWidget}. It registers a
 * MessageContext in the environment and if a childservice needs to output a message which should have a consistent look
 * (location on the screen, styles etc.) it adds the message via <code>showMessage(String,String)</code>.
 * </p>
 * <p>
 * A widget can use the MessageContext from the environment to output the accumulated messages.
 * </p>
 * <p>
 * Permanent messages should stay in MessageContext until explicitly cleared, other messages should be cleared after the
 * user has seen them once.
 * </p>
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface MessageContext extends UpdateRegionProvider {

  /**
   * @since 1.1
   */
  String MESSAGE_REGION_KEY = "messages";

  /**
   * A message type indicating its an error message.
   */
  String ERROR_TYPE = "error";

  /**
   * A message type indicating its an info message.
   */
  String WARNING_TYPE = "warning";

  /**
   * A message type indicating its an info message.
   */
  String INFO_TYPE = "info";

  /**
   * Adds the <code>message</code> of given message <code>type</code> to the messages to be shown the user. The message
   * will be removed from this context before the next request.
   * 
   * @param type The type of messages that the given message will be bound to. Can be one of, but is not limited to the
   *          ones defined {@link MessageContext}.
   * @param message The message text to show. Can be a label that will be localized before displaying.
   * @param params Optional parameters for the message label.
   */
  void showMessage(String type, String message, Object... params);

  /**
   * Adds the <code>messageData</code> of given message <code>type</code> to the messages to be shown the user. The
   * message will be removed from this context before the next request.
   * 
   * @param type The type of messages that the given message will be bound to. Can be one of, but is not limited to the
   *          ones defined {@link MessageContext}.
   * @param messageData The data object describing the message to show.
   * @since 2.0
   */
  void showMessage(String type, MessageData messageData);

  /**
   * Adds the <code>messages</code> of given message <code>type</code> to the messages to be shown the user. The
   * messages will be removed from this context before the next request.
   * 
   * @param type The type of messages that given messages will be bound to. Can be one of, but is not limited to the
   *          ones defined {@link MessageContext}.
   * @param messages The message texts to show. Can be labels that will be localized before displaying.
   * @since 1.1
   */
  void showMessages(String type, Set<String> messages);

  /**
   * Adds the <code>messages</code> of given message <code>type</code> to the messages to be shown the user. The
   * messages will be removed from this context before the next request.
   * 
   * @param type The type of messages that the given message will be bound to. Can be one of, but is not limited to the
   *          ones defined {@link MessageContext}.
   * @param messagesData A set of data objects describing the messages to show.
   * @since 2.0
   */
  void showMessagesData(String type, Set<MessageData> messagesData);

  /**
   * Removes the <code>message</code> from the messages of given message <code>type</code>.
   * 
   * @param type The type of messages where the given <code>message</code> will be removed from. Can be one of, but is
   *          not limited to the ones defined {@link MessageContext}.
   * @param message The message to remove. When it's a label to localize, the label parameters will be ignored.
   * @since 1.1
   */
  void hideMessage(String type, String message);

  /**
   * Removes the <code>messageData</code> from the messages of given message <code>type</code>.
   * 
   * @param type The type of messages that the given message will be removed from. Can be one of, but is not limited to
   *          the ones defined {@link MessageContext}.
   * @param messageData The data object describing the message to remove.
   * @since 2.0
   */
  void hideMessage(String type, MessageData messageData);

  /**
   * Removes the <code>messages</code> from the messages of given message <code>type</code>.
   * 
   * @param type The type of messages that the given messages will be removed from. Can be one of, but is not limited to
   *          the ones defined {@link MessageContext}.
   * @param messages The messages to remove. When a message is a label to localize, the label parameters will be
   *          ignored.
   * @since 1.1
   */
  void hideMessages(String type, Set<String> messages);

  /**
   * Removes the <code>messagesData</code> from the messages of given message <code>type</code>.
   * 
   * @param type The type of messages that the given messages will be removed from. Can be one of, but is not limited to
   *          the ones defined {@link MessageContext}.
   * @param messagesData The data objects describing the messages to remove.
   * @since 2.0
   */
  void hideMessagesData(String type, Set<MessageData> messagesData);

  // ==========================================================================
  // Some concrete methods of showMessage() and hideMessage():
  // ==========================================================================

  /**
   * Adds the <code>message</code> to the messages of {@link #ERROR_TYPE} to be shown to the user.
   * 
   * @param message The message text to show. Can be a label that will be localized before displaying.
   * @param params Optional parameters for the message label.
   * @see #showMessage(String, String, Object...)
   */
  void showErrorMessage(String message, Object... params);

  /**
   * Removes the <code>message</code> from the messages of {@link #ERROR_TYPE}.
   * 
   * @param message The message to remove. When it's a label to localize, the label parameters will be ignored.
   * @see #hideMessage(String, String)
   * @since 1.1
   */
  void hideErrorMessage(String message);

  /**
   * Adds the <code>message</code> to the messages of {@link #WARNING_TYPE} to be shown to the user.
   * 
   * @param message The message text to show. Can be a label that will be localized before displaying.
   * @param params Optional parameters for the message label.
   * @see #showMessage(String, String, Object...)
   */
  void showWarningMessage(String message, Object... params);

  /**
   * Removes the <code>message</code> from the messages of {@link #WARNING_TYPE}.
   * 
   * @param message The message to remove. When it's a label to localize, the label parameters will be ignored.
   * @see #hideMessage(String, String)
   * @since 1.1
   */
  void hideWarningMessage(String message);

  /**
   * Adds the <code>message</code> to the messages of {@link #INFO_TYPE} to be shown to the user.
   * 
   * @param message The message text to show. Can be a label that will be localized before displaying.
   * @param params Optional parameters for the message label.
   * @see #showMessage(String, String, Object...)
   */
  void showInfoMessage(String message, Object... params);

  /**
   * Removes the <code>message</code> from the messages of {@link #INFO_TYPE}.
   * 
   * @param message The message to remove. When it's a label to localize, the label parameters will be ignored.
   * @see #hideMessage(String, String)
   * @since 1.1
   */
  void hideInfoMessage(String message);

  // ==========================================================================
  // Solution for adding permanent messages that exist until manually removed:
  // ==========================================================================

  /**
   * Shows a permanent message <code>message</code> of type <code>type</code> to the user. The message will be shown
   * until hidden explicitly.
   * 
   * @param type The type of messages that the given message will be bound to. Can be one of, but is not limited to the
   *          ones defined {@link MessageContext}.
   * @param message The message text to show. Can be a label that will be localized before displaying.
   * @param params Optional parameters for the message label.
   */
  void showPermanentMessage(String type, String message, Object... params);

  /**
   * Clears the specific permanent message, under all message types where it might be present.
   * 
   * @param message to be removed from permanent messages
   */
  void hidePermanentMessage(String message);

  // ==========================================================================
  // Methods for removing all messages:
  // ==========================================================================

  /**
   * Clears all non-permanent messages.
   */
  void clearMessages();

  /**
   * Clears all of the permanent messages.
   */
  void clearPermanentMessages();

  /**
   * Clears all messages (both permanent and usual).
   */
  void clearAllMessages();

  // ==========================================================================
  // Methods for retrieving messages in order to render them:
  // ==========================================================================

  /**
   * Returns all messages as a Map. The keys of the Map are the different message types encountered so far and under the
   * keys are collections of messages associated with that type.
   * <p>
   * A child service should do as follows to access the messages
   * 
   * <pre>
   * MessageContext msgCtx = getEnvironment().requireEntry(MessageContext.class);
   * Map&lt;String, Collection&lt;MessageData&gt;&gt; map = msgCtx.getMessages();
   * Collection&lt;MessageData&gt; list = map.get(MessageContext.ERROR_TYPE);
   * </pre>
   * <p>
   * The map could be <code>null</code>, if this service was not used. The collection is <code>null</code>, if no
   * messages of that type been added to the messages.
   * 
   * @return A map of message types and their corresponding messages.
   * @since 1.1
   */
  Map<String, Collection<MessageData>> getMessages();

  /**
   * Returns all messages after resolving their values. Otherwise similar to {@link #getMessages()}.
   * 
   * @param locCtx The localization context that is used for resolving.
   * @return A map of message types and their corresponding values.
   * @since 2.0
   */
  Map<String, Collection<String>> getResolvedMessages(LocalizationContext locCtx);

  /**
   * The contract for storing message data. The message is not just one string. It can have parameters that will be
   * inserted into right places in the message text.
   * 
   * @author Martti Tamm (martti@araneaframework.org)
   * @since 2.0
   */
  interface MessageData extends Serializable {

    /**
     * Provides the message code as it was provided to the message context.
     * 
     * @return The message code.
     */
    String getMessage();

    /**
     * Provides message parameters as they were provided to the message context.
     * 
     * @return The message parameters.
     */
    Object[] getMessageParameters();

  }
}
