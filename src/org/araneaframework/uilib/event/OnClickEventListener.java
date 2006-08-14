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

package org.araneaframework.uilib.event;



/**
 * This interface defines a "on click" event listener that is usually attached
 * to a button. This listener will receive notification when the control is
 * being clicked by the user
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * 
 */
public interface OnClickEventListener  extends java.io.Serializable {
  /**
   * This constant is the name of the event, that this listener listens to.
   */  
  public static final String ON_CLICK_EVENT = "onClicked";

  /**
   * UiEvent callback function that is called when the registered event occurs.
   * @throws Exception if something goes wrong.
   */
  public void onClick() throws Exception;
}
