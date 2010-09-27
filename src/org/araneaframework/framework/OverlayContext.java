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

package org.araneaframework.framework;

import java.io.Serializable;
import java.util.Map;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.Widget;
import org.araneaframework.framework.FlowContext.Configurator;
import org.araneaframework.framework.FlowContext.Handler;
import org.araneaframework.http.PopupWindowContext;

/**
 * Another context interface that, in contrast to {@link FlowContext} and {@link PopupWindowContext}, deals with flow
 * logic that is running in overlay mode.
 * <p>
 * Using <code>OverlayContext</code> assumes that the visual part of the application also supports it. One must register
 * the <code>OverlayContext</code> in JSP with <code>&lt;ui:registerOverlay/&gt;</code> tag.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public interface OverlayContext extends Serializable {

  /**
   * The request parameter name identifying that the request comes from an overlay.
   */
  public static final String OVERLAY_REQUEST_KEY = "araOverlay";

  /**
   * Specifies whether the overlay mode is currently running and visible to the user.
   * 
   * @return whether the overlay mode is currently active.
   */
  boolean isOverlayActive();

  /**
   * Allows to specify custom overlay options as a <code>Map</code>. These options will be forwarded to the script that
   * handles the overlay mode visually.
   * 
   * @param options options as <code>&lt;String, String&gt;</code> pair.
   */
  void setOverlayOptions(Map<String, Object> options);

  /**
   * Provides the current options for overlay rendering.
   * 
   * @return the current options for overlay rendering.
   */
  Map<String, Object> getOverlayOptions();

  /**
   * Destroys the current flow and starts a new one inside the overlay mode. When the new flow ends execution, it will
   * return control to the caller of the current flow (no matter whether it is inside the overlay mode or not). Started
   * sub-flow can be configured using the configurator.
   * 
   * @param flow The uninitialized widget that wishes to run.
   * @param configurator Custom configuration for the new flow.
   */
  void replace(Widget flow, Configurator configurator);

  /**
   * Destroys the current flow and starts a new one inside the overlay mode. When the new flow ends execution, it will
   * return control to the caller of the current flow (no matter whether it is inside the overlay mode or not).
   * 
   * @param flow The uninitialized widget that should start to run.
   * @see FlowContext#replace(Widget)
   */
  void replace(Widget flow);

  /**
   * Resets all currently running flows inside the overlay mode and calls the <code>callback</code> allowing to start
   * new flows. Useful e.g. in a menu, when selecting a new menu item and reseting the old stack.
   * 
   * @param callback The callback to handle the calling of new flows.
   * @see FlowContext#reset(EnvironmentAwareCallback)
   */
  void reset(EnvironmentAwareCallback callback);

  /**
   * Starts a new nested <code>flow</code> inside the overlay mode, that can be configured using the
   * <code>configurator</code>. Current flow becomes inactive until sub-flow calls {@link FlowContext#finish(Object)} or
   * {@link FlowContext#cancel()}. <code>handler</code> allows to receive a notification, when the sub-flow ends
   * execution.
   * <p>
   * The <code>flow</code> is the only mandatory parameter.
   * 
   * @param flow The uninitialized widget that should start to run.
   * @param configurator The configuration handler for the widget.
   * @param handler Allows to receive notification when the widget finishes or cancels the flow.
   */
  void start(Widget flow, Configurator configurator, Handler<?> handler);

  /**
   * Starts a new nested <code>flow</code> inside the overlay mode. Current flow becomes inactive until sub-flow calls
   * {@link FlowContext#finish(Object)} or {@link FlowContext#cancel()}. <code>handler</code> allows to receive a
   * notification, when the sub-flow ends execution.
   * <p>
   * The <code>flow</code> is the only mandatory parameter.
   * 
   * @param flow The uninitialized widget that should start to run.
   * @param handler Allows to receive notification when the widget finishes or cancels the flow.
   */
  void start(Widget flow, Handler<?> handler);

  /**
   * Starts a new nested <code>flow</code> inside the overlay mode. Current flow becomes inactive until sub-flow calls
   * {@link FlowContext#finish(Object)} or {@link FlowContext#cancel()}.
   * <p>
   * The <code>flow</code> is mandatory parameter.
   * 
   * @param flow The uninitialized widget that should start to run.
   * @see FlowContext#start(Widget)
   */
  void start(Widget flow);

  /**
   * Similar to {@link FlowContext#cancel()} but closes the entire OverlayContext not just the last flow widget.
   * 
   * @since 1.2
   */
  void cancel();

  /**
   * /** Similar to {@link FlowContext#finish(Object)} but closes the entire OverlayContext not just the last flow
   * widget.
   * 
   * @param result The result to return from the overlay context.
   * 
   * @since 1.2
   */
  void finish(Object result);

  /**
   * This is a marker interface to say that overlay mode is active by putting this interface class into the
   * <code>Environment</code>. The tags may use the <code>Environment</code> to check the mode.
   * 
   * @author Alar Kvell (alar@araneaframework.org)
   * @since 1.1
   */
  interface OverlayActivityMarkerContext extends Serializable {}

}
