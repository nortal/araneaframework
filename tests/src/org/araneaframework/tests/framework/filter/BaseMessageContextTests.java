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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.collections.map.UnmodifiableMap;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
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
  protected OutputData output;
  
  protected abstract MessageContext getMessageContext();

  protected OutputData getOutputData() {
    return new MockOutputData();
  }
  
  protected boolean isMap(Object object) {
    return (object == null) || (object instanceof Map);
  }
  
  protected void setUp() throws Exception {
    output = getOutputData();
    msgCtx = getMessageContext();

    // assertions do not allow filter widgets without childs :)
    ((FilterWidget)msgCtx).setChildWidget(new MockBaseWidget());
    
    Environment env = new StandardEnvironment(null, new HashMap());
    ((Widget)msgCtx)._getComponent().init(env);
  }
  
  // add nothing, test emptiness
  public void testEmpty_1() throws Exception {
    ((Widget)msgCtx)._getWidget().render(output);

    Object messages = output.getAttribute(MessageContext.MESSAGE_KEY);
    assertTrue("Messages must be typed java.util.Map or be null.", isMap(messages));
    if (messages != null)   
      assertTrue("MessageMap must be empty.", ((Map)messages).size() == 0);
  }
  
  // add something, clear, test emptiness
  public void testEmpty_2() throws Exception {
    msgCtx.showInfoMessage("TestMessage");
    msgCtx.clearMessages();

    ((Widget)msgCtx)._getWidget().render(output);

    Object messages = output.getAttribute(MessageContext.MESSAGE_KEY);
    assertTrue("Messages must be typed java.util.Map or be null.", isMap(messages));
    if (messages != null)
        assertTrue("MessageMap must be empty.", ((Map)messages).size() == 0);
  }
  
  // test that non-permanent messages do not survive update(), map must be empty;
  public void testEmpty_3() throws Exception {
    msgCtx.showInfoMessage("message survival test");
    
    ((Widget)msgCtx)._getWidget().update(new MockInputData());
    ((Widget)msgCtx)._getWidget().render(output);
    
    Object messages = output.getAttribute(MessageContext.MESSAGE_KEY);
    assertTrue("Messages must be typed java.util.Map or be null.", isMap(messages));
    if (messages != null)
        assertTrue("MessageMap must be empty.", ((Map)messages).size() == 0);
  }

  // test that added messages really are present after render();
  public void testNonEmpty_1() throws Exception {
    msgCtx.showInfoMessage("surviving message");
    ((Widget)msgCtx)._getWidget().render(output);
    
    Object messages = output.getAttribute(MessageContext.MESSAGE_KEY);
    assertTrue("messages must not be null", messages != null);
    assertTrue("MessageMap must contain ONE element!", ((Map)messages).size() == 1);
  }
  
  // test that added permanent messages survive the update();
  public void testNonEmpty_2() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "message survival test");
    
    ((Widget)msgCtx)._getWidget().update(new MockInputData());
    ((Widget)msgCtx)._getWidget().render(output);

    Object messages = output.getAttribute(MessageContext.MESSAGE_KEY);
    assertTrue("messages must not be null", messages != null);
    assertTrue("MessageMap must contain ONE element!", ((Map)messages).size() == 1);
  }

  // test that permanent messages and messages for current render come together nicely in a Collection
  public void testNonEmpty_3() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "permanent message");
    msgCtx.showMessage(MessageContext.ERROR_TYPE, "one-time message");
    msgCtx.showErrorMessage("Another error message added with defined interface method.");
    
    ((Widget)msgCtx)._getWidget().render(output);
    
    Object messages = output.getAttribute(MessageContext.MESSAGE_KEY);
    assertTrue("messages must not be null", messages != null);
    assertTrue("Messages must contain ONE elements!", ((Map)messages).size() == 1);
    
    Object errorMessages = ((Map)messages).get(MessageContext.ERROR_TYPE);
    assertTrue("Messages must be in java.util.Collection", errorMessages instanceof Collection);

    assertTrue("There must be THREE error messages", ((Collection)errorMessages).size() == 3);
  }
  
  // test that hiding of permanent messages works
  public void testPermanentMessageHiding() throws Exception {
    msgCtx.showPermanentMessage(MessageContext.ERROR_TYPE, "permanent message");
    msgCtx.showMessage(MessageContext.ERROR_TYPE, "one-time message");
    msgCtx.showErrorMessage("Another error message added with defined interface method.");
    
    msgCtx.hidePermanentMessage("permanent message");
    
    ((Widget)msgCtx)._getWidget().render(output);
    
    Object messages = output.getAttribute(MessageContext.MESSAGE_KEY);
    assertTrue("messages must not be null", messages != null);
    assertTrue("Messages must contain ONE elements!", ((Map)messages).size() == 1);
    
    Object errorMessages = ((Map)messages).get(MessageContext.ERROR_TYPE);
    assertTrue("Messages must be in java.util.Collection", errorMessages instanceof Collection);

    assertTrue("There must be TWO error messages", ((Collection)errorMessages).size() == 2);
  }

  // Dummy OutputData which popAttribute() does not pop values, so that after 
  // calling render(OutputData) it is possible to check what went into it.
  private static class MockOutputData implements OutputData {
    private Map dataMap = new HashMap();

	public Object narrow(Class interfaceClass) { return null; }
	public InputData getInputData() { return null; }
	public Path getScope() { return null; }
	public void popScope() {}
	public void pushScope(Object step) {}
	public void restoreScope(Path scope) {}
	public void extend(Class interfaceClass, Object extension) {}
	
	/* Interesting for the test are these four. */
	public Object getAttribute(Object key) { return dataMap.get(key); }
	public Map getAttributes() { return UnmodifiableMap.decorate(dataMap); }
	public Object popAttribute(Object key) { return dataMap.get(key); } // do not pop :)
	public void pushAttribute(Object key, Object value) {
      dataMap.put(key, value);
	}
  }
}
