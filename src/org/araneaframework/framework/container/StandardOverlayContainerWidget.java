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
import org.araneaframework.http.StateVersioningContext;
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
  public static final Map<String, String> DEFAULT_PRESENTATION_OPTIONS = new LinkedHashMap<String, String>();
  private static final String OVERLAY_SPECIAL_RESPONSE_ID = "<!-- araOverlaySpecialResponse -->";

  private static final String MAIN_CHILD_KEY = "m";
  private static final String OVERLAY_CHILD_KEY = "o";
  
  protected Map<String, String> presentationOptions = new LinkedHashMap<String, String>();

  private Widget main;
  private FlowContextWidget overlay;
  
  static {
    DEFAULT_PRESENTATION_OPTIONS.put("method", "post");
    DEFAULT_PRESENTATION_OPTIONS.put("overlayClose", "false");
    DEFAULT_PRESENTATION_OPTIONS.put("width", "800");
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

  @Override
  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), OverlayContext.class, this);
  }

  @Override
  protected void init() throws Exception {
    super.init();
    Assert.notNull(main);
    Assert.notNull(overlay);
    addWidget(MAIN_CHILD_KEY, main);
    addWidget(OVERLAY_CHILD_KEY, overlay);
    overlay.addNestedEnvironmentEntry(this, OverlayActivityMarkerContext.class, new OverlayActivityMarkerContext(){});
  }

  @Override
  protected void update(InputData input) throws Exception {
    if (isOverlayActive())
      overlay._getWidget().update(input);
    else
      main._getWidget().update(input);
  }

  @Override
  protected void event(Path path, InputData input) throws Exception {
  	assertActiveHierarchy(path,  "Cannot deliver event to wrong hierarchy!");
    super.event(path, input);
  }

  @Override
  protected void action(Path path, InputData input, OutputData output) throws Exception {
  	assertActiveHierarchy(path,  "Cannot deliver action to wrong hierarchy!");
    super.action(path, input, output);
  }

  /**
	 * Asserts that the current widget is in the active hierarchy. If not, the
	 * execution will fail with an exception.
	 * 
	 * @param path Path of the widget (from the request).
	 * @param message A description message to include with the exception.
	 * @since 1.1.2
	 */
	protected void assertActiveHierarchy(Path path, String message) {
		if (path != null && path.hasNext()) {
			String key = isOverlayActive() ? MAIN_CHILD_KEY : OVERLAY_CHILD_KEY;
			Assert.isTrue(!key.equals(path.getNext()), message);
		}
	}

	@Override
  protected void render(OutputData output) throws Exception {
    if (output.getInputData().getGlobalData().containsKey(OverlayContext.OVERLAY_REQUEST_KEY)) {
      overlay._getWidget().render(output);

      if (!isOverlayActive()) {
        // response should be empty as nothing was rendered when overlay did not contain an active flow
        // write out a hack of a response that should be interpreted by Aranea.ModalBox.afterLoad
        HttpServletResponse response = ServletUtil.getResponse(output);
        response.getWriter().write(OVERLAY_SPECIAL_RESPONSE_ID + "\n");

        StateVersioningContext ctx = overlay.getEnvironment().getEntry(StateVersioningContext.class);
        if (ctx != null) {
          String stateId = getInputData().getGlobalData().get(StateVersioningContext.STATE_ID_KEY);
          ctx.saveState(stateId);
          response.getWriter().write("<!--" + stateId + "-->\n");
        }
      }
    } else {
      main._getWidget().render(output);
      if (!isOverlayActive()) { // overlay has become inactive for some reason
        UpdateRegionContext urCtx = EnvironmentUtil.getUpdateRegionContext(getEnvironment());
        urCtx.disableOnce();
      }
    }
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
  public Map<String, String> getOverlayOptions() {
    return presentationOptions;
  }

  public void setOverlayOptions(Map<String, String> presentationOptions) {
    this.presentationOptions = presentationOptions; 
  }
}
