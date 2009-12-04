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
 * This is just a sample data object that can be used in lists, forms, etc.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class DataDTO implements Serializable {

  private Long id;

  private Boolean booleanField;

  private Long longField;

  private String stringField;

  public DataDTO() {}

  public DataDTO(Long id, Boolean booleanField, Long longField, String stringField) {
    this.id = id;
    this.booleanField = booleanField;
    this.longField = longField;
    this.stringField = stringField;
  }

  public Boolean getBooleanField() {
    return booleanField;
  }

  public void setBooleanField(Boolean booleanField) {
    this.booleanField = booleanField;
  }

  public Long getLongField() {
    return longField;
  }

  public void setLongField(Long longField) {
    this.longField = longField;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
