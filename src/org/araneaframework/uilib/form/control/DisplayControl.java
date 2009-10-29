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

import org.araneaframework.uilib.support.DataType;

/**
 * This class represents a read only control, that is technically it is not a true control, but just a label displaying
 * the text to the user.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * 
 */
public class DisplayControl extends BaseControl<Object> {

  /**
   * {@inheritDoc}
   * <p>
   * Due to the purpose of <code>DisplayControl</code>, this methods always returns <code>false</code>.
   * 
   * @return <code>false</code>.
   */
  @Override
  public boolean isRead() {
    return false;
  }

  public DataType getRawValueType() {
    return new DataType(Object.class);
  }

  /**
   * Returns the {@link ViewModel} of this <code>DisplayControl</code> for rendering the control.
   * 
   * @return The {@link ViewModel} of this <code>DisplayControl</code>.
   */
  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  /**
   * The view model implementation of <code>DisplayControl</code>. The view model provides the data for tags to render
   * the control.
   * 
   * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
   */
  public class ViewModel extends BaseControl<Object>.ViewModel {

    private Object value;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      this.value = DisplayControl.this.innerData;
    }

    /**
     * Returns the value of <code>DisplayControl</code>.
     * 
     * @return The value of <code>DisplayControl</code>.
     */
    public Object getValue() {
      return this.value;
    }
  }
}
