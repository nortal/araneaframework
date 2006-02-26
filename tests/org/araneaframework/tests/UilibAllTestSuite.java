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

package org.araneaframework.tests;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * This is just a wrapper class, that has a static function, which returns all 
 * of the tests in the current module. 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class UilibAllTestSuite extends TestSuite  {	
  /**
   * Returns all tests in current module. Can be used to copy the required tests.
   * @return all tests in current module.
   */
	public static Test getAllTests(){
		TestSuite test = new TestSuite();
    /*test.addTestSuite(FormTest.class);
    test.addTestSuite(ListTest.class);
    test.addTestSuite(VoProcessingTest.class);
    test.addTestSuite(FormControlTest.class);
    test.addTestSuite(FormElementTest.class);
    test.addTestSuite(FormConverterTest.class);
    test.addTestSuite(FormConstraintTest.class);
    test.addTestSuite(WidgetTest.class);
    test.addTestSuite(FormRWTest.class);  */ 
		return test;
	}
}

