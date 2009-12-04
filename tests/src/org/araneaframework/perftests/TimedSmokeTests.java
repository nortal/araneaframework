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

package org.araneaframework.perftests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class TimedSmokeTests {
  public static final long toleranceInMillis = 100;
  
  public static Test suite() throws Exception {
    //TestSuite suite = new TestSuite();
    /*
    SmokeTests testCase = new SmokeTests("testSmoke");
    testCase.initAdapter("smokeTest.xml");
    Test timedTest = new TimedTest(testCase, 1000 + toleranceInMillis);
    suite.addTest(timedTest);

    testCase = new SmokeTests("testSerialization");
    testCase.initAdapter("serializationTestsConf.xml");
    timedTest = new TimedTest(testCase, 1000 + toleranceInMillis);
    suite.addTest(timedTest);
    
    testCase = new SmokeTests("testDestroyPropagates");
    testCase.initAdapter("repeatedRequest.xml");
    timedTest = new TimedTest(testCase, 1000 + toleranceInMillis);
    suite.addTest(timedTest);
    
    testCase = new SmokeTests("testRequestRoutingNonComposite");
    testCase.initAdapter("repeatedRequest.xml");
    timedTest = new TimedTest(testCase, 1000 + toleranceInMillis);
    suite.addTest(timedTest);
    
    testCase = new SmokeTests("testRequestRoutingComposite");
    testCase.initAdapter("repeatedRequest.xml");
    timedTest = new TimedTest(testCase, 1000 + toleranceInMillis);
    suite.addTest(timedTest);
    */
    //return suite;
    return new TestSuite();
  }
  
  public static void main(String args[]) throws Exception {
    //junit.textui.TestRunner.run(suite());
  }
}
