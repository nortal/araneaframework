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

package org.araneaframework.tests;

import org.araneaframework.uilib.support.DataType;

import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.tests.mock.MockConfiguration;
import org.araneaframework.uilib.ConverterNotFoundException;
import org.araneaframework.uilib.form.converter.ConverterFactory;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class FormConverterTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(FormConverterTest.class);

  public void testConverterNotFound() throws Exception {

    boolean exceptionThrown = false;

    try {
      ConverterFactory.getInstance(new MockConfiguration()).findConverter(new DataType(FormConverterTest.class),
          new DataType(Exception.class));
    } catch (ConverterNotFoundException e) {
      LOG.debug(e.getMessage());
      exceptionThrown = true;
    }

    assertTrue("ConverterFactory must throw a ConverterNotFoundException if Converter cannot be found.",
        exceptionThrown);
  }

}
