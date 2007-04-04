package org.araneaframework.uilib.event;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.EventListener;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.reader.BeanFormReader;

public final class ValidatingProxyEventListener implements EventListener {
  public static final Logger log = Logger.getLogger(ValidatingProxyEventListener.class);

  private Object eventTarget;
  private FormWidget form;
  private Class modelType;

  public ValidatingProxyEventListener(Object eventTarget, FormWidget form, Class modelType) {
    Assert.notNullParam(form, "form");
    Assert.notNullParam(eventTarget, "eventTarget");
    Assert.notNullParam(modelType, "modelType");
    
    this.eventTarget = eventTarget;
    this.form = form;
    this.modelType = modelType;
  }
  
  public ValidatingProxyEventListener(Object eventTarget, BeanFormWidget form) {
    Assert.notNullParam(form, "form");
    Assert.notNullParam(eventTarget, "eventTarget");
    
    this.eventTarget = eventTarget;
    this.form = form;
    this.modelType = form.getBeanClass();
  }

  public void processEvent(Object eventId, InputData input) throws Exception {
    if (!form.convertAndValidate()) return;

    BeanFormReader reader = new BeanFormReader(form);
    Object bean = reader.getBean(modelType);

    String eventParameter = (String) input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);
    String eventHandlerName = "handleEvent" + ((String) eventId).substring(0, 1).toUpperCase()
        + ((String) eventId).substring(1);

    Method eventHandler;
    // lets try to find a handle method with a bean argument
    try {
      eventHandler = eventTarget.getClass().getMethod(eventHandlerName, new Class[] { modelType });

      log.debug("Calling method '" + eventHandlerName + "(" + modelType.getName() + ")' of class '"
          + eventTarget.getClass().getName() + "'.");
      eventHandler.invoke(eventTarget, new Object[] { bean });

      return;
    }
    catch (NoSuchMethodException e) {/* OK */
    }

    // lets try to find a method with a bean and a String type argument
    try {
      eventHandler = eventTarget.getClass().getMethod(eventHandlerName, new Class[] { modelType, String.class });

      log.debug("Calling method '" + eventHandlerName + "(" + modelType.getName() + ", String)' of class '"
          + eventTarget.getClass().getName() + "'.");
      eventHandler.invoke(eventTarget, new Object[] { bean, eventParameter });

      return;
    }
    catch (NoSuchMethodException e) {/* OK */
    }

    log.warn("Widget '" + input.getScope() +
        "' cannot deliver event as no event listeners were registered for the event id '" + eventId + "'!" + Assert.thisToString(eventTarget)); 

  }
}
