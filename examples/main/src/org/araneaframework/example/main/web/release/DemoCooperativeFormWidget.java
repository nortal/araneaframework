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

package org.araneaframework.example.main.web.release;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.release.model.ExampleData;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DefaultSelectControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class DemoCooperativeFormWidget extends TemplateBaseWidget {

  private BeanFormWidget<Firm> companyForm;

  private BeanFormWidget<Invoice> invoiceForm;

  private Map<Long, Firm> firms = new HashMap<Long, Firm>();

  static List<String> allCountries = new ArrayList<String>();

  static {
    allCountries = ExampleData.getCountries(Locale.ENGLISH);
  }

  private BeanFormWidget<Invoice> buildInvoiceForm() {
    BeanFormWidget<Invoice> result = new BeanFormWidget<Invoice>(Invoice.class);
    result.addBeanElement("id", "cooperativeForm.invoice.id", new TextControl(10L, 10L), true);
    result.addBeanElement("date", "cooperativeForm.invoice.date", new DateControl(), true);
    result.addBeanElement("sum", "cooperativeForm.invoice.sum", new FloatControl(), true);
    return result;
  }

  private BeanFormWidget<Firm> buildCompanyForm() {
    BeanFormWidget<Firm> result = new BeanFormWidget<Firm>(Firm.class);
    result.addElement("arkNumber", "cooperativeForm.centralfirmid", new NumberControl(), new LongData(), true);
    result.addBeanElement("registryAddress", "cooperativeForm.registryaddress", new TextControl());
    result.addBeanElement("postalAddress", "cooperativeForm.postaladdress", new TextControl());
    result.addBeanElement("bankAccount", "cooperativeForm.bankaccount", new TextControl());
    result.addBeanElement("vatNumber", "cooperativeForm.vatidentifier", new NumberControl());
    result.addBeanElement("firmType", "cooperativeForm.firmtype", buildFirmTypeSelect());
    return result;
  }

  private Firm generateFirm(Long arkNumber) {
    Firm result = new Firm();
    result.setArkNumber(arkNumber);

    Random rnd = new Random();

    result.setBankAccount(RandomStringUtils.randomNumeric(16));
    String registryCountry = allCountries.get(rnd.nextInt(allCountries.size()));
    String postalCountry = allCountries.get(rnd.nextInt(allCountries.size()));
    String registryStreet = ExampleData.fungi[rnd.nextInt(ExampleData.fungi.length)];
    String postalStreet = ExampleData.fungi[rnd.nextInt(ExampleData.fungi.length)];
    String registryhouse = String.valueOf(rnd.nextInt(500));
    String postalhouse = String.valueOf(rnd.nextInt(200));

    result.setRegistryAddress(registryStreet + "-" + registryhouse + ", " + registryCountry);
    result.setPostalAddress(postalStreet + "-" + postalhouse + ", " + postalCountry);
    result.setVatNumber(Math.abs(rnd.nextLong()));
    result.setFirmType(rnd.nextInt(3) + 1);

    return result;
  }

  private SelectControl<DisplayItem> buildFirmTypeSelect() {
    DefaultSelectControl result = new DefaultSelectControl();
    result.addItem("select.choose", "0");
    result.addItem("cooperativeForm.ltd.public", "1");
    result.addItem("cooperativeForm.npo", "2");
    result.addItem("cooperativeForm.ltd.private", "3");
    return result;
  }

  @Override
  protected void init() throws Exception {
    setViewSelector("release/demos/friendlyUpdateDemo");
    this.companyForm = buildCompanyForm();
    this.invoiceForm = buildInvoiceForm();
    addWidget("companyForm", this.companyForm);
    addWidget("invoiceForm", this.invoiceForm);
  }

  public void handleEventFetchData() throws Exception {
    if (this.companyForm.convertAndValidate()) {
      Thread.sleep(6000);
      Firm firm = this.companyForm.writeToBean();

      // present ?
      Firm present = this.firms.get(firm.getArkNumber());

      if (present == null) {
        present = generateFirm(firm.getArkNumber());
      }

      this.firms.put(present.getArkNumber(), present);
      this.companyForm.readFromBean(present);
    }
  }

  public void handleEventResetCompanyForm() throws Exception {
    this.companyForm.readFromBean(new Firm());
  }

  public static class Firm implements Serializable {

    private Long arkNumber;

    private String registryAddress;

    private String postalAddress;

    private String bankAccount;

    private Long vatNumber;

    private Integer firmType;

    public Long getArkNumber() {
      return this.arkNumber;
    }

    public void setArkNumber(Long arkNumber) {
      this.arkNumber = arkNumber;
    }

    public String getRegistryAddress() {
      return this.registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
      this.registryAddress = registryAddress;
    }

    public String getPostalAddress() {
      return this.postalAddress;
    }

    public void setPostalAddress(String postalAddress) {
      this.postalAddress = postalAddress;
    }

    public String getBankAccount() {
      return this.bankAccount;
    }

    public void setBankAccount(String bankAccount) {
      this.bankAccount = bankAccount;
    }

    public Long getVatNumber() {
      return this.vatNumber;
    }

    public void setVatNumber(Long vatNumber) {
      this.vatNumber = vatNumber;
    }

    public Integer getFirmType() {
      return this.firmType;
    }

    public void setFirmType(Integer firmType) {
      this.firmType = firmType;
    }
  }

  public static class Invoice implements Serializable {

    private String id;

    private Date date;

    private BigDecimal sum;

    public String getId() {
      return this.id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public Date getDate() {
      return this.date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    public BigDecimal getSum() {
      return this.sum;
    }

    public void setSum(BigDecimal sum) {
      this.sum = sum;
    }
  }
}
