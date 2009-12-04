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

package org.araneaframework.tests.integration;

//import org.araneaframework.InputData;
//import org.araneaframework.core.EventListener;
//import org.araneaframework.framework.container.StandardFlowContainerWidget;
//import org.araneaframework.core.ApplicationWidget;
//import org.araneaframework.framework.TransactionContext;
//import junit.framework.TestSuite;
//import junit.framework.Test;
//import org.araneaframework.mock.core.MockEventfulStandardWidget;
//import org.araneaframework.framework.filter.StandardTransactionFilterWidget;
//import org.springframework.beans.factory.BeanFactory;
//import org.springframework.mock.web.MockServletContext;
//import org.springframework.mock.web.MockServletConfig;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.mock.web.MockHttpServletRequest;
//import java.util.HashMap;
//import java.util.Map;
//import org.araneaframework.http.ServletServiceAdapterComponent;
//import org.araneaframework.mock.servlet.MockServlet;
import junit.framework.TestCase;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * TODO: this class contains some really legacy code, it must be updated.
 */
public class SmokeTests extends TestCase {
//  private MockServlet servlet;
//  private ServletServiceAdapterComponent adapter;
//  
//  private Map initedAdapters = new HashMap();
//    
//  private MockHttpServletRequest req;
//  private MockHttpServletResponse resp;
//  
//  public SmokeTests(String name) {
//    super(name);
//  }
//  
//  public ServletServiceAdapterComponent initAdapter(String configFile) throws Exception {
//    
//    if (servlet == null ) {
//      servlet  = new MockServlet();
//    }
//    
//    ServletServiceAdapterComponent comp = 
//      (ServletServiceAdapterComponent)initedAdapters.get(configFile);
//    
//    if (comp == null) {
//      servlet.setConfFile(configFile);
//      servlet.init(new MockServletConfig(new MockServletContext()));
//      
//      initedAdapters.put(configFile, comp);
//      
//      return servlet.getBuiltComponent();
//    }
//    
//    return comp;
//  }
//  
//  public void setUp() throws Exception {
//    servlet  = new MockServlet();
//    
//    req = new MockHttpServletRequest();
//    resp = new MockHttpServletResponse();
//  }
//  
//  public void testSmoke() throws Exception {
//    adapter = initAdapter("smokeTest.xml");
//    adapter.service(req, resp);
//    //success if no exception thrown
//    fail();
//  }
//  
//  public void testSerialization() throws Exception {
//    adapter = initAdapter("serializationTestsConf.xml");
//    
//    BeanFactory factory = servlet.getFactory();
//    MockViewPortWidget widget = (MockViewPortWidget)factory.getBean("rootWidget");
//    widget.setChild(new MockEventfulStandardWidget());
//    adapter.service(req, resp);
//    //success if no exception thrown
//  }
//  
//  public void testRepeatedRequestCatching() throws Exception {
//    String event = "theEvent";
//    String widgetKey =StandardViewPortWidget.CHILD_KEY;
//    
//    adapter = initAdapter("repeatedRequest.xml");
//    // lets get reference to the bean doing the heavy-lifting
//    // its a singleton, so we're cool
//    BeanFactory factory = servlet.getFactory();
//    MockViewPortWidget widget = (MockViewPortWidget)factory.getBean("rootWidget");
//    
//    req.addParameter(ApplicationWidget.EVENT_HANDLER_ID_KEY, event);
//    req.addParameter(ApplicationWidget.EVENT_PATH_KEY, widgetKey);
//    
//    // first request, transactionId will get intialized
//    adapter.service(req, resp);
//    // helper returns true on null transactionId
//    
//    MockEventfulStandardWidget child1 = 
//      (MockEventfulStandardWidget)widget.getChildren().get(widgetKey);
//    req.addParameter(TransactionContext.TRANSACTION_ID_KEY, ""+child1.getTransactionId());
//    
//    // second request with the valid transactionId
//    adapter.service(req, resp);
//
//    assertTrue(child1.getEventProcessed());
//    child1.setEventProcessed(false);
//    
//    req.addParameter(TransactionContext.TRANSACTION_ID_KEY, ""+child1.getTransactionId());    
//    adapter.service(req, resp);
//    // transactionId used 2nd time, should not process the event
//    assertFalse(child1.getEventProcessed());
//  }
//  
//  public void testDestroyPropagates() throws Exception {
//    adapter = initAdapter("repeatedRequest.xml");
//    
//    MockViewPortWidget widget = (MockViewPortWidget)servlet.getFactory().getBean("rootWidget");
//    // initializes everything
//    adapter.service(req, resp);
//    adapter._getComponent().destroy();
//    
//    MockEventfulStandardWidget child = (MockEventfulStandardWidget)widget.getChildren().get(StandardViewPortWidget.CHILD_KEY);
//    assertTrue(child.getDestroyCalled());
//  }
//    
//  public void testRequestRoutingComposite() throws Exception {
//    
//    String childKey = "aWidget";
//    String event = "tehEvent";
//    
//    adapter = initAdapter("simpleFilters.xml");
//    
//    MockEventfulStandardWidget widget = (MockEventfulStandardWidget)servlet.getFactory().getBean("rootWidget");
//    MockEventfulStandardWidget childWidget = new MockEventfulStandardWidget();
//    childWidget.addEventListener(event, new EventListener() {
//      private static final long serialVersionUID = 1L;
//      public void processEvent(String eventId, InputData input)
//          throws Exception {}
//    });
//
//    widget.addWidget(childKey, childWidget);
//    
//    req.addParameter(ApplicationWidget.EVENT_HANDLER_ID_KEY, event);
//    req.addParameter(ApplicationWidget.EVENT_PATH_KEY, childKey);    
//    // initializes everything
//    adapter.service(req, resp);
//    adapter.service(req, resp);
//    
//    assertTrue(((MockEventfulStandardWidget)widget.getChildren().get(childKey)).getEventProcessed());
//  }
//  
//  public static Test suite() {
//    return new TestSuite(SmokeTests.class);
//  }
//
//  public static void main(String args[]) {
//    junit.textui.TestRunner.run(suite());
//  }
}
