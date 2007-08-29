package org.araneaframework.example.main.web.demo;

import java.util.Date;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.OverlayRootWidget;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.uilib.event.ProxyOnClickEventListener;
import org.araneaframework.uilib.form.FormElement;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.constraint.NotEmptyConstraint;
import org.araneaframework.uilib.form.control.ButtonControl;
import org.araneaframework.uilib.form.control.CheckboxControl;
import org.araneaframework.uilib.form.control.DateControl;
import org.araneaframework.uilib.form.control.DateTimeControl;
import org.araneaframework.uilib.form.control.FloatControl;
import org.araneaframework.uilib.form.control.TextControl;
import org.araneaframework.uilib.form.control.TimeControl;
import org.araneaframework.uilib.form.data.BigDecimalData;
import org.araneaframework.uilib.form.data.BooleanData;
import org.araneaframework.uilib.form.data.DateData;
import org.araneaframework.uilib.form.data.StringData;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class ModalDialogDemoWidget extends TemplateBaseWidget {
	  private static final long serialVersionUID = 1L;
	  private FormWidget form;
	  
	  /**
	   * Builds the form.
	   */
	  protected void init() throws Exception {
		setViewSelector("demo/modalDialog");

		form = new FormWidget();

	    FormElement el = form.createElement("#Textbox", new TextControl(), new StringData(), false);
	    form.addElement("textbox1", el);
	    
	    // and here we add form elements to form without the extra step taken previously. 
	    form.addElement("checkbox1", "#Checkbox", new CheckboxControl(), new BooleanData(), false);
	    form.addElement("dateTime", "common.datetime", new DateTimeControl(), new DateData(), false);
	    form.addElement("time", "common.time", new TimeControl(), new DateData(), false);
	    form.addElement("date", "common.date", new DateControl(), new DateData(), false);
	    form.addElement("number", "#Number", new FloatControl(), new BigDecimalData(), false);
	    // require the number input field to be filled. It could have been achieved already
	    // on formelement creation by setting mandatory attribute to true
	    form.getElement("number").setConstraint(new NotEmptyConstraint());
	    // sets initial value of form element
	    form.setValueByFullName("dateTime", new Date());

		// now we construct a button, that is also Control. Reason why we cannot just add it
	    // to form is obvious, we want to add a specific listener to button before.
	    ButtonControl button = new ButtonControl();
		button.addOnClickEventListener(new ProxyOnClickEventListener(this, "testSimpleForm"));
		// add the button to form. As the button does not hold any value, Data will be null.
		form.addElement("button", "#Button", button, null, false);
	    
	    // the usual, add the created widget to main widget.
		addWidget("form", form);
	  }

	  /**
	   * A test action, invoked when button is pressed. It adds the values of 
	   * formelements to message context, and they end up at the top of user screen
	   * at the end of the request.
	   */
	  public void handleEventTestSimpleForm() throws Exception {
	    // if form is not invalid, do not try to show form element values 
	    // (error messages are added automatically to the messagecontext 
	    // though, user will not be without feedback)
	    if (form.convertAndValidate()) {
	    	// long way to check form element value ...
	    	getMessageCtx().showInfoMessage("Checkbox value is: " + ((FormElement) form.getElement("checkbox1")).getData().getValue());
	    	// and a shorter one
	    	getMessageCtx().showInfoMessage("Textbox value is: " + form.getValueByFullName("textbox1"));
	    	getMessageCtx().showInfoMessage("DateTime value is: " + form.getValueByFullName("dateTime"));
	    	getMessageCtx().showInfoMessage("Time value is: " + form.getValueByFullName("time"));
	    	getMessageCtx().showInfoMessage("Date value is: " + form.getValueByFullName("date"));
	    	getMessageCtx().showInfoMessage("Number value is: " + form.getValueByFullName("number"));
	    }
	  }
	  
	  public void handleEventNextFlowOverlay() throws Exception {
	    getOverlayCtx().getFlowCtx().start(new OverlayRootWidget(new StandardFlowContainerWidget(new ModalDialogDemoWidget())));
	  }
	  
	  public void handleEventNextFlow() throws Exception {
	      getFlowCtx().start(new ModalDialogDemoWidget());
	  }
	  
	  public void handleEventReturn() throws Exception {
	    getFlowCtx().cancel();
	  }
	  
	  public OverlayContext getOverlayCtx() {
	    return (OverlayContext) getEnvironment().requireEntry(OverlayContext.class);
	  }
}
