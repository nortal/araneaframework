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

package org.araneaframework.uilib.form.control;

import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;


/**
 * This class represents a button.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class ButtonControl extends BaseControl<String> {

  //*********************************************************************
  // FIELDS
  //*********************************************************************  
  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();


  /**
   * Returns true
   */
  @Override
  public boolean isRead() {
    return true;
  }
  
	/**
   * @param onClickEventListener {@link OnClickEventListener} which is called when the control is clicked.
   * 
	 * @see StandardControlEventListenerAdapter#addOnClickEventListener(OnClickEventListener)
	 */
	public void addOnClickEventListener(OnClickEventListener onClickEventListener) {
		eventHelper.addOnClickEventListener(onClickEventListener);
	}  
	
  /**
   * Returns null.
   * @return null.
   */
  public String getRawValueType() {
    return null;
  }
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
	
  @Override
  protected void init() throws Exception {
    super.init();
    
    setGlobalEventListener(eventHelper);
  }
  
  /**
   * Empty method
   */
  @Override
  protected void readFromRequest(HttpInputData request) {
    // Button control is not interested in what is submitted
  }

  /**
   * Empty method
   */
  @Override
  public void convertAndValidate() {
    // Button control is not interested in conversion and validation
  }

  /**
   * Does nothing
   */
  protected void prepareResponse() {
    // Button control does not have data
  }

  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * Represents a <code>ButtonControl</code> view model.
   * 
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends BaseControl<String>.ViewModel {

    private boolean hasOnClickEventListeners;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.hasOnClickEventListeners = ButtonControl.this.eventHelper.hasOnClickEventListeners();
    }
    
    /**
     * Returns whether any onClick events have been registered.
     * @return whether any onClick events have been registered.
     */
    public boolean isOnClickEventRegistered() {
      return hasOnClickEventListeners;
    }
  }  
}
