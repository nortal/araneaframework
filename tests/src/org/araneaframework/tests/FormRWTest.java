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

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.tests.mock.MockEnvironment;
import org.araneaframework.tests.mock.TestVO;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.form.data.StringData;
import org.araneaframework.uilib.form.reader.BeanFormReader;
import org.araneaframework.uilib.form.reader.BeanFormWriter;
import org.araneaframework.uilib.form.reader.MapFormReader;
import org.araneaframework.uilib.form.reader.MapFormWriter;

/**
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class FormRWTest extends TestCase {

  private static final Log LOG = LogFactory.getLog(FormRWTest.class);

  private static final String LIFE_IS_BEAUTIFUL = "Life is beautiful";

  private static final Long TEN = 10L;

  private static final Boolean TRUE = true;

  private FormWidget makeVoForm() throws Exception {
    // Creating form :-)
    FormWidget voForm = new FormWidget();
    voForm._getComponent().init(null, new MockEnvironment());

    // Adding elements to form
    voForm.addElement("booleanValue", "vo checkbox", new CheckboxControl(), new BooleanData(), true);
    voForm.addElement("stringValue", "vo text", new TextControl(), new StringData(), true);
    voForm.addElement("longValue", "vo long", new TextControl(), new LongData(), true);

    // Adding a composite element
    FormWidget subTestVO = voForm.addSubForm("subTestVO");
    subTestVO.addElement("booleanValue", "vo checkbox", new CheckboxControl(), new BooleanData(), true);
    subTestVO.addElement("stringValue", "vo text", new TextControl(), new StringData(), true);
    subTestVO.addElement("longValue", "vo long", new TextControl(), new LongData(), true);

    return voForm;
  }

  /**
   * Tests basic Value Object reading.
   */
  public void testFormBasicVoReading() throws Exception {
    FormWidget voForm = makeVoForm();
    TestVO test = new TestVO();

    voForm.setValueByFullName("booleanValue", TRUE);
    voForm.setValueByFullName("stringValue", LIFE_IS_BEAUTIFUL);
    voForm.setValueByFullName("longValue", TEN);

    new BeanFormReader(voForm).readFormBean(test);

    LOG.debug("Value Object after reading from form" + test);

    assertEquals("Boolean value read properly", test.getBooleanValue(), TRUE);
    assertEquals("String value read properly", test.getStringValue(), LIFE_IS_BEAUTIFUL);
    assertEquals("Long value read properly", test.getLongValue(), TEN);
  }

  /**
   * Tests hierarchical Value Object reading.
   */
  public void testFormHierarchicalVoReading() throws Exception {
    FormWidget voForm = makeVoForm();
    TestVO test = new TestVO();

    voForm.setValueByFullName("subTestVO.booleanValue", TRUE);
    voForm.setValueByFullName("subTestVO.stringValue", LIFE_IS_BEAUTIFUL);
    voForm.setValueByFullName("subTestVO.longValue", TEN);

    new BeanFormReader(voForm).readFormBean(test);

    LOG.debug("Value Object after reading from form");

    assertEquals("Boolean value read properly", test.getSubTestVO().getBooleanValue(), TRUE);
    assertEquals("String value read properly", test.getSubTestVO().getStringValue(), LIFE_IS_BEAUTIFUL);
    assertEquals("Long value read properly", test.getSubTestVO().getLongValue(), TEN);
  }

  /**
   * Tests basic Value Object writing.
   */
  public void testFormBasicVoWriting() throws Exception {
    FormWidget voForm = makeVoForm();
    TestVO test = new TestVO();

    test.setBooleanValue(TRUE);
    test.setStringValue(LIFE_IS_BEAUTIFUL);
    test.setLongValue(TEN);

    new BeanFormWriter<TestVO>(TestVO.class).writeFormBean(voForm, test);

    assertEquals("Boolean value written properly", voForm.getValueByFullName("booleanValue"), TRUE);
    assertEquals("String value written properly", voForm.getValueByFullName("stringValue"), LIFE_IS_BEAUTIFUL);
    assertEquals("Long value written properly", voForm.getValueByFullName("longValue"), TEN);
  }

  /**
   * Tests hierarchical Value Object writing.
   */
  public void testFormHierarchicalVoWriting() throws Exception {
    FormWidget voForm = makeVoForm();

    TestVO test = new TestVO();
    test.setSubTestVO(new TestVO());
    test.getSubTestVO().setBooleanValue(TRUE);
    test.getSubTestVO().setStringValue(LIFE_IS_BEAUTIFUL);
    test.getSubTestVO().setLongValue(TEN);

    new BeanFormWriter<TestVO>(TestVO.class).writeFormBean(voForm, test);

    assertEquals("Boolean value written properly", voForm.getValueByFullName("subTestVO.booleanValue"), TRUE);
    assertEquals("String value written properly", voForm.getValueByFullName("subTestVO.stringValue"), LIFE_IS_BEAUTIFUL);
    assertEquals("Long value written properly", voForm.getValueByFullName("subTestVO.longValue"), TEN);
  }

  /**
   * Tests basic Map reading.
   */
  public void testFormBasicMapReading() throws Exception {
    FormWidget mapForm = makeVoForm();
    mapForm.setValueByFullName("booleanValue", TRUE);
    mapForm.setValueByFullName("stringValue", LIFE_IS_BEAUTIFUL);
    mapForm.setValueByFullName("longValue", TEN);

    Map<String, Object> readMap = new MapFormReader(mapForm).getMap();

    LOG.debug("Map read from form: " + readMap);

    assertTrue("Boolean value read properly", readMap.get("booleanValue").equals(TRUE));
    assertTrue("String value read properly", readMap.get("stringValue").equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value read properly", readMap.get("longValue").equals(TEN));
  }

  /**
   * Tests hierarchical Map reading.
   */
  @SuppressWarnings("unchecked")
  public void testFormHierarchicalMapReading() throws Exception {
    FormWidget mapForm = makeVoForm();
    mapForm.setValueByFullName("booleanValue", Boolean.FALSE);
    mapForm.setValueByFullName("subTestVO.booleanValue", TRUE);
    mapForm.setValueByFullName("subTestVO.stringValue", LIFE_IS_BEAUTIFUL);
    mapForm.setValueByFullName("subTestVO.longValue", TEN);

    Map<String, Object> readMap = new MapFormReader(mapForm).getMap();

    LOG.debug("Map read from form: " + readMap);

    Map<String, Object> subTestVoMap = (Map<String, Object>) readMap.get("subTestVO");
    assertEquals(Boolean.FALSE, readMap.get("booleanValue"));
    assertNull("String value read properly", readMap.get("stringValue"));
    assertNull("Long value read properly", readMap.get("longValue"));

    assertEquals("Boolean value read properly", subTestVoMap.get("booleanValue"), TRUE);
    assertEquals("String value read properly", subTestVoMap.get("stringValue"), LIFE_IS_BEAUTIFUL);
    assertEquals("Long value read properly", subTestVoMap.get("longValue"), TEN);
  }

  /**
   * Tests basic Map writing.
   */
  public void testFormBasicMapWriting() throws Exception {

    FormWidget mapForm = makeVoForm();
    Map<String, Object> testMap = new HashMap<String, Object>();

    testMap.put("booleanValue", TRUE);
    testMap.put("stringValue", LIFE_IS_BEAUTIFUL);
    testMap.put("longValue", TEN);

    new MapFormWriter().writeForm(mapForm, testMap);

    assertEquals("Boolean value written properly", mapForm.getValueByFullName("booleanValue"), TRUE);
    assertEquals("String value written properly", mapForm.getValueByFullName("stringValue"), LIFE_IS_BEAUTIFUL);
    assertEquals("Long value written properly", mapForm.getValueByFullName("longValue"), TEN);
  }

  /**
   * Tests hierarchical Map writing.
   */
  public void testFormHierarchicalMapWriting() throws Exception {

    FormWidget mapForm = makeVoForm();
    Map<String, Object> testMap = new HashMap<String, Object>();

    testMap.put("booleanValue", TRUE);
    testMap.put("stringValue", LIFE_IS_BEAUTIFUL);
    testMap.put("longValue", TEN);

    Map<String, Object> topTestMap = new HashMap<String, Object>();
    topTestMap.put("subTestVO", testMap);

    new MapFormWriter().writeForm(mapForm, topTestMap);

    assertEquals("Boolean value written properly", mapForm.getValueByFullName("subTestVO.booleanValue"), TRUE);
    assertEquals("String value written properly", mapForm.getValueByFullName("subTestVO.stringValue"),
        LIFE_IS_BEAUTIFUL);
    assertEquals("Long value written properly", mapForm.getValueByFullName("subTestVO.longValue"), TEN);
  }
}
