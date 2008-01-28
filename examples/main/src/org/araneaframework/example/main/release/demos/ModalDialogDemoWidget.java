package org.araneaframework.example.main.release.demos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.Closure;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.time.DateUtils;
import org.araneaframework.Component;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BroadcastMessage;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.example.main.TemplateBaseWidget;
import org.araneaframework.example.main.web.OverlayRootWidget;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.FlowEventConfirmationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.framework.FlowEventConfirmationContext.ConfirmationCondition;
import org.araneaframework.framework.container.FlowEventConfirmationContextImpl;
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

    form = new FormWidget();

    FormElement el = form.createElement("common.Textbox", new TextControl(), new StringData(), false);
    form.addElement("textbox1", el);

    form.addElement("checkbox1", "Checkbox", new CheckboxControl(), new BooleanData(), false);
    form.addElement("dateTime", "common.datetime", new DateTimeControl(), new DateData(), false);
    form.addElement("time", "common.time", new TimeControl(), new DateData(), false);
    form.addElement("date", "common.date", new DateControl(), new DateData(), false);
    form.addElement("number", "common.float", new FloatControl(), new BigDecimalData(), false);
    // require the number input field to be filled. It could have been achieved already
    // on formelement creation by setting mandatory attribute to true
    form.getElement("number").setConstraint(new NotEmptyConstraint());
    // sets initial value of form element

	form.setValueByFullName("dateTime", DateUtils.truncate(new Date(), Calendar.MINUTE));

	// now we construct a button, that is also Control. Reason why we cannot just add it
    // to form is obvious, we want to add a specific listener to button before.
    ButtonControl button = new ButtonControl();
	button.addOnClickEventListener(new ProxyOnClickEventListener(this, "testSimpleForm"));
	// add the button to form. As the button does not hold any value, Data will be null.
	form.addElement("button", "common.Submit", button, null, false);
	
	form.markBaseState();
	
	class X implements Predicate, Serializable {
		public boolean evaluate(Object obj) {
			form.convert();
			boolean stateChanged = form.isStateChanged();
			getMessageCtx().showInfoMessage("Predicate was evaluated to " + stateChanged);
			return stateChanged;
		}
    }

	final X xp = new X();
	
	class ConfCondition extends FlowEventConfirmationContextImpl.NoopConfirmationCondition {
      public Predicate getCancelPredicate() {
        return xp;
      }
	}

	StandardFlowEventConfirmationHandler confirmationHandler = new StandardFlowEventConfirmationHandler(new ConfCondition());
	confirmationHandler.setDoConfirm(new StandardConfirmFlowEventClosure());
  getFlowEventAutoConfirmationContext().setFlowEventConfirmationHandler(confirmationHandler);

    // the usual, add the created widget to main widget.
	addWidget("form", form);
  }

  public void handleEventTestSimpleForm() throws Exception {
    form.convertAndValidate();
  }
  
  public void handleEventNextFlowOverlay() throws Exception {
    getOverlayCtx().start(new OverlayRootWidget(new StandardFlowContainerWidget(new ModalDialogDemoWidget(true))));
  }
  
  public void handleEventNextFlow() throws Exception {
    getFlowCtx().start(new ModalDialogDemoWidget(true));
  }
  
  public void handleEventReturn() throws Exception {
    if (isNested())
      getFlowCtx().cancel();
  }

  public boolean isNested() {
	  return nested;
  }
  
  public OverlayContext getOverlayCtx() {
    return (OverlayContext) getEnvironment().requireEntry(OverlayContext.class);
  }
  
  /// temporary testing hacks
  
  protected static class FlowEventConfirmationEventListener extends StandardEventListener {
    private static final long serialVersionUID = 1L;
	private Closure positive, negative;
	  
	public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
      boolean b = Boolean.valueOf(eventParam).booleanValue();
      (b ? positive : negative).execute(null);
	}

    public void setPositive(Closure positive) {
      this.positive = positive;
    }

    public void setNegative(Closure negative) {
      this.negative = negative;
    }
  }
  
  protected static class StandardFlowEventConfirmationHandler implements FlowEventConfirmationContext.FlowEventConfirmationHandler {
    private static final long serialVersionUID = 1L;
	  private ConfirmationCondition conditionProvider;
	  private Closure onConfirm;
	  private Closure doConfirm;

    public StandardFlowEventConfirmationHandler(ConfirmationCondition conditionProvider) {
      setConfirmationCondition(conditionProvider);
    }

    public void setConfirmationCondition(ConfirmationCondition conditionProvider) {
      this.conditionProvider = conditionProvider;   
    }

    public void setOnConfirm(Closure onConfirm) {
      Assert.isInstanceOf(Serializable.class, onConfirm, "onConfirm Closure must implement java.io.Serializable");
      this.onConfirm = onConfirm;
    }
    
    public Closure getOnConfirm() {
      return this.onConfirm;
    }
    
    public void setDoConfirm(Closure doConfirm) {
      Assert.isInstanceOf(Serializable.class, doConfirm, "doConfirm Closure must implement java.io.Serializable");
      this.doConfirm = doConfirm;
    }

    public Closure getDoConfirm() {
      return doConfirm;
    }

    public ConfirmationCondition getConfirmationCondition() {
      return conditionProvider;
    }
  }
  
  protected static class StandardConfirmFlowEventClosure implements Closure, Serializable {
    private static final long serialVersionUID = 1L;

    public void execute(Object flowObject) {
      Widget flow = (Widget) flowObject;
      FlowContext fCtx = (FlowContext) flow.getEnvironment().requireEntry(FlowContext.class);
      MessageContext msgCtx = (MessageContext) flow.getEnvironment().requireEntry(MessageContext.class);

      FormWidgetFinderMessage msg = new FormWidgetFinderMessage();
      msg.send(null, flow);
      List forms = msg.getAllForms();

      for (Iterator i = forms.iterator(); i.hasNext();) {
        FormWidget fw = (FormWidget) i.next();
        fw.markBaseState();
      }
      
      msgCtx.showInfoMessage("Form data was changed since last save. Repeat navigation event to confirm your intentions.");
    }
  }
  
  private static class FormWidgetFinderMessage extends BroadcastMessage {
    List formList = new ArrayList();

    protected void execute(Component component) throws Exception {
      if (component instanceof org.araneaframework.uilib.form.FormWidget) {
        formList.add(component);
      }
    }

    public List getAllForms() { return formList; }
  }
}
