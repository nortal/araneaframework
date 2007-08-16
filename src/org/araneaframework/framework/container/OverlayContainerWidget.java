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

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.Assert;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.FlowContext;
import org.araneaframework.framework.OverlayContext;

/**
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public class OverlayContainerWidget extends BaseApplicationWidget implements OverlayContext {

  private static final String OVERLAY_REQUEST_KEY = "araOverlay";

  private static final String MAIN_CHILD_KEY = "m";
  private static final String OVERLAY_CHILD_KEY = "o";

  private Widget main;
  private Widget overlay;

  public void setMain(Widget main) {
    this.main = main;
  }

  public void setOverlay(Widget overlay) {
    this.overlay = overlay;
  }

  public FlowContext getFlowCtx() {
    return (FlowContext) overlay;
  }

  public boolean isOverlayActive() {
    return getFlowCtx().isNested();
  }

  protected Environment getChildWidgetEnvironment() throws Exception {
    return new StandardEnvironment(super.getChildWidgetEnvironment(), OverlayContext.class, this);
  }

  protected void init() throws Exception {
    super.init();
    Assert.notNull(main);
    Assert.notNull(overlay);
    Assert.isInstanceOf(FlowContext.class, overlay, "Overlay Widget must implement FlowContext interface");
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
    if (output.getInputData().getGlobalData().containsKey(OVERLAY_REQUEST_KEY))
      overlay._getWidget().render(output);
    else
      main._getWidget().render(output);
  }

}
