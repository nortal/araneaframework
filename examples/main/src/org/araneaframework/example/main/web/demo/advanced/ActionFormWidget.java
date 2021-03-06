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

package org.araneaframework.example.main.web.demo.advanced;

import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.lang.StringUtils;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ProxyActionListener;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.IntegerData;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class ActionFormWidget extends TemplateBaseWidget {

  private FormWidget form;
  private FormElement<BigDecimal, BigDecimal> price;
  private FormElement<BigInteger, Integer> quantity;
  private FormElement<BigDecimal, BigDecimal> vat;
  private FormElement<BigDecimal, BigDecimal> total;
  private FormElement<BigDecimal, BigDecimal> vatTotal;
  private FormElement<BigDecimal, BigDecimal> bigTotal;

  @Override
  protected void init() throws Exception {
    setViewSelector("demo/advanced/actionForm");

    this.form = new FormWidget();

    this.price = this.form.addElement("price", "actionForm.price", new FloatControl(), new BigDecimalData());
    this.price.setDisabled(true);
    this.price.setValue(round2(new BigDecimal(Math.random() * 5 + 5)));

    this.quantity = this.form.addElement("quantity", "actionForm.quantity", new NumberControl(), new IntegerData());
    this.quantity.setValue(3);

    this.total = this.form.addElement("total", "actionForm.total", new FloatControl(), new BigDecimalData());
    this.total.setDisabled(true);

    this.vat = this.form.addElement("vat", "actionForm.vat", new FloatControl(), new BigDecimalData());
    this.vat.setValue(new BigDecimal("0.18"));

    this.vatTotal = this.form.addElement("vatTotal", "#not used", new FloatControl(), new BigDecimalData());
    this.vatTotal.setDisabled(true);

    this.bigTotal = this.form.addElement("bigTotal", "actionForm.totalSum", new FloatControl(), new BigDecimalData());
    this.bigTotal.setDisabled(true);

    calculate();
    addWidget("form", this.form);

    addActionListener("quantityChange", new StandardActionListener() {

      @Override
      public void processAction(String actionId, String actionParam,
          InputData input, OutputData output) throws Exception {
        actionParam = StringUtils.substringBefore(prepareNumber(actionParam), ".");
        quantity.setValue(new Integer(actionParam));
        calculate();
        writeFields(output);
      }
    });

    addActionListener("vatChange", new ProxyActionListener(this));
  }

  public void handleActionVatChange(String actionParam) throws Exception {
    this.vat.setValue(round2(new BigDecimal(prepareNumber(actionParam))));
    calculate();
    writeFields(getOutputData());
  }

  private String prepareNumber(String num) {
    final String defNum = "0";
    num = StringUtils.replace(num, ",", "."); // Replace commas with period. Otherwise cannot parse.
    num = StringUtils.replace(num, " ", "");  // Remove all spaces.
    num = StringUtils.defaultIfEmpty(num, defNum);
    return StringUtils.isNumeric(StringUtils.replace(num, ".", "")) ? num :defNum;
  }

  protected void calculate() {
    this.total.setValue(round2(calculateTotal(this.price.getValue(), this.quantity.getValue())));
    this.vatTotal.setValue(round2(calculateVatTotal(this.total.getValue(), this.vat.getValue())));
    this.bigTotal.setValue(round2(calculateBigTotal(this.total.getValue(), this.vatTotal.getValue())));
  }

  protected BigDecimal calculateTotal(BigDecimal price, Integer quantity) {
    return price.multiply(new BigDecimal(quantity.doubleValue()));
  }

  protected BigDecimal calculateVatTotal(BigDecimal total, BigDecimal vat) {
    return total.multiply(vat);
  }

  protected BigDecimal calculateBigTotal(BigDecimal total, BigDecimal vatTotal) {
    return total.add(vatTotal);
  }

  protected void writeFields(OutputData output) throws Exception {
    StringBuffer s = new StringBuffer();
    s.append(this.quantity.getValue()).append("|");
    s.append(this.vat.getValue()).append("|");
    s.append(this.total.getValue()).append("|");
    s.append(this.vatTotal.getValue()).append("|");
    s.append(this.bigTotal.getValue());

    Writer out = ((HttpOutputData) output).getWriter();
    out.write(s.toString());
  }

  public static BigDecimal round2(BigDecimal num) {
    return new BigDecimal(num.movePointRight(2).intValue()).movePointLeft(2);
  }
}
