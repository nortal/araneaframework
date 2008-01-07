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

package org.araneaframework.framework.container;

import java.util.Map;
import org.apache.commons.collections.map.LinkedMap;
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

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public class StandardOverlayContainerWidget extends BaseApplicationWidget implements OverlayContext {
  /**
   * <p> 
   * Map containing the default overlay presentation options. 
   * Default values are as follows:</p>
   * <ul>
   *   <li>method: post</li>
   *   <li>overlayClose: false</li>
   *   <li>width: 800</li>
   *   <li>slideDownDuration: 0.0</li>
   *   <li>slideUpDuration: 0.0</li>
   *   <li>overlayDuration: 0.0</li>
   *   <li>resizeDuration: 0.0</li>
   * </ul>
   */
  public static final Map DEFAULT_PRESENTATION_OPTIONS = new LinkedMap();
  private static final String OVERLAY_REQUEST_KEY = "araOverlay";

  private static final String MAIN_CHILD_KEY = "m";
  private static final String OVERLAY_CHILD_KEY = "o";
  
  protected Map presentationOptions = new LinkedMap();

  private Widget main;
  private FlowContextWidget overlay;
  
  static {
    DEFAULT_PRESENTATION_OPTIONS.put("method", "post");
    DEFAULT_PRESENTATION_OPTIONS.put("overlayClose", Boolean.FALSE);
    DEFAULT_PRESENTATION_OPTIONS.put("width", new Integer(800));
    DEFAULT_PRESENTATION_OPTIONS.put("slideDownDuration", String.valueOf(0.0));
    DEFAULT_PRESENTATION_OPTIONS.put("slideUpDuration", String.valueOf(0.0));
    DEFAULT_PRESENTATION_OPTIONS.put("overlayDuration", String.valueOf(0.0));
    DEFAULT_PRESENTATION_OPTIONS.put("resizeDuration", String.valueOf(0.0));
  }

  {
    presentationOptions.putAll(DEFAULT_PRESENTATION_OPTIONS);
  }

  public void setMain(Widget main) {
    this.main = main;
  }

  public void setOverlay(FlowContextWidget overlay) {
    this.overlay = overlay;
  }

  public boolean isOverlayActive() {
    return overlay.isNested();
  }

  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), OverlayContext.class, this);
  }

  protected void init() throws Exception {
    super.init();
    Assert.notNull(main);
    Assert.notNull(overlay);
    addWidget(MAIN_CHILD_KEY, main);
    addWidget(OVERLAY_CHILD_KEY, overlay);
  }

  protected void update(InputData input) throws Exception {
    if (isOverlayActive())
      overlay._getWidget().update(input);
    else
      main._getWidget().update(input);
  }

  protected void event(Path path, InputData input) throws Exception {
    if (path != null && path.hasNext()) {
      Object next = path.getNext();
      Assert.isTrue(!(isOverlayActive() ? MAIN_CHILD_KEY : OVERLAY_CHILD_KEY).equals(next), "Cannot deliver event to wrong hierarchy!");
    }
    super.event(path, input);
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    if (path != null && path.hasNext()) {
      Object next = path.getNext();
      Assert.isTrue(!(isOverlayActive() ? MAIN_CHILD_KEY : OVERLAY_CHILD_KEY).equals(next), "Cannot deliver action to wrong hierarchy!");
    }
    super.action(path, input, output);
  }

  protected void render(OutputData output) throws Exception {
    if (output.getInputData().getGlobalData().containsKey(OVERLAY_REQUEST_KEY)) {
        overlay._getWidget().render(output);
    }
    else
      main._getWidget().render(output);
  }

  // FlowContext methods
  public void replace(Widget flow) {
    overlay.replace(flow);
  }

  public void replace(Widget flow, Configurator configurator) {
    overlay.replace(flow, configurator);
  }

  public void reset(EnvironmentAwareCallback callback) {
    overlay.reset(callback);
  }

  public void start(Widget flow, Configurator configurator, Handler handler) {
    overlay.start(flow, configurator, handler);
  }

  public void start(Widget flow, Handler handler) {
    overlay.start(flow, handler);
  }

  public void start(Widget flow) {
    overlay.start(flow);
  }

  /* The presentation options of this overlay. */
  public Map getOverlayOptions() {
    return presentationOptions;
  }

  public void setOverlayOptions(Map presentationOptions) {
    this.presentationOptions = presentationOptions; 
  }
}
