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

package org.araneaframework.tests.framework.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.Environment;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.FilterWidget;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.mock.MockInputData;
import org.araneaframework.mock.core.MockBaseWidget;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public abstract class BaseMessageContextTests extends TestCase {
  protected MessageContext msgCtx;
  
  protected abstract MessageContext getMessageContext();
  
  protected void setUp() throws Exception {
    msgCtx = getMessageContext();

    // assertions do not allow filter widgets without childs :)
    ((FilterWidget)msgCtx).setChildWidget(new MockBaseWidget());
    
    Environment env = new StandardEnvironment(null, new HashMap());
    ((Widget)msgCtx)._getComponent().init(null, env);
  }
  
  // add nothing, test emptiness
  public void testEmpty_1() throws Exception {
    Map messages = msgCtx.getMessages();
    assertTrue("MessageMap must be null or empty.", messages == null || messages.size() == 0);
  }
  
  // add something, clear, test emptiness
  public void testEmpty_2() throws Exception {
    msgCtx.showInfoMessage("TestMessage");
    msgCtx.clearMessages();

    Map messages = msgCtx.getMessages();
    assertTrue("MessageMap must be null or empty.", messages == null || messages.size() == 0);
  }
  
  // test that non-permanent messages do not survive update(), map must be empty;
  public void testEmpty_3() throws Exception {
    msgCtx.showInfoMessage("message survival test");
    
    ((Widget)msgCtx)._getWidget().update(new MockInputData());
    
    Map messages = msgCtx.getMessages();
    assertTrue("MessageMap must be null or empty.", messages == null || messages.size() == 0);
  }

  // test that added messages really are present after render();
  public void testNonEmpty_1() throws Exception {
    msgCtx.showInfoMessage("surviving message");
    
    Map messages = msgCtx.getMessages();
    assertTrue("messages must not be null", messages != null);
    assertTrue("MessageMap must contain ONE element!", messages.size() == 1);
  }
  
  // test that added permanent messages survive the update();
  public void testNonEmpty_2() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "message survival test");
    
    ((Widget)msgCtx)._getWidget().update(new MockInputData());

    Map messages = msgCtx.getMessages();
    assertTrue("messages must not be null", messages != null);
    assertTrue("MessageMap must contain ONE element!", messages.size() == 1);
  }

  // test that permanent messages and messages for current render come together nicely in a Collection
  public void testNonEmpty_3() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "permanent message");
    msgCtx.showMessage(MessageContext.ERROR_TYPE, "one-time message");
    msgCtx.showErrorMessage("Another error message added with defined interface method.");
    
    Map messages = msgCtx.getMessages();
    assertTrue("messages must not be null", messages != null);
    assertTrue("Messages must contain ONE elements!", messages.size() == 1);
    
    Object errorMessages = messages.get(MessageContext.ERROR_TYPE);
    assertTrue("Messages must be in java.util.Collection", errorMessages instanceof Collection);

    assertTrue("There must be THREE error messages", ((Collection)errorMessages).size() == 3);
  }
  
  // test that message hiding works
  public void testMessageHiding() throws Exception {
    msgCtx.showInfoMessage("infomessage");
    msgCtx.showWarningMessage("warningmessage");
    msgCtx.showErrorMessage("errormessage");

    msgCtx.hideInfoMessage("infomessage");
    msgCtx.hideWarningMessage("warningmessage");
    msgCtx.hideErrorMessage("errormessage");
    
    Map messages = msgCtx.getMessages();

    Object infoMessages = messages.get(MessageContext.INFO_TYPE);
    Object warningMessages = messages.get(MessageContext.WARNING_TYPE);
    Object errorMessages = messages.get(MessageContext.ERROR_TYPE);

    assertTrue("infoMessages must be in java.util.Collection", infoMessages instanceof Collection);
    assertTrue("warningMessages must be in java.util.Collection", warningMessages instanceof Collection);
    assertTrue("errorMessages must be in java.util.Collection", errorMessages instanceof Collection);
    
    assertTrue("infoMessages must be empty", ((Collection)infoMessages).isEmpty());
    assertTrue("warningMessages must be empty", ((Collection)warningMessages).isEmpty());
    assertTrue("errorMessages must be empty", ((Collection)errorMessages).isEmpty());
    
    // also test that only messages of given type are hidden (cleared)
    msgCtx.showInfoMessage("simplemessage");
    msgCtx.hideWarningMessage("simplemessage");

    infoMessages = messages.get(MessageContext.INFO_TYPE);
    warningMessages = messages.get(MessageContext.WARNING_TYPE);

    assertTrue("Info message must be present, since only warning was hidden", !((Collection)infoMessages).isEmpty());
  }
  
  // test that hiding of permanent messages works
  public void testPermanentMessageHiding() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "permanent message");
    msgCtx.showMessage(MessageContext.ERROR_TYPE, "one-time message");
    msgCtx.showErrorMessage("Another error message added with defined interface method.");
    
    msgCtx.hidePermanentMessage("permanent message");
    
    Map messages = msgCtx.getMessages();
    assertTrue("messages must not be null", messages != null);
    assertTrue("Messages must contain ONE element!", messages.size() == 1);
    
    Object errorMessages = messages.get(MessageContext.ERROR_TYPE);
    assertTrue("Messages must be in java.util.Collection", errorMessages instanceof Collection);

    assertTrue("There must be TWO error messages", ((Collection)errorMessages).size() == 2);
  }
  
  /** Tests that message addition order is preserved on rendering. */
  public void testMessageOrderPreservation() throws Exception {
    ArrayList messages = new ArrayList(200);
    for (int i = 0; i < 200; i++) {
      String nextMessage = RandomStringUtils.randomAlphanumeric(30);
      messages.add(i, nextMessage);
	    msgCtx.showErrorMessage(nextMessage);
    }

    Map renderedMessageMap = msgCtx.getMessages();
    Collection renderedMessages = (Collection)renderedMessageMap.get(MessageContext.ERROR_TYPE);
    
    int j = 0;
    for (Iterator i = renderedMessages.iterator(); i.hasNext(); j++)
      assertEquals((String)messages.get(j), (String)i.next());

    assertTrue("There should have been 200 error messages", j == 200);
  }

}
