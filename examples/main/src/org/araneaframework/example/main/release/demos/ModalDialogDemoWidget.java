
package org.araneaframework.example.main.release.demos;

import org.araneaframework.http.HttpInputData;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.uilib.core.OverlayRootWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FileUploadControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.FileInfoData;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ModalDialogDemoWidget extends TemplateBaseWidget {

  private static final long serialVersionUID = 1L;

  private FormWidget form;

  private boolean nested = false;

  public ModalDialogDemoWidget() {}

  private ModalDialogDemoWidget(boolean isNested) {
    this.nested = isNested;
  }

  /**
   * Builds the form.
   */
  protected void init() throws Exception {
    setViewSelector("release/demos/modalDialog");
    addWidget("form", buildForm());
  }

  private FormWidget buildForm() {
    this.form = new FormWidget();

    this.form.addElement("textbox1", form.createElement("common.Textbox",
        new TextControl(), new StringData(), false));
    this.form.addElement("checkbox1", "Checkbox", new CheckboxControl(),
        new BooleanData(), false);
    this.form.addElement("dateTime", "common.datetime", new DateTimeControl(),
        new DateData(), false);
    this.form.addElement("date", "common.date", new DateControl(),
        new DateData(), false);
    this.form.addElement("number", "common.float", new FloatControl(),
        new BigDecimalData(), false);
    this.form.addElement("upload", "common.file", new FileUploadControl(),
        new FileInfoData(), false);


    // Require the number input field to be filled. It could have been achieved
    // already on formelement creation by setting mandatory attribute to true.
    this.form.getElement("number").setConstraint(new NotEmptyConstraint());


    // Sets initial value of the form element:
    this.form.setValueByFullName("dateTime", DateUtils.truncate(new Date(),
        Calendar.MINUTE));


    // Now we construct a button, that is also Control. Reason why we cannot
    // just add it to form is obvious, we want to add a specific listener to
    // button before:
    ButtonControl button = new ButtonControl();
    button.addOnClickEventListener(new ProxyOnClickEventListener(this,
        "testSimpleForm"));


    // Add the button to form. As the button does not hold any value, Data will
    // be null.
    this.form.addElement("button", "common.Submit", button, null, false);

    return this.form;
  }

  public void handleEventTestSimpleForm() throws Exception {
    HttpInputData input = (HttpInputData) getInputData();
    input.pushPathPrefix("pathDemo");
    input.popPathPrefix();
    this.form.convertAndValidate();
  }

  public void handleEventNextFlowOverlay() throws Exception {
    getOverlayCtx().start(
        new OverlayRootWidget(new ModalDialogDemoWidget(true)));
  }

  public void handleEventNextFlow() throws Exception {
    getFlowCtx().start(new ModalDialogDemoWidget(true));
  }

  public void handleEventReturn() throws Exception {
    if (isNested()) {
      getFlowCtx().cancel();
    }
  }

  public void handleEventClose() throws Exception {
    if (isOverlay()) {
      getOverlayCtx().cancel();
    }
  }

  public boolean isNested() {
    return this.nested;
  }

  public boolean isOverlay() {
    return getOverlayCtx().isOverlayActive();
  }

}
