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

package org.araneaframework.tests.mock;

import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.backend.util.BeanMapper;

/**
 * This VO is used by tests only ({@link org.araneaframework.uilib.tests.ListTest},
 * {@link org.araneaframework.uilib.tests.FormTest},{@link org.araneaframework.uilib.tests.VoProcessingTest}.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class TestVO implements Serializable, Cloneable {

  private static final Log LOG = LogFactory.getLog(TestVO.class);

  /**
   * Private VoMapper, used for <code>toString</code> and <code>equals</code> methods.
   */
  private BeanMapper<TestVO> beanMapper;

  private Long id;

  private String stringValue;

  private Boolean booleanValue;

  private Long longValue;

  private TestVO subTestVO;

  public TestVO() {
    this.beanMapper = new BeanMapper<TestVO>(TestVO.class);
  }

  /**
   * Returns id.
   * 
   * @return the id.
   */
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * <code>Boolean</code> value.
   */
  public Boolean getBooleanValue() {
    return this.booleanValue;
  }

  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * <code>Long</code> value.
   */
  public Long getLongValue() {
    return this.longValue;
  }

  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }

  /**
   * <code>String</code> value.
   */
  public String getStringValue() {
    return this.stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Included <code>TestVO</code> for hierarchy tests.
   */
  public TestVO getSubTestVO() {
    return this.subTestVO;
  }

  public void setSubTestVO(TestVO subTestVO) {
    this.subTestVO = subTestVO;
  }

  // *******************************************************************
  // PUBLIC METHODS
  // *******************************************************************

  /**
   * Overrides default <code>toString</code> method.
   * <P/>
   * 
   * @return <code>java.lang.String</code> representation of this value object.
   */
  @Override
  public String toString() {
    StringBuffer result = new StringBuffer();
    for (String field : this.beanMapper.getPropertyNames()) {
      result.append(field);
      result.append("=");
      result.append("" + this.beanMapper.getProperty(this, field));
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
    // In case other VO is null, or of other class.
    if (otherVo == null || (!this.getClass().equals(otherVo.getClass()))) {
      return false;
    }

    // Otherwise compare all fields
    boolean result = true;
    for (String field : this.beanMapper.getPropertyNames()) {
      result = valuesEqual(this.beanMapper.getProperty(this, field), this.beanMapper.getProperty(otherVo, field));
    }
    return result;
  }

  /**
   * Implements hash using Value Object fields.
   */
  @Override
  public int hashCode() {
    int result = 17;
    for (String field : this.beanMapper.getPropertyNames()) {
      result = 37 * result + this.beanMapper.getProperty(this, field).hashCode();
    }
    return result;
  }

  /**
   * Overrides default <code>clone()</code> operation.
   * <P/>
   * 
   * @return clone of this object.
   */
  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      LOG.warn("TestVO threw CloneNotSupportedException, check that everything is defined Cloneable");
      return null;
    }
  }

  // *******************************************************************
  // PRIVATE HELPER METHODS
  // *******************************************************************

  /**
   * Checks for object equality.
   */
  private boolean valuesEqual(Object value1, Object value2) {
    return ObjectUtils.equals(value1, value2);
  }

}
