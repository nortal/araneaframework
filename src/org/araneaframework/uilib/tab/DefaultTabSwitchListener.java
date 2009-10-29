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
package org.araneaframework.uilib.tab;

import org.apache.commons.collections.Closure;
import org.araneaframework.uilib.tab.TabContainerContext.TabSwitchListener;

/**
 * A default implementation of {@link TabSwitchListener} that always allows tab
 * switches.
 * 
 * @author Martti Tamm (martti@araneaframework.org)
 * @since 1.2.2
 */
public class DefaultTabSwitchListener implements TabSwitchListener {

  public boolean onSwitch(TabWidget selectedTab, TabWidget newTab,
      Closure switchClosure) {
    return true;
  }
}
