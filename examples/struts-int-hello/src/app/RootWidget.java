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

package app;

import app.aranea.NameWidget;
import app.aranea.StrutsHelloWorldWidget;
import org.araneaframework.framework.container.StandardFlowContainerWidget;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * @author Jevgeni kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 */
public class RootWidget extends BaseUIWidget {
	protected void init() throws Exception {
    addWidget("strutsHelloWorld", new StrutsHelloWorldWidget());
    addWidget("araneaHelloWorld", new StandardFlowContainerWidget(new NameWidget()));
    setViewSelector("root");
	} 
}
