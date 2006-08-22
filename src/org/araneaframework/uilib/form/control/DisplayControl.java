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


/**
 * This class represents a read only control, that is technically it is not a 
 * true control, but just a label displaying the text to the user.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class DisplayControl extends BaseControl {
  /**
   * Returns <code>false</code>.
   * @return <code>false</code>.
   */
  public boolean isRead() {
    return false;
  }

  public String getRawValueType() {
    return "Object";
  }
  
  //*********************************************************************
  //* INTERNAL METHODS
  //*********************************************************************  	
 
  /**
   * Returns {@link ViewModel}.
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }
  
  protected void process() throws Exception {
    innerData = value;
    
    super.process();    
  }

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************    
  
  /**
   * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
   * 
   */
  public class ViewModel extends BaseControl.ViewModel {

    private Object value;
    
    /**
     * Takes an outer class snapshot.     
     */    
    public ViewModel() {
      this.value = innerData;
    }       
    
    /**
     * Returns value.
     * @return value.
     */
    public Object getValue() {
      return value;
    }
    
  }  
}
