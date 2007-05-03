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

package org.araneaframework.http;

import java.io.Serializable;

/**
 * Update region filter, supporting updating of HTML page regions and sending
 * miscellaneous data back via AJAX requests.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public interface UpdateRegionContext extends Serializable {

  /**
   * Disable updateregion filter during this request only. Already rendered data
   * will be discarded. In client-side, transactionId will be set inconsistent
   * and page will be forced to reload in order to perform full render.
   */
  void disableOnce();

  /**
   * Notify that a document region is rendered by the specified widget. The list
   * of document regions is cleared before every full render. Updateregion tags
   * should always call this, so that when updateregion filter is invoked, it is
   * known which widget to render for a particular document region.
   * 
   * @param documentRegionId document region id 
   * @param widgetId id of the widget that will render the document region
   */
  void addDocumentRegion(String documentRegionId, String widgetId);

  /**
   * Add a handler for custom region.
   * 
   * @param name
   *          handler name. Javascript handler with the same name will be called
   *          in client-side.
   * @param handler
   *          handler
   */
  void addRegionHandler(String name, RegionHandler handler);

  /**
   * A way to pass custom data back to client-side via AJAX requests.
   */
  interface RegionHandler extends Serializable {

    /**
     * Will be called only when updateregion filter is activated on AJAX
     * requests.
     * 
     * @return content that will be passed to Javascript region handler in
     *         client-side. If <code>null</code> is returned, then Javascript
     *         region handler will not be called.
     */
    String getContent() throws Exception;

  }

}
