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

package org.araneaframework.http.util;

import java.util.HashMap;
import java.util.Map;
import org.araneaframework.OutputData;

/**
 * Utility class for setting system form's fields.
 * 
 * @author Toomas Römer <toomas@webmedia.ee>
 */
public abstract class ClientStateUtil {
	public static final String SYSTEM_FORM_STATE = "systemFormState";

	/**
	 * Adds a key value pair to a map in the OutputData that system form uses to
	 * print global fields. In global data under SYSTEM_FORM_STATE is map of
	 * all the key value pairs that are printed to every system form. Every component
	 * can add values to that map. This is a convenience method for adding values without
	 * checking for the existence of the map in the OutputData.
	 * 
	 * @param key the key of the value.
	 * @param value the value under the key.
	 * @param output OutputData that will be enriched with the map.
	 */
	public static void put(String key, String value, OutputData output) {
		Map map = (Map) output.getAttribute(SYSTEM_FORM_STATE);

		if (map == null) {
			map = new HashMap();
			output.pushAttribute(SYSTEM_FORM_STATE, map);
		}
		map.put(key, value);
	}
}
