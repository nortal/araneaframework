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
package org.araneaframework.uilib.util;

import org.araneaframework.uilib.ConfigurationContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public abstract class ConfigurationContextUtil {
  private ConfigurationContextUtil() {}
  
  public static boolean isBackgroundFormValidationEnabled(ConfigurationContext cctx) {
    Boolean b = (Boolean) cctx.getEntry(ConfigurationContext.SEAMLESS_BACKGROUND_FORM_VALIDATION);
    return b == null ? false : b.booleanValue();
  }
  
  public static Long getDefaultListItemsOnPage(ConfigurationContext cctx) {
    Long l = (Long) cctx.getEntry(ConfigurationContext.DEFAULT_LIST_ITEMS_ON_PAGE);
    return l;
  }
}
