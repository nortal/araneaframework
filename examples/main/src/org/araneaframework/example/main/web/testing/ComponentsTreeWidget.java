/*
 * Copyright 2007 Webmedia Group Ltd.
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

package org.araneaframework.example.main.web.testing;

import org.araneaframework.core.util.SessionTreeRenderingMessage;
import org.araneaframework.example.main.TemplateBaseWidget;

/**
 * A demo widget showing the running Aranea components as a tree structure.
 * 
 * @author Martti Tamm (martti <i>at</i> araneaframework <i>dot</i> org)
 * @since 1.2.3
 */
public class ComponentsTreeWidget extends TemplateBaseWidget {

  protected void init() throws Exception {
    setViewSelector("testing/componentsTree");
    putViewData("tree", SessionTreeRenderingMessage.execute(getEnvironment()).getResultStr());
  }
}
