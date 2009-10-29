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

package org.araneaframework.tests.framework.filter;

import org.araneaframework.framework.MessageContext.MessageData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.BaseWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.FilterWidget;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.mock.MockInputData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseMessageContextTests extends TestCase {
  protected MessageContext msgCtx;

  protected abstract MessageContext getMessageContext();

  @Override
  protected void setUp() throws Exception {
    msgCtx = getMessageContext();

    // assertions do not allow filter widgets without children :)
    ((FilterWidget)msgCtx).setChildWidget(new BaseWidget());

    Environment env = new StandardEnvironment(null, new HashMap<Class<?>, Object>());
    ((Widget)msgCtx)._getComponent().init(null, env);
  }

  // add nothing, test emptiness
  public void testEmpty_1() throws Exception {
    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();
    assertTrue("MessageMap must be null or empty.", messages == null || messages.size() == 0);
  }

  // add something, clear, test emptiness
  public void testEmpty_2() throws Exception {
    msgCtx.showInfoMessage("TestMessage");
    msgCtx.clearMessages();

    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();
    assertTrue("MessageMap must be null or empty.", messages == null || messages.size() == 0);
  }

  // test that non-permanent messages do not survive update(), map must be empty;
  public void testEmpty_3() throws Exception {
    msgCtx.showInfoMessage("message survival test");

    ((Widget)msgCtx)._getWidget().update(new MockInputData());

    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();
    assertTrue("MessageMap must be null or empty.", messages == null || messages.size() == 0);
  }

  // test that added messages really are present after render();
  public void testNonEmpty_1() throws Exception {
    msgCtx.showInfoMessage("surviving message");

    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();
    assertNotNull("messages must not be null", messages);
    assertTrue("MessageMap must contain ONE element!", messages.size() == 1);
  }

  // test that added permanent messages survive the update();
  public void testNonEmpty_2() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "message survival test");

    ((Widget)msgCtx)._getWidget().update(new MockInputData());

    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();
    assertNotNull("messages must not be null", messages);
    assertTrue("MessageMap must contain ONE element!", messages.size() == 1);
  }

  // test that permanent messages and messages for current render come together nicely in a Collection
  public void testNonEmpty_3() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "permanent message");
    msgCtx.showMessage(MessageContext.ERROR_TYPE, "one-time message");
    msgCtx.showErrorMessage("Another error message added with defined interface method.");

    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();
    assertNotNull("messages must not be null", messages);
    assertTrue("Messages must contain ONE elements!", messages.size() == 1);

    Collection<MessageData> errorMessages = messages.get(MessageContext.ERROR_TYPE);
    assertNotNull("Error messages must not be null", errorMessages);
    assertTrue("There must be THREE error messages", errorMessages.size() == 3);
  }

  // test that message hiding works
  public void testMessageHiding() throws Exception {
    msgCtx.showInfoMessage("infomessage");
    msgCtx.showWarningMessage("warningmessage");
    msgCtx.showErrorMessage("errormessage");

    msgCtx.hideInfoMessage("infomessage");
    msgCtx.hideWarningMessage("warningmessage");
    msgCtx.hideErrorMessage("errormessage");

    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();

    Collection<MessageData> infoMessages = messages.get(MessageContext.INFO_TYPE);
    Collection<MessageData> warningMessages = messages.get(MessageContext.WARNING_TYPE);
    Collection<MessageData> errorMessages = messages.get(MessageContext.ERROR_TYPE);

    assertTrue("infoMessages must be empty", infoMessages.isEmpty());
    assertTrue("warningMessages must be empty", warningMessages.isEmpty());
    assertTrue("errorMessages must be empty", errorMessages.isEmpty());

    // also test that only messages of given type are hidden (cleared)
    msgCtx.showInfoMessage("simplemessage");
    msgCtx.hideWarningMessage("simplemessage");

    infoMessages = messages.get(MessageContext.INFO_TYPE);
    warningMessages = messages.get(MessageContext.WARNING_TYPE);

    assertTrue("Info message must be present, since only warning was hidden", !infoMessages.isEmpty());
  }

  // test that hiding of permanent messages works
  public void testPermanentMessageHiding() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "permanent message");
    msgCtx.showMessage(MessageContext.ERROR_TYPE, "one-time message");
    msgCtx.showErrorMessage("Another error message added with defined interface method.");

    msgCtx.hidePermanentMessage("permanent message");

    Map<String, Collection<MessageData>> messages = msgCtx.getMessages();
    assertNotNull("messages must not be null", messages);
    assertTrue("Messages must contain ONE element!", messages.size() == 1);

    Collection<MessageData> errorMessages = messages.get(MessageContext.ERROR_TYPE);
    assertTrue("There must be TWO error messages", errorMessages.size() == 2);
  }

  /** Tests that message addition order is preserved on rendering. */
  public void testMessageOrderPreservation() throws Exception {
    ArrayList<String> messages = new ArrayList<String>(200);
    for (int i = 0; i < 200; i++) {
      String nextMessage = RandomStringUtils.randomAlphanumeric(30);
      messages.add(i, nextMessage);
        msgCtx.showErrorMessage(nextMessage);
    }

    Map<String, Collection<MessageData>> renderedMessageMap = msgCtx.getMessages();
    Collection<MessageData> renderedMessages = renderedMessageMap.get(MessageContext.ERROR_TYPE);

    int j = 0;
    for (Iterator<MessageData> i = renderedMessages.iterator(); i.hasNext(); j++)
      assertEquals(messages.get(j), i.next().getMessage());

    assertTrue("There should have been 200 error messages", j == 200);
  }

}
