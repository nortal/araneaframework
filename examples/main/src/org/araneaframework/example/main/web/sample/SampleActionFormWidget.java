package org.araneaframework.example.main.web.sample;

import java.io.Writer;
import java.math.BigDecimal;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.StandardActionListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.NumberControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.IntegerData;

public class SampleActionFormWidget extends TemplateBaseWidget {

  private FormWidget form;
  private FormElement price;
  private FormElement quantity;
  private FormElement vat;
  private FormElement total;
  private FormElement vatTotal;
  private FormElement bigTotal;

  protected void init() throws Exception {
    super.init();
    setViewSelector("sample/sampleActionForm");
    
    form = new FormWidget();
    
    price = form.addElement("price", "#Price", new FloatControl(), new BigDecimalData(), false);
    price.setDisabled(true);
    price.setValue(round2(new BigDecimal(Math.random() * 5 + 5)));
    
    quantity = form.addElement("quantity", "#Quantity", new NumberControl(), new IntegerData(), false);
    quantity.setValue(new Integer(3));
    
    total = form.addElement("total", "#Total", new FloatControl(), new BigDecimalData(), false);
    total.setDisabled(true);
    
    vat = form.addElement("vat", "#VAT", new FloatControl(), new BigDecimalData(), false);
    vat.setValue(new BigDecimal("0.18"));
    
    vatTotal = form.addElement("vatTotal", "#not used", new FloatControl(), new BigDecimalData(), false);
    vatTotal.setDisabled(true);
    
    bigTotal = form.addElement("bigTotal", "#Total Sum", new FloatControl(), new BigDecimalData(), false);
    bigTotal.setDisabled(true);
    
    calculate();
    addWidget("form", form);
    
    addActionListener("quantityChange", new StandardActionListener() {
      public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
        // TODO numberFormatException handling
        quantity.setValue(new Integer(actionParam));
        calculate();
        form._getWidget().process();
        writeFields(output);
      }}
    );
    
    addActionListener("vatChange", new StandardActionListener() {
      public void processAction(Object actionId, String actionParam, InputData input, OutputData output) throws Exception {
        // TODO numberFormatException handling
        vat.setValue(round2(new BigDecimal(actionParam)));
        calculate();
        form._getWidget().process();
        writeFields(output);
      }}
    );
  }

  protected void calculate() {
    total.setValue(round2(calculateTotal((BigDecimal) price.getValue(), (Integer) quantity.getValue())));
    vatTotal.setValue(round2(calculateVatTotal((BigDecimal) total.getValue(), (BigDecimal) vat.getValue())));
    bigTotal.setValue(round2(calculateBigTotal((BigDecimal) total.getValue(), (BigDecimal) vatTotal.getValue())));
  }

  protected BigDecimal calculateTotal(BigDecimal price, Integer quantity) {
    return price.multiply(new BigDecimal(quantity.intValue()));
  }

  protected BigDecimal calculateVatTotal(BigDecimal total, BigDecimal vat) {
    return total.multiply(vat);
  }

  protected BigDecimal calculateBigTotal(BigDecimal total, BigDecimal vatTotal) {
    return total.add(vatTotal);
  }

  protected void writeFields(OutputData output) throws Exception {
    StringBuffer s = new StringBuffer();
    s.append(quantity.getValue()).append("|");
    s.append(vat.getValue()).append("|");
    s.append(total.getValue()).append("|");
    s.append(vatTotal.getValue()).append("|");
    s.append(bigTotal.getValue());

    Writer out = ((HttpOutputData) output).getWriter();
    out.write(s.toString());
  }

  public static BigDecimal round2(BigDecimal num) {
    return new BigDecimal(num.movePointRight(2).toBigInteger()).movePointLeft(2);
  }

}
