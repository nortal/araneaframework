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

import java.util.HashMap;
import junit.framework.TestCase;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.core.StandardPath;
import org.araneaframework.framework.filter.StandardTransactionFilterWidget;
import org.araneaframework.mock.core.MockEventfulBaseWidget;
import org.araneaframework.servlet.core.StandardServletInputData;
import org.araneaframework.servlet.core.StandardServletOutputData;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardTransactionFilterWidgetTests extends TestCase {
  private StandardTransactionFilterWidget trans;
  private MockEventfulBaseWidget child;
  private MockHttpServletResponse resp;
  private MockHttpServletRequest req;
  private StandardServletOutputData output;
  
  public void setUp() throws Exception {
    child = new MockEventfulBaseWidget();
    trans = new StandardTransactionFilterWidget();
    trans.setChildWidget(child);
    trans._getComponent().init(new StandardEnvironment(null, new HashMap()));
    
    resp = new MockHttpServletResponse();
    req = new MockHttpServletRequest();
    
    output = new StandardServletOutputData(req, resp);
    trans._getWidget().render(output);
  }
  
  public void testConsistentKeyRoutesUpdate() throws Exception {
    Long key = ((Long)trans.getTransactionId());
    
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, key.toString());
    StandardServletInputData input = new StandardServletInputData(req);
    trans._getWidget().update(input);
    
    assertTrue(child.isUpdateCalled());    
  }
  
  public void testInConsistentKeyDoesntRouteUpdate() throws Exception {
    long key = ((Long)trans.getTransactionId()).longValue();
    
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, (++key)+"");
    StandardServletInputData input = new StandardServletInputData(req);
    trans._getWidget().update(input);
    
    trans._getWidget().update(input);
    assertFalse(child.isUpdateCalled());
  }
  
  public void testConsistentKeyRoutesEvent() throws Exception {
    Long key = ((Long)trans.getTransactionId());
    
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, key.toString());
    StandardServletInputData input = new StandardServletInputData(req);
    trans._getWidget().update(input);
    
    trans._getWidget().event(new StandardPath(""), input);
    assertTrue(child.isEventCalled());    
  }
  
  public void testInConsistentKeyDoesntRouteEvent() throws Exception {    
    long key = ((Long)trans.getTransactionId()).longValue();
    
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, (++key)+"");
    StandardServletInputData input = new StandardServletInputData(req);
    trans._getWidget().update(input);
    
    trans._getWidget().event(new StandardPath(""), input);
    assertFalse(child.isEventCalled());
  }
  
  public void testConsistentKeyRoutesProcess() throws Exception {
    Long key = ((Long)trans.getTransactionId());
    
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, key.toString());
    StandardServletInputData input = new StandardServletInputData(req);
    trans._getWidget().update(input);
    
    trans._getWidget().process();
    assertTrue(child.isProcessCalled());    
  }
  
  public void testInConsistentKeyDoesntRouteProcess() throws Exception {    
    long key = ((Long)trans.getTransactionId()).longValue();
    
    req.addParameter(StandardTransactionFilterWidget.TRANSACTION_ID_KEY, (++key)+"");
    StandardServletInputData input = new StandardServletInputData(req);
    trans._getWidget().update(input);
    
    trans._getWidget().process();
    assertFalse(child.isProcessCalled());
  }
  
  public void testDestroyChild() throws Exception {
    trans._getComponent().destroy();
    assertTrue(child.getDestroyCalled());
  }
}
