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

import java.util.Iterator;
import java.util.Map;
import org.araneaframework.Widget;
import org.araneaframework.core.StandardWidget;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class StandardViewPortService extends StandardWidget {
  //*******************************************************************
  // CONSTANTS
  //*******************************************************************
  
  private Map children;
  
  //*******************************************************************
  // PUBLIC METHODS
  //*******************************************************************  
  
  public void setChildren(Map children) {
    this.children = children;
  }
  
  //*******************************************************************
  // PROTECTED METHODS
  //*******************************************************************

  protected void init() throws Exception {
    for (Iterator i = children.entrySet().iterator(); i.hasNext();) {
      Map.Entry entry = (Map.Entry) i.next();
      addWidget(entry.getKey(), (Widget) entry.getValue());
    }    
  }
}
