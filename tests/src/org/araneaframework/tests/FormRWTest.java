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

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
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
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class FormRWTest extends TestCase {

  private static Logger log = Logger.getLogger(FormRWTest.class);

  private FormWidget makeVoForm() throws Exception {

    //Creating form :-)
    FormWidget voForm = new FormWidget();
    voForm._getComponent().init(new MockEnvironment());
    
    //Adding elements to form
    voForm.addElement("booleanValue", "vo checkbox", new CheckboxControl(), new BooleanData(), true);
    voForm.addElement("stringValue", "vo text", new TextControl(), new StringData(), true);
    voForm.addElement("longValue", "vo long", new TextControl(), new LongData(), true);

    //Adding a composite element
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

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);

    voForm.setValueByFullName("booleanValue", Boolean.TRUE);
    voForm.setValueByFullName("stringValue", LIFE_IS_BEAUTIFUL);
    voForm.setValueByFullName("longValue", TEN);

    BeanFormReader voReader = new BeanFormReader(voForm);

    voReader.readFormBean(test);

    log.debug("Value Object after reading from form" + test);

    assertTrue("Boolean value read properly", test.getBooleanValue().equals(Boolean.TRUE));
    assertTrue("String value read properly", test.getStringValue().equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value read properly", test.getLongValue().equals(TEN));
  }

  /**
   * Tests hierarchical Value Object reading.
   */
  public void testFormHierarchicalVoReading() throws Exception {

    FormWidget voForm = makeVoForm();
    TestVO test = new TestVO();

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);   

    voForm.setValueByFullName("subTestVO.booleanValue", Boolean.TRUE);
    voForm.setValueByFullName("subTestVO.stringValue", LIFE_IS_BEAUTIFUL);
    voForm.setValueByFullName("subTestVO.longValue", TEN);

    BeanFormReader voReader = new BeanFormReader(voForm);

    voReader.readFormBean(test);

    log.debug("Value Object after reading from form");

    assertTrue("Boolean value read properly", test.getSubTestVO().getBooleanValue().equals(Boolean.TRUE));
    assertTrue("String value read properly", test.getSubTestVO().getStringValue().equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value read properly", test.getSubTestVO().getLongValue().equals(TEN));
  }

  /**
   * Tests basic Value Object writing.
   */
  public void testFormBasicVoWriting() throws Exception {

    FormWidget voForm = makeVoForm();
    TestVO test = new TestVO();

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);

    test.setBooleanValue(Boolean.TRUE);
    test.setStringValue(LIFE_IS_BEAUTIFUL);
    test.setLongValue(TEN);

    BeanFormWriter voWriter = new BeanFormWriter(test.getClass());

    voWriter.writeFormBean(voForm, test);

    assertTrue("Boolean value written properly", voForm.getValueByFullName("booleanValue").equals(Boolean.TRUE));
    assertTrue("String value written properly", voForm.getValueByFullName("stringValue").equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value written properly", voForm.getValueByFullName("longValue").equals(TEN));
  }

  /**
   * Tests hierarchical Value Object writing.
   */
  public void testFormHierarchicalVoWriting() throws Exception {

    FormWidget voForm = makeVoForm();
    TestVO test = new TestVO();

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);

    test.setSubTestVO(new TestVO());

    test.getSubTestVO().setBooleanValue(Boolean.TRUE);
    test.getSubTestVO().setStringValue(LIFE_IS_BEAUTIFUL);
    test.getSubTestVO().setLongValue(TEN);

    BeanFormWriter voWriter = new BeanFormWriter(test.getClass());

    voWriter.writeFormBean(voForm, test);

    assertTrue("Boolean value written properly", voForm.getValueByFullName("subTestVO.booleanValue").equals(Boolean.TRUE));
    assertTrue("String value written properly", voForm.getValueByFullName("subTestVO.stringValue").equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value written properly", voForm.getValueByFullName("subTestVO.longValue").equals(TEN));  }
  
  /**
   * Tests basic Map reading.
   */
  public void testFormBasicMapReading() throws Exception {

    FormWidget mapForm = makeVoForm();    

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);

    mapForm.setValueByFullName("booleanValue", Boolean.TRUE);
    mapForm.setValueByFullName("stringValue", LIFE_IS_BEAUTIFUL);
    mapForm.setValueByFullName("longValue", TEN);

    MapFormReader mapReader = new MapFormReader(mapForm);

    Map readMap = mapReader.getMap();

    log.debug("Map read from form: " + readMap);

    assertTrue("Boolean value read properly", readMap.get("booleanValue").equals(Boolean.TRUE));
    assertTrue("String value read properly", readMap.get("stringValue").equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value read properly", readMap.get("longValue").equals(TEN));
  }  
  
  /**
   * Tests hierarchical Map reading.
   */
  public void testFormHierarchicalMapReading() throws Exception {

    FormWidget mapForm = makeVoForm();    

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);

    mapForm.setValueByFullName("subTestVO.booleanValue", Boolean.TRUE);
    mapForm.setValueByFullName("subTestVO.stringValue", LIFE_IS_BEAUTIFUL);
    mapForm.setValueByFullName("subTestVO.longValue", TEN);
    
    MapFormReader mapReader = new MapFormReader(mapForm);

    Map readMap = mapReader.getMap();

    log.debug("Map read from form: " + readMap);
    
    Map subTestVoMap = (Map) readMap.get("subTestVO");
    assertNull("Boolean value read properly", readMap.get("booleanValue"));
    assertNull("String value read properly", readMap.get("stringValue"));
    assertNull("Long value read properly", readMap.get("longValue"));
    
    assertTrue("Boolean value read properly", subTestVoMap.get("booleanValue").equals(Boolean.TRUE));
    assertTrue("String value read properly", subTestVoMap.get("stringValue").equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value read properly", subTestVoMap.get("longValue").equals(TEN));
  }    
  
  /**
   * Tests basic Map writing.
   */
  public void testFormBasicMapWriting() throws Exception {

    FormWidget mapForm = makeVoForm();
    Map testMap = new HashMap();

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);

    testMap.put("booleanValue", Boolean.TRUE);
    testMap.put("stringValue", LIFE_IS_BEAUTIFUL);
    testMap.put("longValue", TEN);

    MapFormWriter mapWriter = new MapFormWriter();

    mapWriter.writeForm(mapForm, testMap);

    assertTrue("Boolean value written properly", mapForm.getValueByFullName("booleanValue").equals(Boolean.TRUE));
    assertTrue("String value written properly", mapForm.getValueByFullName("stringValue").equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value written properly", mapForm.getValueByFullName("longValue").equals(TEN));
  }  
  
  /**
   * Tests hierarchical Map writing.
   */
  public void testFormHierarchicalMapWriting() throws Exception {

    FormWidget mapForm = makeVoForm();
    Map testMap = new HashMap();

    final String LIFE_IS_BEAUTIFUL = "Life is beautiful";
    final Long TEN = new Long(10);

    testMap.put("booleanValue", Boolean.TRUE);
    testMap.put("stringValue", LIFE_IS_BEAUTIFUL);
    testMap.put("longValue", TEN);
    
    
    Map topTestMap = new HashMap();
    topTestMap.put("subTestVO", testMap);    

    MapFormWriter mapWriter = new MapFormWriter();

    mapWriter.writeForm(mapForm, topTestMap);

    assertTrue("Boolean value written properly", mapForm.getValueByFullName("subTestVO.booleanValue").equals(Boolean.TRUE));
    assertTrue("String value written properly", mapForm.getValueByFullName("subTestVO.stringValue").equals(LIFE_IS_BEAUTIFUL));
    assertTrue("Long value written properly", mapForm.getValueByFullName("subTestVO.longValue").equals(TEN));
  }    
    
}
