package org.araneaframework.uilib.event;

import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.EventListener;
import org.araneaframework.core.util.ProxiedHandlerUtil;
import org.araneaframework.uilib.form.BeanFormWidget;
import org.araneaframework.uilib.form.FormWidget;
import org.araneaframework.uilib.form.reader.BeanFormReader;

/**
 * This is a special event handler that listens for any events targeted at given
 * widget and validates the given form before passing the event to the target
 * widget. If the form validation fails, the widget won't receive its event.
 * <p>
 * In the target widget, it looks for certain types of event listener methods:
 * 
 * <pre><code>
 * public void handleEvent[IncomingEventId](MyObject formObject);
 * </code></pre>
 * 
 * or
 * 
 * <pre><code>
 * public void handleEvent[IncomingEventId](MyObject formObject, String eventParam);
 * </code></pre>
 * 
 * The data object class is either specified as a constructor parameter or
 * determined by the <code>beanClass</code> variable of the
 * {@link BeanFormWidget}. If no such event listener exists then it will just
 * give a warning.
 * <p>
 * It is also quite easy to use:
 * 
 * <pre><code>
 * form.addEventListener([IncomingEventId], new ValidatingProxyEventListener(this, form));
 * </code></pre>
 * 
 * @see ProxyOnChangeEventListener
 * @see ProxyOnClickEventListener
 */
public final class ValidatingProxyEventListener implements EventListener {

  private static final long serialVersionUID = 1L;

  private static final Log log = LogFactory.getLog(ValidatingProxyEventListener.class);

  private Widget eventTarget;

  private FormWidget form;

  private Class modelType;

  /**
   * Initializes this event listener with target widget (<code>eventTarget</code>)
   * and form widget (<code>form</code>). The <code>modelType</code>
   * specifies the data object class that can be used to read form data. If the
   * <code>form</code> is already a {@link BeanFormWidget} then it is easier
   * to use the other constructor.
   * 
   * @param eventTarget The target widget that has the event listeners
   * @param form The form widget that holds the data to collect.
   * @param modelType The class of the data object.
   */
  public ValidatingProxyEventListener(Widget eventTarget, FormWidget form,
      Class modelType) {
    Assert.notNullParam(form, "form");
    Assert.notNullParam(eventTarget, "eventTarget");
    Assert.notNullParam(modelType, "modelType");

    this.eventTarget = eventTarget;
    this.form = form;
    this.modelType = modelType;
  }

  /**
   * Initializes this event listener with target widget (<code>eventTarget</code>)
   * and form widget (<code>form</code>). The <code>beanClass</code> of the
   * <code>form</code> will be used to identify the data object parameter type
   * of the event listener.
   * 
   * @param eventTarget The target widget that has the event listeners
   * @param form The bean form widget that holds the data to collect.
   */
  public ValidatingProxyEventListener(Widget eventTarget, BeanFormWidget form) {
    Assert.notNullParam(form, "form");
    Assert.notNullParam(eventTarget, "eventTarget");

    this.eventTarget = eventTarget;
    this.form = form;
    this.modelType = form.getBeanClass();
  }

  public void processEvent(Object eventId, InputData input) throws Exception {
    if (!form.convertAndValidate()) {
      return;
    }

    BeanFormReader reader = new BeanFormReader(form);
    Object bean = reader.getBean(modelType);
    String eventParameter = (String) input.getGlobalData().get(
        ApplicationWidget.EVENT_PARAMETER_KEY);

    String eventHandlerName = "handleEvent"
        + ((String) eventId).substring(0, 1).toUpperCase()
        + ((String) eventId).substring(1);

    Method eventHandler;

    // lets try to find a handle method with a bean argument
    try {
      eventHandler = ProxiedHandlerUtil.getEventHandler((String) eventId,
          eventTarget, new Class[] { modelType });

      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + eventHandlerName + "("
            + modelType.getName() + ")' of class '"
            + eventTarget.getClass().getName() + "'.");
      }

      eventHandler.invoke(eventTarget, new Object[] { bean });
      return;
    } catch (NoSuchMethodException e) {/* OK */}

    // lets try to find a method with a bean and a String type argument
    try {
      eventHandler = ProxiedHandlerUtil.getEventHandler((String) eventId,
          eventTarget, new Class[] { modelType, String.class });

      if (log.isDebugEnabled()) {
        log.debug("Calling method '" + eventHandlerName + "("
            + modelType.getName() + ", String)' of class '"
            + eventTarget.getClass().getName() + "'.");
      }

      eventHandler.invoke(eventTarget, new Object[] { bean, eventParameter });
      return;
    } catch (NoSuchMethodException e) {/* OK */}

    if (log.isWarnEnabled()) {
      log.warn("Widget '" + eventTarget.getScope() + "' cannot deliver "
          + "event as no event listeners were registered for the event id '"
          + eventId + "'!" + Assert.thisToString(eventTarget));
    }
  }

}
