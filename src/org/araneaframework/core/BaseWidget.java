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

package org.araneaframework.core;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.util.Assert;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * The base class for all Aranea widget built on top of <tt>BaseService</tt>. This class does not support composite
 * design pattern, thus cannot contain child items by default.
 * <p>
 * Compared to BaseService has the following extra functionality:
 * <ul>
 * <li><code>update(InputData)</code> - update the state of the widget with the data in InputData</li>
 * <li><code>event(Path, InputData)</code> - handling a event</li>
 * <li><code>render(OutputData)</code> - rendering this Widget to OutputData</li>
 * </ul>
 * 
 * @author Toomas Römer (toomas@webmedia.ee)
 */
public class BaseWidget extends BaseService implements Widget {

  public Widget.Interface _getWidget() {
    return new WidgetImpl();
  }

  /**
   * A method for widget to handle exceptions when a widget update/event/render method should fail. Default
   * implementation just delegates exception handling to {@link BaseComponent#handleException(Exception)}.
   * 
   * @param e An exception that has occurred.
   * @throws Exception Any exception that may occur during exception handling.
   */
  protected void handleWidgetException(Exception e) throws Exception {
    handleException(e);
  }

  /**
   * A callback that is called upon widget request cycle beginning. Among the three callbacks, this is called first.
   * 
   * @param input Input data for the widget or for its child components.
   * @throws Exception Any exception that my occur.
   */
  protected void update(InputData input) throws Exception {
  }

  /**
   * A callback that is called when an event is sent to this widget. Among the three callbacks, this is called second.
   * 
   * @param path The path information about the event target.
   * @param input Input data for the widget or for its child components.
   * @throws Exception Any exception that my occur.
   */
  protected void event(Path path, InputData input) throws Exception {
  }

  /**
   * A callback that is called upon widget rendering. Among the three callbacks, this is called last.
   * 
   * @param output Output data for the widget or for its child components.
   * @throws Exception Any exception that my occur.
   */
  protected void render(OutputData output) throws Exception {
  }

  @Override
  protected InputData getInputData() {
    InputData input = super.getInputData();
    OutputData output = super.getOutputData();

    // lets try to give a not null answer to the user
    if (input == null && output != null) {
      return output.getInputData();
    }

    // as last resort, look in the Environment -- when result cannot be found be
    // before, this probably means that we are still in widgets init() method.
    if (input == null) {
      input = getEnvironment().getEntry(InputData.class);
    }

    return input;
  }

  @Override
  protected OutputData getOutputData() {
    OutputData output = super.getOutputData();
    InputData input = super.getInputData();
    if (output == null && input != null) {
      return input.getOutputData();
    }

    // as last resort, look in the Environment -- when result cannot be found be
    // before, this probably means that we are still in widgets init() method.
    if (output == null) {
      output = getEnvironment().getEntry(OutputData.class);
    }

    return output;
  }

  /**
   * Base widget implementation that uses synchronization to perform service actions to make sure no one else uses it at
   * the same time.
   */
  protected class WidgetImpl implements Widget.Interface {

    public void update(InputData input) {
      Assert.notNullParam(this, input, "input");

      _startCall();
      BaseWidget.this.currentInputData = input;
      try {
        BaseWidget.this.update(input);
      } catch (Exception e) {
        try {
          handleWidgetException(e);
        } catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      } finally {
        BaseWidget.this.currentInputData = null;
        _endCall();
      }
    }

    public void event(Path path, InputData input) {
      Assert.notNullParam(this, input, "input");

      _startCall();
      BaseWidget.this.currentInputData = input;

      try {
        BaseWidget.this.event(path, input);
      } catch (Exception e) {
        try {
          handleWidgetException(e);
        } catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      } finally {
        BaseWidget.this.currentInputData = null;
        _endCall();
      }
    }

    public void render(OutputData output) {
      Assert.notNullParam(this, output, "output");

      _startCall();
      BaseWidget.this.currentOutputData = output;

      try {
        BaseWidget.this.render(output);
      } catch (Exception e) {
        try {
          handleWidgetException(e);
        } catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      } finally {
        BaseWidget.this.currentOutputData = null;
        _endCall();
      }
    }
  }
}
