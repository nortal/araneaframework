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

package org.araneaframework.example.main.business.util;

import java.io.Serializable;

/**
 * This value object is used in some tests ({@link org.araneaframework.uilib.tests.ListTest},
 * {@link org.araneaframework.uilib.tests.FormTest},{@link org.araneaframework.uilib.tests.VoProcessingTest}) for the
 * purpose of testing working with different data types. Therefore, this object has properties of different types.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DemoVO implements Serializable, Cloneable {

  private Long id;

  private String stringValue;

  private Boolean booleanValue;

  private Long longValue;

  private DemoVO subTestVO;

  /**
   * Provides the ID of this object.
   * 
   * @return The ID of this object.
   */
  public Long getId() {
    return this.id;
  }

  /**
   * Sets the ID of this object.
   * 
   * @param id The ID of this object.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Provides the Boolean value of this object.
   * 
   * @return The Boolean value of this object.
   */
  public Boolean getBooleanValue() {
    return this.booleanValue;
  }

  /**
   * Sets the Boolean value of this object.
   * 
   * @param booleanValue The Boolean value of this object.
   */
  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * Provides the Long value of this object.
   * 
   * @return The Long value of this object.
   */
  public Long getLongValue() {
    return this.longValue;
  }

  /**
   * Sets the Long value of this object.
   * 
   * @param longValue The Long value of this object.
   */
  public void setLongValue(Long longValue) {
    this.longValue = longValue;
  }

  /**
   * Provides the String value of this object.
   * 
   * @return The String value of this object.
   */
  public String getStringValue() {
    return this.stringValue;
  }

  /**
   * Sets the String value of this object.
   * 
   * @param stringValue The String value of this object.
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Provides the sub value object of this object. Used for hierarchy tests.
   * 
   * @return The sub value object of this object.
   */
  public DemoVO getSubTestVO() {
    return this.subTestVO;
  }

  /**
   * Sets the sub value object of this object.
   * 
   * @param stringValue The sub value object of this object.
   */
  public void setSubTestVO(DemoVO subTestVO) {
    this.subTestVO = subTestVO;
  }
}
