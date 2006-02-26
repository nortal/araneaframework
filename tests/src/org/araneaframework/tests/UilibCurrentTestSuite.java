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
import org.apache.log4j.Logger;

/**
 * This is test suite which contains all tests that have to be run and controlled
 * during current development cycle.
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
* @see "Junit testframework"
* @see <a href="http://www.junit.org">www.junit.org</a> */
public class UilibCurrentTestSuite extends TestSuite {
	
	private static Logger log = Logger.getLogger(UilibCurrentTestSuite.class);
	
	/**
	 * Creates new instance of <code>GeneralCurrentTestSuite</code>.
	 */
	public UilibCurrentTestSuite() {
	}
	/**
	 * Creates new suite to be tested. The tester must add all tests to be run here.
	 * While this file is in the baseline, the method returns empty suite.
	 */
	public static Test suite(){
	  return UilibAllTestSuite.getAllTests();
      //return new FormConstraintTest("testFormOptionalConstraint");
	}
}

