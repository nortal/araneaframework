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

package org.araneaframework.http;

import java.io.Serializable;

/**
 * @author Maksim Boiko <mailto:max@webmedia.ee>
 */
public interface WindowFocusPositionContext extends Serializable {

  /**
   * Sets focus to element by concatination of given parameters. The focused element should be
   * ${widgetId}.${formId}.${elementId}
   */
  void setFocusToElement(String widgetId, String formId, String elementId);
  
  /**
   * Sets focus to element using its full id path.
   */
  void setFocusToElement(String fullElementId);

  /**
   * Returnes full element id of element that will receive focus after rendering.
   */
  String getFocusedElement();
  
  /**
   * Resets focus, after execution of this method no focus will set on page rendering.
   */
  void resetFocus();
}
