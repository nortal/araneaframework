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

package org.araneaframework.uilib.event;

import java.io.Serializable;

/**
 * This interface defines <i>any kind</i> event listener that is usually attached to a form control. This listener will
 * receive notification when the client-side logic fires it. This interface was added later (in Aranea 2.0) to also
 * support events other than "onChange" and "onClick". However, {@link OnChangeEventListener} and
 * {@link OnClickEventListener} implementations are still supported for backward compatibility.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 2.0
 */
public interface CustomEventListener extends Serializable {

  /**
   * Event callback function that is called when the registered event occurs. Since one event listener can listen to
   * several events, the parameter <code>event</code> is provided to identify, which event was fired.
   * 
   * @param event The name of the event that was fired.
   * @throws Exception if something goes wrong, will break application execution.
   */
  public void onEvent(String event) throws Exception;

}
