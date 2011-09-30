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

package org.araneaframework.framework;

import java.io.Serializable;
import java.util.Map;

/**
 * Another context interface that, in contrast to standard {@link FlowContext}, deals with flow logic that is running in
 * overlay mode (a thread of logic running on top of the current page).
 * <p>
 * Using <code>OverlayContext</code> assumes that the visual part of the application also supports it. One must register
 * the <code>OverlayContext</code> in JSP with <code>&lt;ui:registerOverlay/&gt;</code> tag.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public interface OverlayContext extends FlowContext {

  /**
   * The request parameter name identifying that the request comes from an overlay.
   */
  String OVERLAY_REQUEST_KEY = "araOverlay";

  /**
   * Specifies whether the overlay mode is currently running and visible to the user.
   * 
   * @return whether the overlay mode is currently active.
   */
  boolean isOverlayActive();

  /**
   * Allows to specify custom overlay options as a <code>Map</code>. These options will be forwarded to the script that
   * handles the overlay mode visually.
   * 
   * @param options options as <code>&lt;String, String&gt;</code> pair.
   */
  void setOverlayOptions(Map<String, Object> options);

  /**
   * Provides the current options for overlay rendering.
   * 
   * @return the current options for overlay rendering.
   */
  Map<String, Object> getOverlayOptions();

  /**
   * This is a marker interface to say that overlay mode is active by putting this interface class into the
   * <code>Environment</code>. The tags may use the <code>Environment</code> to check the mode.
   * 
   * @author Alar Kvell (alar@araneaframework.org)
   * @since 1.1
   */
  interface OverlayActivityMarkerContext extends Serializable {
  }

}
