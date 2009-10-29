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

package org.araneaframework.example.main.business.model;

import java.math.BigDecimal;

/**
 * @author Rein Raudjärv <rein@webmedia.ee>
 * 
 * @hibernate.class table="contract" lazy="false"
 */
public class ContractMO implements GeneralMO {

  private Long id;

  private CompanyMO company;

  private PersonMO person;

  private String notes;

  private BigDecimal total;

  /**
   * @hibernate.id column="id" generator-class="increment"
   */
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * @hibernate.many-to-one column="COMPANY_ID" not-null="true"
   */
  public CompanyMO getCompany() {
    return company;
  }

  public void setCompany(CompanyMO company) {
    this.company = company;
  }

  /**
   * @hibernate.many-to-one column="PERSON_ID" not-null="true"
   */
  public PersonMO getPerson() {
    return person;
  }

  public void setPerson(PersonMO person) {
    this.person = person;
  }

  /**
   * @hibernate.property column="NOTES"
   */
  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  /**
   * @hibernate.property column="TOTAL"
   */
  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
