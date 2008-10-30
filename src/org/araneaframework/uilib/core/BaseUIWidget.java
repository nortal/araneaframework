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

package org.araneaframework.uilib.core;

import org.araneaframework.Component;
import org.araneaframework.Environment;
import org.araneaframework.OutputData;
import org.araneaframework.Scope;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.ProxyEventListener;
import org.araneaframework.framework.ConfirmationContext;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.LocalizationContext;
import org.araneaframework.framework.MessageContext;
import org.araneaframework.framework.MountContext;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.framework.OverlayContext.OverlayActivityMarkerContext;
import org.araneaframework.http.JspContext;
import org.araneaframework.http.PopupWindowContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.ConfigurationContext;
import org.springframework.beans.factory.BeanFactory;

/**
 * This widget represents the usual custom application widget that is rendered
 * using JSP tags. It assumes to be connected with a JSP page and allows setting
 * its view selector.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class BaseUIWidget extends BaseApplicationWidget {

  private static final long serialVersionUID = 1L;

  protected String viewSelector;

  /**
   * Sets the view selector for this widget, should be path to <code>jsp</code>
   * file without <code>jsp</code> extension.
   * 
   * @param viewSelector path to <code>jsp</code> file, without file extension
   */
  protected void setViewSelector(String viewSelector) {
    this.viewSelector = viewSelector;
  }

  /**
   * Provides the <code>ConfigurationContext</code> from the
   * <code>Environment</code>. It can be used to access configuration
   * settings.
   * 
   * @return The <code>ConfigurationContext</code>.
   */
  protected ConfigurationContext getConfiguration() {
    return (ConfigurationContext) getEnvironment().requireEntry(
        ConfigurationContext.class);
  }

  /**
   * Provides the <code>FlowContext</code> from the <code>Environment</code>.
   * It can be used to start, replace, or finish flows.
   * 
   * @return The <code>FlowContext</code>.
   */
  protected FlowContext getFlowCtx() {
    return EnvironmentUtil.getFlowContext(getEnvironment());
  }

  /**
   * Provides the <code>MessageContext</code> from the
   * <code>Environment</code>. It can be used to display or hide messages
   * from the user.
   * 
   * @return The <code>MessageContext</code>.
   */
  protected MessageContext getMessageCtx() {
    return EnvironmentUtil.getMessageContext(getEnvironment());
  }

  /**
   * Provides the <code>LocalizationContext</code> from the
   * <code>Environment</code>. It can be used to handle localization changes,
   * and translate (i.e. resolve) messages.
   * 
   * @return The <code>LocalizationContext</code>.
   */
  protected LocalizationContext getL10nCtx() {
    return EnvironmentUtil.getLocalizationContext(getEnvironment());
  }

  /**
   * Provides the <code>MountContext</code> from the <code>Environment</code>.
   * It can be used to provide fixed URLs that can be accessed by GET requests.
   * 
   * @return The <code>MountContext</code>.
   */
  protected MountContext getMountCtx() {
    return (MountContext) getEnvironment().requireEntry(MountContext.class);
  }

  /**
   * Provides the <code>BeanFactory</code> from the <code>Environment</code>.
   * It can be used to access Spring framework configuration.
   * 
   * @return The <code>BeanFactory</code>.
   */
  protected BeanFactory getBeanFactory() {
    return (BeanFactory) getEnvironment().requireEntry(BeanFactory.class);
  }

  /**
   * Provides the <code>OverlayContext</code> from the
   * <code>Environment</code>. It can be used to start and handle flow in
   * <i>overlay</i> mode.
   * 
   * @return The <code>OverlayContext</code>.
   */
  protected OverlayContext getOverlayCtx() {
    return (OverlayContext) getEnvironment().requireEntry(OverlayContext.class);
  }

  /**
   * Provides the <code>ConfirmationContext</code> from the
   * <code>Environment</code>. It can be used to present the user a question
   * in a message box, and activate the code in the closure, if the user chooses
   * yes.
   * 
   * @return The <code>ConfirmationContext</code>.
   * @since 1.1.3
   */
  protected ConfirmationContext getConfirmationCtx() {
    return (ConfirmationContext) getEnvironment().requireEntry(
        ConfirmationContext.class);
  }

  /**
   * Provides the <code>PopupWindowContext</code> from the
   * <code>Environment</code>. It can be used to open and manage popups.
   * 
   * @return The <code>PopupWindowContext</code>.
   * @since 1.1.3
   */
  protected PopupWindowContext getPopupCtx() {
    return (PopupWindowContext) getEnvironment().requireEntry(
        PopupWindowContext.class);
  }

  /**
   * Specifies wether this widget is running in <i>overlay</i> mode.
   * 
   * @return <code>true</code>, if this widget is running in <i>overlay</i>
   *         mode.
   * @since 1.1
   */
  protected boolean isRunningInOverlay() {
    return getEnvironment().getEntry(OverlayActivityMarkerContext.class) != null;
  }

  /**
   * Translates the message under the given key, with help from widget's current
   * {@link org.araneaframework.framework.LocalizationContext}.
   * 
   * @param key The key to translate.
   * @return The found message found translated into language corresponding to
   *         current <code>Locale</code>.
   */
  protected String t(String key) {
    return getL10nCtx().localize(key);
  }

  /**
   * Renders widget to <code>output</code> using the defined
   * <code>viewSelector</code>.
   */
  protected void render(OutputData output) throws Exception {
    if (viewSelector == null)
      throw new RuntimeException("Widget '" + getClass().getName()
          + "' does not have a view selector!");
    JspContext jspCtx = (JspContext) getEnvironment().requireEntry(
        JspContext.class);
    String jsp = resolveJspName(jspCtx, viewSelector);
    ServletUtil.include(jsp, this, output);
  }

  protected String resolveJspName(JspContext jspCtx, String viewSelector) {
    return jspCtx.getJspPath() + "/" + viewSelector + jspCtx.getJspExtension();
  }

  public Component.Interface _getComponent() {
    return new ComponentImpl();
  }

  protected class ComponentImpl extends BaseApplicationWidget.ComponentImpl {

    private static final long serialVersionUID = 1L;

    public synchronized void init(Scope scope, Environment env) {
      setGlobalEventListener(new ProxyEventListener(BaseUIWidget.this));
      super.init(scope, env);
    }
  }
}
