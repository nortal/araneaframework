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

package org.araneaframework.framework.container;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Environment;
import org.araneaframework.EnvironmentAwareCallback;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.FlowContextWidget;
import org.araneaframework.framework.OverlayContext;
import org.araneaframework.framework.FlowContext.Configurator;
import org.araneaframework.framework.FlowContext.Handler;
import org.araneaframework.http.UpdateRegionContext;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.ServletUtil;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class StandardOverlayContainerWidget extends BaseApplicationWidget implements OverlayContext {

  /**
   * <p>
   * Map containing the default overlay presentation options. OPTIONS with default values (most default values are
   * defined in the JavaScript file but they all can be overridden using <code>OverlayContext</code>):
   * <p>
   * <table style="border-color: #000" border="1" cellpadding="5" cellspacing="0">
   * <tr>
   * <th>Option</th>
   * <th>Default value</th>
   * <th>Description</th>
   * </tr>
   * <tr>
   * <td>overlayClose</td>
   * <td>false</td>
   * <td>Close modal box by clicking on overlay</td>
   * </tr>
   * <tr>
   * <td>width</td>
   * <td>800</td>
   * <td>Default width in pixels</td>
   * </tr>
   * <tr>
   * <td>height</td>
   * <td>90</td>
   * <td>Default height in pixels</td>
   * </tr>
   * <tr>
   * <td>maxHeight</td>
   * <td>0.9</td>
   * <td>
   * If content is very long, defines the maximum height of ModalBox. If value <= 1.0 then the value is a percentage of
   * dialog height in contrast to browser window client area height. Otherwise it's in pixels.</td>
   * </tr>
   * <tr>
   * <td>overlayOpacity</td>
   * <td>0.75</td>
   * <td>Default overlay opacity</td>
   * </tr>
   * <tr>
   * <td>overlayDuration</td>
   * <td>0</td>
   * <td>Default overlay fade in/out duration in seconds</td>
   * </tr>
   * <tr>
   * <td>slideDownDuration</td>
   * <td>0</td>
   * <td>Default ModalBox appear slide down effect in seconds</td>
   * </tr>
   * <tr>
   * <td>slideUpDuration</td>
   * <td>0</td>
   * <td>Default ModalBox hiding slide up effect in seconds</td>
   * </tr>
   * <tr>
   * <td>resizeDuration</td>
   * <td>0</td>
   * <td>Default resize duration seconds</td>
   * </tr>
   * <tr>
   * <td>inactiveFade</td>
   * <td>true</td>
   * <td>Fades MB window on inactive state transitions</td>
   * </tr>
   * <tr>
   * <td>transitions</td>
   * <td>false</td>
   * <td>Toggles transition effects</td>
   * </tr>
   * <tr>
   * <td>loadingString</td>
   * <td>"Please wait. Loading..."</td>
   * <td>Default loading string message</td>
   * </tr>
   * <tr>
   * <td>method</td>
   * <td>"post"</td>
   * <td>Default AJAX request method</td>
   * </tr>
   * </table>
   */
  public static final Map<String, String> DEFAULT_PRESENTATION_OPTIONS = new LinkedHashMap<String, String>();

  public static final String OVERLAY_SPECIAL_RESPONSE_ID = "<!-- araOverlaySpecialResponse -->";

  protected static final String MAIN_CHILD_KEY = "m";

  protected static final String OVERLAY_CHILD_KEY = "o";

  protected Map<String, String> presentationOptions = new LinkedHashMap<String, String>();

  protected Widget main;

  protected FlowContextWidget overlay;

  static {
    DEFAULT_PRESENTATION_OPTIONS.put("method", "post");
    DEFAULT_PRESENTATION_OPTIONS.put("overlayClose", "false");
    DEFAULT_PRESENTATION_OPTIONS.put("width", "800");
    DEFAULT_PRESENTATION_OPTIONS.put("slideDownDuration", "0");
    DEFAULT_PRESENTATION_OPTIONS.put("slideUpDuration", "0");
    DEFAULT_PRESENTATION_OPTIONS.put("overlayDuration", "0");
    DEFAULT_PRESENTATION_OPTIONS.put("resizeDuration", "0");
    DEFAULT_PRESENTATION_OPTIONS.put("maxHeight", "0.9");
  }

  public StandardOverlayContainerWidget() {
    this.presentationOptions.putAll(DEFAULT_PRESENTATION_OPTIONS);
  }

  public void setMain(Widget main) {
    this.main = main;
  }

  public void setOverlay(FlowContextWidget overlay) {
    this.overlay = overlay;
  }

  public boolean isOverlayActive() {
    return this.overlay.isNested();
  }

  @Override
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), OverlayContext.class, this);
  }

  @Override
  protected void init() throws Exception {
    super.init();
    Assert.notNull(this.overlay);
    Assert.notNull(this.main);
    addWidget(OVERLAY_CHILD_KEY, this.overlay);
    this.overlay.addNestedEnvironmentEntry(this, OverlayActivityMarkerContext.class,
        new OverlayActivityMarkerContext() {});
    addWidget(MAIN_CHILD_KEY, this.main);
  }

  @Override
  protected void update(InputData input) throws Exception {
    if (isOverlayActive()) {
      this.overlay._getWidget().update(input);
    } else {
      this.main._getWidget().update(input);
    }
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
    assertActiveHierarchy(path);
    super.event(path, input);
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    assertActiveHierarchy(path);
    super.action(path, input, output);
  }

  /**
   * Asserts that the current widget is in the active hierarchy. If not, the execution will fail with an exception.
   * 
   * @param path Path of the widget (from the request).
   * @since 1.1.2
   */
  protected void assertActiveHierarchy(Path path) {
    if (path != null && path.hasNext()) {
      String key = isOverlayActive() ? MAIN_CHILD_KEY : OVERLAY_CHILD_KEY;
      Assert.isTrue(!key.equals(path.getNext()), "Cannot deliver action to wrong hierarchy!");
    }
  }

  @Override
  protected void render(OutputData output) throws Exception {
    if (output.getInputData().getGlobalData().containsKey(OverlayContext.OVERLAY_REQUEST_KEY)) {
      this.overlay._getWidget().render(output);
      if (!isOverlayActive()) {
        // response should be empty as nothing was rendered when overlay did not
        // contain an active flow
        // write out a hack of a response that should be interpreted by
        // Aranea.ModalBox.afterLoad
        HttpServletResponse response = ServletUtil.getResponse(output);
        response.getWriter().write(OVERLAY_SPECIAL_RESPONSE_ID + "\n");
      }
    } else {
      this.main._getWidget().render(output);
      if (!isOverlayActive()) { // overlay has become inactive for some reason
        UpdateRegionContext urCtx = EnvironmentUtil.getUpdateRegionContext(getEnvironment());
        urCtx.disableOnce();
      }
    }
  }

  // FlowContext methods
  public void replace(Widget flow) {
    this.overlay.replace(flow);
  }

  public void replace(Widget flow, Configurator configurator) {
    this.overlay.replace(flow, configurator);
  }

  public void reset(EnvironmentAwareCallback callback) {
    this.overlay.reset(callback);
  }

  public void start(Widget flow, Configurator configurator, Handler<?> handler) {
    this.overlay.start(flow, configurator, handler);
  }

  public void start(Widget flow, Handler<?> handler) {
    this.overlay.start(flow, handler);
  }

  public void start(Widget flow) {
    this.overlay.start(flow);
  }

  /* The presentation options of this overlay. */
  public Map<String, String> getOverlayOptions() {
    return this.presentationOptions;
  }

  public void setOverlayOptions(Map<String, String> presentationOptions) {
    this.presentationOptions = presentationOptions;
  }

  public void finish(Object result) {
    this.overlay.finish(result);
  }

  public void cancel() {
    this.overlay.cancel();
  }
}
