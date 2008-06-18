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

package org.araneaframework.tests.mock;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.util.BeanMapper;


/**
 * This VO is used by tests only ({@link org.araneaframework.uilib.tests.ListTest},
 * {@link org.araneaframework.uilib.tests.FormTest},{@link org.araneaframework.uilib.tests.VoProcessingTest}.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class TestVO implements Serializable, Cloneable {

  private static final Log log = LogFactory.getLog(TestVO.class);

  /**
   * Private VoMapper, used for <code>toString</code> and <code>equals</code> methods.
   */
  private BeanMapper beanMapper;
  private Long id;
  private String stringValue;
  private Boolean booleanValue;
  private Long longValue;
  private TestVO subTestVO;

  public TestVO() {
    beanMapper = new BeanMapper(getClass());
  }
  
  /**
   * Returns id.
   * 
   * @return the id.
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * <code>Boolean</code> value.
   */
  public Boolean getBooleanValue() {
    return booleanValue;
  }

  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * <code>Long</code> value.
   */
  public Long getLongValue() {
    return longValue;
  }

  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }

  /**
   * <code>String</code> value.
   */
  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Included <code>TestVO</code> for hierarchy tests.
   */
  public TestVO getSubTestVO() {
    return subTestVO;
  }

  public void setSubTestVO(TestVO subTestVO) {
    this.subTestVO = subTestVO;
  }

  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************
  
  /**
   * Overrides default <code>toString</code> method. <P/>
   * 
   * @return <code>java.lang.String</code> representation of this value object.
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();
    List voFields = beanMapper.getFields();
    for (Iterator i = voFields.iterator(); i.hasNext();) {
      String field = (String) i.next();
      result.append(field);
      result.append("=");
      result.append("" + beanMapper.getFieldValue(this, field));
      result.append("; ");
    }
    return result.toString();
  }

  /**
   * Compares Value Object for equality by their fields.
   * 
   * @param otherVo Value Object to compare to.
   * @return <code>boolean</code>- if Value Object are equal.
   */
  @Override
  public boolean equals(Object otherVo) {
    //In case other VO is null, or of other class.
    if (otherVo == null || (!this.getClass().equals(otherVo.getClass()))) { return false; }

    //Otherwise compare all fields
    boolean result = true;
    List voFields = beanMapper.getFields();
    for (Iterator i = voFields.iterator(); i.hasNext() && result;) {
      String field = (String) i.next();
      result = valuesEqual(beanMapper.getFieldValue(this, field), beanMapper.getFieldValue(otherVo,
          field));
    }
    return result;
  }

  /**
   * Implements hash using Value Object fields.
   */
  @Override
  public int hashCode() {
    int result = 17;
    List voFields = beanMapper.getFields();
    for (Iterator i = voFields.iterator(); i.hasNext();) {
      String field = (String) i.next();
      result = 37 * result + beanMapper.getFieldValue(this, field).hashCode();
    }
    return result;
  }

  /**
   * Overrides default <code>clone()</code> operation. <P/>
   * 
   * @return clone of this object.
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    }
    catch (CloneNotSupportedException e) {
      log.warn("TestVO threw CloneNotSupportedException, check that everything is defined Cloneable");
      return null;
    }
  }

  //*******************************************************************
  // PRIVATE HELPER METHODS
  //*******************************************************************

  /**
   * Checks for object equality.
   */
  private boolean valuesEqual(Object value1, Object value2) {
    return (value1 == null ? value2 == null : value1.equals(value2));
  }

}
