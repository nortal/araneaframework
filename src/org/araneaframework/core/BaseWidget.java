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

package org.araneaframework.core;

import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.Widget;

/**
 * Non-composite widget component.
 * <p>
 * Compared to BaseService has the following extra
 * functionality:
 * <ul>
 * <li>update(InputData) - update the state of the widget with the data in InputData</li>
 *  <li>event(Path, InputData) - handling a event</li>
 *  <li>process() - postprocessing</li>
 *  <li>render(OutputData) - rendering this Widget to OutputData</li>
 * </ul>
 * </p>
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class BaseWidget extends BaseService implements Widget {

  /**
   * Returns a widget's internal implementation.
   * @return the widget's implementation
   */
  public Widget.Interface _getWidget() {
    return new WidgetImpl();
  }
    
  protected class WidgetImpl implements Widget.Interface {
    
    public void update(InputData input) throws Exception {
      _startCall();
      currentInputData = input;
      try {
        BaseWidget.this.update(input);
      }
      catch (Exception e) {
        handleException(e);
      }
      finally {
        currentInputData = null;
        _endCall();
      } 
    }
    
    public void event(Path path, InputData input) throws Exception {
      _startCall();
      currentInputData = input;
      try {
        BaseWidget.this.event(path, input);
      }
      catch (Exception e) {
        handleException(e);
      }
      finally {
        currentInputData = null;
        _endCall();
      }
    }
    
    public void process() throws Exception {
      _startCall();
      try {
        BaseWidget.this.process();
      }
      catch (Exception e) {
        handleException(e);
      }
      finally {
        _endCall();
      }
    }
    
    public void render(OutputData output) throws Exception {
      _startCall();
      currentOutputData = output;
      try {
        BaseWidget.this.render(output);
      }
      catch (Exception e) {
        handleException(e);
      }
      finally {
        currentOutputData = null;
        _endCall();
      }
    }
  }   
    
  // Callbacks
  protected void update(InputData input) throws Exception {}
  protected void event(Path path, InputData input) throws Exception {}
  protected void process() throws Exception {}
  protected void render(OutputData output) throws Exception {}
  
  protected InputData getCurrentInput() {
  	InputData input = super.getCurrentInput();
  	OutputData output = super.getCurrentOutput();

  	// lets try to give a not null answer to the user
  	if (input == null && output != null) {
  			return output.getInputData();
  	}
  	
  	return super.getCurrentInput();
  }
  
  protected OutputData getCurrentOutput() {
  	OutputData output = super.getCurrentOutput();
  	InputData input = super.getCurrentInput();
  	if (output == null && input != null) {
  		return input.getOutputData();
  	}
  	
  	return super.getCurrentOutput();
  }
}
