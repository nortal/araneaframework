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

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.araneaframework.tests.mock.MockConfiguration;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.converter.ConverterFactory;


/**
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class FormConverterTest extends TestCase {
  
  private static Logger log = Logger.getLogger(FormConverterTest.class);
  
  public void testConverterNotFound() throws Exception {    
    
    boolean exceptionThrown = false;
    
    try {
      ConverterFactory.getInstance(new MockConfiguration()).findConverter(
          "unexistingUniqueRandomType1", 
          "unexistingUniqueRandomType2",
          new MockEnvironment());
    }
    catch (ConverterNotFoundException e) {
      log.debug(e.getMessage());
      exceptionThrown = true;
    }
    
    assertTrue("ConverterFactory must throw a ConverterNotFoundException if Converter cannot be found.", exceptionThrown);
  }
  
}
