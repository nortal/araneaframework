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

import org.araneaframework.framework.LocalizationContext;

import java.io.Serializable;
import java.util.Map;

/**
 * A way to pass custom data back to client-side via AJAX requests.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 * @since 1.1
 */
public interface UpdateRegionProvider extends Serializable {

  /**
   * Will be called only when updateregion filter is activated on AJAX requests.
   * @param locCtx TODO
   * 
   * @return map containing regions that will be passed to client-side. Map must
   *         have region names as <code>String</code> keys and region contents
   *         as <code>String</code> values. Region contents will be passed to
   *         client-side Javascript region handler, designated by region name.
   *         May return <code>null</code> or empty map. If map contains a
   *         <code>null</code> value, then that region is not included in
   *         response.
   */
  Map<String, String> getRegions(LocalizationContext locCtx);

}
