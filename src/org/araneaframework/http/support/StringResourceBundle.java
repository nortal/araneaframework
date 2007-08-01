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

package org.araneaframework.http.support;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class StringResourceBundle extends ResourceBundle {
	public Enumeration getKeys() {
		return null;
	}

  protected Object handleGetObject(String key) {
	if (key.length() > 0 && key.charAt(0) == '#')
      return key.substring(1);
	return null;
  }
}
