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

package org.araneaframework.example.main.business.util;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.araneaframework.backend.BaseBean;
import org.araneaframework.backend.util.BeanMapper;


/**
 * This VO is used by tests only ({@link org.araneaframework.uilib.tests.ListTest},
 * {@link org.araneaframework.uilib.tests.FormTest},{@link org.araneaframework.uilib.tests.VoProcessingTest}.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public class TestVO extends BaseBean implements Serializable, Cloneable {

  private static Logger log = Logger.getLogger(TestVO.class);

  /**
   * Private VoMapper, used for <code>toString</code> and <code>equals</code> methods.
   */
  private BeanMapper voMapper;
  private Long id;
  private String stringValue;
  private Boolean booleanValue;
  private Long longValue;
  private TestVO subTestVO;

  public TestVO() {
    voMapper = new BeanMapper(getClass());
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
}
