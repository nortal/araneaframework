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

package org.araneaframework.framework;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.araneaframework.http.UpdateRegionProvider;

/**
 * A context for adding messages to a central pool of messages for later 
 * output with a consistent look. 
 * <p>A good example is {@link org.araneaframework.framework.filter.StandardMessagingFilterWidget}.
 * It registers a MessageContext in the environment and if a childservice needs to output a
 * message which should have a consistent look (location on the screen, styles etc.) it adds
 * the message via <code>showMessage(String,String)</code>.
 * </p>
 * <p>
 * A widget can use the MessageContext from the environment to output the accumulated
 * messages.
 * </p>
 * <p>
 * Permanent messages should stay in MessageContext until explicitly cleared, other messages should
 * be cleared after the user has seen them once.
 * </p>
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public interface MessageContext extends UpdateRegionProvider {
  /** @since 1.1 */
  public static final String MESSAGE_REGION_KEY = "messages";

  /**
   * A message type indicating its an error message. 
   */
  public static final String ERROR_TYPE = "error";
  
  /**
   * A message type indicating its an info message.
   */
  public static final String WARNING_TYPE = "warning";   
  
  /**
   * A message type indicating its an info message.
   */
  public static final String INFO_TYPE = "info";

  /**
   * Shows a message <code>message</code> of type <code>type</code> to the user. 
   * Message is cleared after the user sees it once.
   */
  public void showMessage(String type, String message);
  
  /**
   * Shows <code>messages</code> of given <code>type</code> to the user. 
   * Messages are cleared after the user sees them once.
   * 
   * @param messages Set&lt;String&gt;
   * @since 1.1
   */
  public void showMessages(String type, Set<String> messages);
  
  /**
   * Removes a message <code>message</code> of type <code>type</code>. 
   * @since 1.1
   */
  public void hideMessage(String type, String message);
  
  /**
   * Removes messages <code>message</code> of type <code>type</code>. 
   * @param messages Set&lt;String&gt;
   * 
   * @since 1.1
   */
  public void hideMessages(String type, Set<String> messages);

  /**
   * Shows an error message to the user.
   */
  public void showErrorMessage(String message);
  
  /**
   * Hides an error message from user.
   * @since 1.1
   */
  public void hideErrorMessage(String message);
  
  /**
   * Shows a warning message to the user.
   */
  public void showWarningMessage(String message);

  /**
   * Hides a warning message from user.
   * @since 1.1
   */
  public void hideWarningMessage(String message);

  /**
   * Shows an informative message to the user.
   */
  public void showInfoMessage(String message);
  
  /**
   * Hides an info message from user.
   * @since 1.1
   */
  public void hideInfoMessage(String message);
  
  /**
   * Clears all non-permanent messages.
   */
  public void clearMessages();

  /**
   * Shows a permanent message <code>message</code> of type <code>type</code> to the user. 
   * The message will be shown until hidden explicitly. 
   */
  public void showPermanentMessage(String type, String message);
  
  /**
   * Clears the specific permanent message, under all message types where it might be present.
   * @param message to be removed from permanent messages
   */
  public void hidePermanentMessage(String message);
  
  /**
   * Clears all of the permanent messages.
   */
  public void clearPermanentMessages();
  
  /**
   * Clears all messages (both permanent and usual).
   */  
  public void clearAllMessages();
  
  /**
   * Returns all messages as a Map. The keys
   * of the Map are the different message types encountered so far and under the keys
   * are the messages in a Collection.
   * <p>
   * A child service should do as follows to access the messages
   * <pre>
   * <code>
   * ...
   * MessageContext msgCtx = (MessageContext) getEnvironment().requireEntry(MessageContext.class);
   * Map map = msgCtx.getMessages();
   * Collection list = (Collection) map.get(MessageContext.ERROR_TYPE); // collection contains all the error messages
   * </code>
   * </pre>
   * The map could be null if this service was not used. The collection is null if no messages of
   * that type been added to the messages. 
   * </p>
   * 
   * @since 1.1
   */
  public Map<String, Collection<String>> getMessages();
}
