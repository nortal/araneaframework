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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author Rein Raudjärv <rein@webmedia.ee>
 */
@Entity(name = "contract")
public class ContractMO implements GeneralMO {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private CompanyMO company;

  @ManyToOne(optional = false)
  private PersonMO person;

  private String notes;

  private BigDecimal total;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CompanyMO getCompany() {
    return this.company;
  }

  public void setCompany(CompanyMO company) {
    this.company = company;
  }

  public PersonMO getPerson() {
    return this.person;
  }

  public void setPerson(PersonMO person) {
    this.person = person;
  }

  public String getNotes() {
    return this.notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public BigDecimal getTotal() {
    return this.total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }
}
