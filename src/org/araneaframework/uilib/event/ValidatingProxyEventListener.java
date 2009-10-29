package org.araneaframework.uilib.event;

import static org.araneaframework.core.util.ProxiedHandlerUtil.*;

import org.araneaframework.core.util.ProxiedHandlerUtil;

import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.InputData;
import org.araneaframework.Widget;
import org.araneaframework.core.ApplicationWidget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.EventListener;
import org.araneaframework.uilib.form.BeanFormWidget;

/**
 * This is a special event handler that listens for any events targeted at given widget and validates the given form
 * before passing the event to the target widget. If the form validation fails, the widget won't receive its event.
 * <p>
 * In the target widget, it looks for certain types of event listener methods:
 * <pre><code>public void handleEvent[IncomingEventId](MyObject formObject);</code></pre>
 * or
 * <pre><code>public void handleEvent[IncomingEventId](MyObject formObject, String eventParam);</code></pre>
 * or
 * <pre><code>public void handleEvent[IncomingEventId](MyObject formObject, String[] eventParam);</code></pre>
 * or
 * <pre><code>public void handleEvent[IncomingEventId](MyObject formObject, List&lt;String&gt; eventParam);</code></pre>
 * or
 * <pre><code>public void handleEvent[IncomingEventId](MyObject formObject, Map&lt;String, String&gt; eventParam);</code></pre>
 * <p>
 * The data object class is either specified as a constructor parameter or determined by the <code>beanClass</code>
 * variable of the {@link BeanFormWidget}. If no such event listener exists then it will just give a warning.
 * <p>
 * It is also quite easy to use:
 * 
 * <pre><code>form.addEventListener([IncomingEventId], new ValidatingProxyEventListener(this, form));</code></pre>
 * 
 * @see ProxyOnChangeEventListener
 * @see ProxyOnClickEventListener
 * @see ProxiedHandlerUtil
 */
public final class ValidatingProxyEventListener<T> implements EventListener {

  private static final Log LOG = LogFactory.getLog(ValidatingProxyEventListener.class);

  private Widget eventTarget;

  private BeanFormWidget<T> form;

  private Class<T> modelType;

  /**
   * Initializes this event listener with target widget (<code>eventTarget</code>) and form widget (<code>form</code>).
   * The <code>modelType</code> specifies the data object class that can be used to read form data. If the
   * <code>form</code> is already a {@link BeanFormWidget} then it is easier to use the other constructor.
   * 
   * @param eventTarget The target widget that has the event listeners
   * @param form The form widget that holds the data to collect.
   * @param modelType The class of the data object.
   */
  public ValidatingProxyEventListener(Widget eventTarget, BeanFormWidget<T> form, Class<T> modelType) {
    Assert.notNullParam(form, "form");
    Assert.notNullParam(eventTarget, "eventTarget");
    Assert.notNullParam(modelType, "modelType");
    this.eventTarget = eventTarget;
    this.form = form;
    this.modelType = modelType;
  }

  /**
   * Initializes this event listener with target widget (<code>eventTarget</code>) and form widget (<code>form</code>).
   * The <code>beanClass</code> of the <code>form</code> will be used to identify the data object parameter type of the
   * event listener.
   * 
   * @param eventTarget The target widget that has the event listeners
   * @param form The bean form widget that holds the data to collect.
   */
  public ValidatingProxyEventListener(Widget eventTarget, BeanFormWidget<T> form) {
    this(eventTarget, form, form.getBeanClass());
  }

  public void processEvent(String eventId, InputData input) throws Exception {
    if (!this.form.convertAndValidate()) {
      return;
    }

    String param = input.getGlobalData().get(ApplicationWidget.EVENT_PARAMETER_KEY);
    String methodName = getHandlerName(EVENT_HANDLER_PREFIX, eventId);
    String className = this.eventTarget.getClass().getName();
    String modelClassName = this.modelType.getName();
    T bean = this.form.getBean();

    // lets try to find a handle method with a bean argument
    // First, let's try to find a handle method with an empty argument:
    if (hasHandler(this.eventTarget, methodName, this.modelType)) {
      log(methodName, modelClassName, className);
      MethodUtils.invokeExactMethod(this.eventTarget, methodName, bean);

    } else if (hasHandler(this.eventTarget, methodName, this.modelType, String.class)) {
      log(methodName, modelClassName + ", String", className);
      MethodUtils.invokeExactMethod(this.eventTarget, methodName, new Object[] { bean, String.class });

    } else if (hasHandler(this.eventTarget, methodName, this.modelType, String.class)) {
      log(methodName, modelClassName + ", String[]", className);
      MethodUtils.invokeExactMethod(this.eventTarget, methodName, new Object[] { bean, splitParam(param) });

    } else if (hasHandler(this.eventTarget, methodName, this.modelType, List.class)) {
      log(methodName, modelClassName + ", List<String>", className);
      MethodUtils.invokeExactMethod(this.eventTarget, methodName, new Object[] { bean, parseParamList(param) });

    } else if (hasHandler(this.eventTarget, methodName, this.modelType, Map.class)) {
      log(methodName, modelClassName + ", List<String>", className);
      MethodUtils.invokeExactMethod(this.eventTarget, methodName, new Object[] { bean, parseParamMap(param) });

    } else if (LOG.isWarnEnabled()) {
      logHandlerNotFound(EVENT_HANDLER_PREFIX, eventId, this.eventTarget);
    }
  }
}
