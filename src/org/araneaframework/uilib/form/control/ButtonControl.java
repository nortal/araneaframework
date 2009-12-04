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

package org.araneaframework.uilib.form.control;

import org.araneaframework.uilib.event.OnClickEventListener;
import org.araneaframework.uilib.event.StandardControlEventListenerAdapter;
import org.araneaframework.uilib.support.DataType;

/**
 * This class represents a button.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 */
public class ButtonControl extends BaseControl<String> {

  protected StandardControlEventListenerAdapter eventHelper = new StandardControlEventListenerAdapter();

  /**
   * Creates a new button control.
   */
  public ButtonControl() {}

  /**
   * Creates a new button control with given "onClick" event listener.
   * 
   * @param onClickEventListener The listener that listens to click events of this button.
   * @since 2.0
   */
  public ButtonControl(OnClickEventListener onClickEventListener) {
    addOnClickEventListener(onClickEventListener);
  }

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
    this.eventHelper.addOnClickEventListener(onClickEventListener);
  }

  /**
   * Returns null, because <code>ButtonControl</code> does not have any data.
   * 
   * @return <code>null</code>.
   */
  public DataType getRawValueType() {
    return null;
  }

  @Override
  protected void init() throws Exception {
    super.init();
    setGlobalEventListener(this.eventHelper);
  }

  /**
   * Empty method. Button control is not interested in conversion and validation.
   */
  @Override
  public void convertAndValidate() {}

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * Represents a <code>ButtonControl</code> view model.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
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
     * 
     * @return whether any onClick events have been registered.
     */
    public boolean isOnClickEventRegistered() {
      return this.hasOnClickEventListeners;
    }
  }
}
