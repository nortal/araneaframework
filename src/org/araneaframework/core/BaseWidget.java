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

import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;
import org.araneaframework.core.util.ExceptionUtil;

/**
 * Non-composite widget component.
 * <p>
 * Compared to BaseService has the following extra
 * functionality:
 * <ul>
 * <li>update(InputData) - update the state of the widget with the data in InputData</li>
 *  <li>event(Path, InputData) - handling a event</li>
 *  <li>render(OutputData) - rendering this Widget to OutputData</li>
 * </ul>
 * </p>
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class BaseWidget extends BaseService implements Widget {

  private static final long serialVersionUID = 1L;

  /**
   * Returns a widget's internal implementation.
   * @return the widget's implementation
   */
  public Widget.Interface _getWidget() {
    return new WidgetImpl();
  }
    
  protected class WidgetImpl implements Widget.Interface {
    
    private static final long serialVersionUID = 1L;

    public void update(InputData input) {
      Assert.notNullParam(this, input, "input");

      _startCall();
      currentInputData = input;
      try {
        BaseWidget.this.update(input);
      }
      catch (Exception e) {
        try {
          handleWidgetException(e);
        }
        catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      }
      finally {
        currentInputData = null;
        _endCall();
      } 
    }
    
    public void event(Path path, InputData input) {
      Assert.notNullParam(this, input, "input");
      
      _startCall();
      currentInputData = input;
      try {
        BaseWidget.this.event(path, input);
      }
      catch (Exception e) {
        try {
          handleWidgetException(e);
        }
        catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      }
      finally {
        currentInputData = null;
        _endCall();
      }
    }
    
    public void render(OutputData output) {
      Assert.notNullParam(this, output, "output");
      
      _startCall();
      currentOutputData = output;
      try {
        BaseWidget.this.render(output);
      }
      catch (Exception e) {
        try {
          handleWidgetException(e);
        }
        catch (Exception e2) {
          ExceptionUtil.uncheckException(e2);
        }
      }
      finally {
        currentOutputData = null;
        _endCall();
      }
    }
  }   
    
  protected void handleWidgetException(Exception e) throws Exception {
    handleException(e);
  }
    
  // Callbacks
  protected void update(InputData input) throws Exception {}
  protected void event(Path path, InputData input) throws Exception {}
  protected void render(OutputData output) throws Exception {}
  
  protected InputData getInputData() {
  	InputData input = super.getInputData();
  	OutputData output = super.getOutputData();

  	// lets try to give a not null answer to the user
  	if (input == null && output != null) {
      return output.getInputData();
  	}
  	
  	// as last resort, look in the Environment -- when result cannot be found be
  	// before, this probably means that we are still in widgets init() method.
  	if (input == null)
      input = getEnvironment().getEntry(InputData.class);
  	
  	return input;
  }
  
  protected OutputData getOutputData() {
  	OutputData output = super.getOutputData();
  	InputData input = super.getInputData();
  	if (output == null && input != null) {
  		return input.getOutputData();
  	}

  	// as last resort, look in the Environment -- when result cannot be found be
  	// before, this probably means that we are still in widgets init() method.
  	if (output == null)
      output = getEnvironment().getEntry(OutputData.class);

  	return output;
  }
}
