/**
 * Copyright 2006-2007 Webmedia Group Ltd.
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

package org.araneaframework.example.blank;

import org.araneaframework.uilib.core.BaseUIWidget;
import org.araneaframework.uilib.core.MenuContext;

public class BlankWidget extends BaseUIWidget {
	protected void init() throws Exception {
		setViewSelector("blankwidget");
	}
	
	public String getSelectedMenuPath() {
		return ((MenuWidget)getEnvironment().getEntry(MenuContext.class)).getSelectedPath();
	}
}
