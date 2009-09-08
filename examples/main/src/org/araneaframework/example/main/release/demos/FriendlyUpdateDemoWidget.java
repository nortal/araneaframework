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

package org.araneaframework.example.main.release.demos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang.RandomStringUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.release.features.ExampleData;
import org.araneaframework.framework.LocalizationContext.LocaleChangeListener;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.control.SelectControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.LongData;
import org.araneaframework.uilib.support.DisplayItem;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FriendlyUpdateDemoWidget extends TemplateBaseWidget implements LocaleChangeListener {
	private BeanFormWidget<Firm> companyForm;
	private BeanFormWidget<Invoice> invoiceForm;
	private FormElement<String,String> firmElement;
	
	static List<String> allCountries = new ArrayList<String>();
	
	static {
		for (Iterator<String> i = Arrays.asList(Locale.getISOCountries()).iterator(); i.hasNext(); ) {
			allCountries.add(new Locale("en", i.next()).getDisplayCountry(Locale.ENGLISH));
		}
	}

	private BeanFormWidget<Invoice> buildInvoiceForm() {
		BeanFormWidget<Invoice> result = new BeanFormWidget<Invoice>(Invoice.class);
		result.addBeanElement("id", "ufriendly.component.invoice.id", new TextControl(new Long(10), new Long(10)), true);
		result.addBeanElement("date", "ufriendly.component.invoice.date", new DateControl(), true);
		result.addBeanElement("sum", "ufriendly.component.invoice.sum", new FloatControl(), true);
		
		return result;
	}

	private BeanFormWidget<Firm> buildCompanyForm() {
		BeanFormWidget<Firm> result = new BeanFormWidget<Firm>(Firm.class);
		result.addElement("arkNumber", "ufriendly.component.centralfirmid", new NumberControl(), new LongData(), true);
		result.addBeanElement("registryAddress", "ufriendly.component.registryaddress", new TextControl(),  false);
		result.addBeanElement("postalAddress", "ufriendly.component.postaladdress", new TextControl(),  false);
		result.addBeanElement("bankAccount", "ufriendly.component.bankaccount", new TextControl(),  false);
		result.addBeanElement("vatNumber", "ufriendly.component.vatidentifier", new NumberControl(), false);
		firmElement = result.addBeanElement("firmType", "ufriendly.component.firmtype", buildFirmTypeSelect(), false);
		
		return result;
	}
	
	private Map<Long, Firm> firms = new HashMap<Long, Firm>();
	private void handleEventFetchData() throws Exception {
		if (companyForm.convertAndValidate()) {
			Thread.sleep(6000);
			Firm firm = new Firm(); 
			firm = companyForm.writeToBean(firm);

			// present ?
			Firm present = firms.get(firm.getArkNumber()); 
			if (present == null) {
				present = generateFirm(firm.getArkNumber());
			}
			
			firms.put(present.getArkNumber(), present);
			companyForm.readFromBean(present);
		}
	}
	
  protected void init() throws Exception {
    setViewSelector("release/demos/friendlyUpdateDemo");
    getL10nCtx().addLocaleChangeListener(this);
    this.companyForm = buildCompanyForm();
    this.invoiceForm = buildInvoiceForm();
    addWidget("companyForm", this.companyForm);
    addWidget("invoiceForm", this.invoiceForm);
  }


  public void handleEventResetCompanyForm() throws Exception {
    this.companyForm.readFromBean(new Firm());
  }

  private Firm generateFirm(Long arkNumber) {
    Firm result = new Firm();
    result.setArkNumber(arkNumber);

    Random rnd = new Random();

    result.setBankAccount(RandomStringUtils.randomNumeric(16));
    String registryCountry = (String) allCountries.get(rnd.nextInt(allCountries.size()));
    String postalCountry = (String) allCountries.get(rnd.nextInt(allCountries.size()));
    String registryStreet = ExampleData.fungi[rnd.nextInt(ExampleData.fungi.length)];
    String postalStreet = ExampleData.fungi[rnd.nextInt(ExampleData.fungi.length)];
    String registryhouse = String.valueOf(rnd.nextInt(500));
    String postalhouse = String.valueOf(rnd.nextInt(200));
    result.setRegistryAddress(registryStreet + "-" + registryhouse + ", "
        + registryCountry);
    result.setPostalAddress(postalStreet + "-" + postalhouse + ", "
        + postalCountry);
    result.setVatNumber(new Long(Math.abs(rnd.nextLong())));
    result.setFirmType(new Integer(rnd.nextInt(3) + 1));
    return result;
  }

  private SelectControl buildFirmTypeSelect() {
    SelectControl result = new SelectControl();
    result.addItem(new DisplayItem("0", t("select.choose")));
    result.addItem(new DisplayItem("1", t("ufriendly.public.limited")));
    result.addItem(new DisplayItem("2", t("ufriendly.npo")));
    result.addItem(new DisplayItem("3", t("ufriendly.private.limited")));
    return result;
  }

  public void onLocaleChange(Locale oldLocale, Locale newLocale) {
    Object value = this.firmElement.getControl().getRawValue();
    SelectControl c = buildFirmTypeSelect();
// TODO    c.setRawValue(value);
    this.firmElement.setControl(c);
  }

  public static class Firm implements Serializable {

    private static final long serialVersionUID = 1L;

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

    private static final long serialVersionUID = 1L;

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
