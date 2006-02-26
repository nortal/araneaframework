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

package org.araneaframework.uilib.list.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapUtil {
	public static final String MAP_KEY_SEPARATOR = ".";
	
	public static Object getByFullKey(Map map, String key) {
		if (key.indexOf(MAP_KEY_SEPARATOR) != -1) {
			String subMapKey = key.substring(0, key.indexOf(MAP_KEY_SEPARATOR));
			String nextKey =  key.substring(subMapKey.length() + 1);
			
			return getByFullKey((Map) map.get(subMapKey), nextKey);
		}
		return map.get(key);
	}
	
	public static Object putByFullKey(Map map, String key, Object value) {
		if (key.indexOf(MAP_KEY_SEPARATOR) != -1) {
			String subMapKey = key.substring(0, key.indexOf(MAP_KEY_SEPARATOR));
			String nextKey =  key.substring(subMapKey.length() + 1);
			
			Map subMap;
			if (map.containsKey(subMapKey)) {
				subMap = (Map) map.get(subMapKey);
			} else {
				subMap = new HashMap();
				map.put(subMapKey, subMap);
			}
			return putByFullKey(subMap, nextKey, value);
		}
		return map.put(key, value);
	}
	
	public static Map convertToHierachyMap(Map plainMap) {
		Map result = new HashMap();
		Iterator i = plainMap.entrySet().iterator();
		while (i.hasNext()) {
			Entry entry = (Entry) i.next();
			putByFullKey(result, (String) entry.getKey(), entry.getValue());							
		}
		return result;
	}
	
	public static Map convertToPlainMap(Map hierarchyMap) {
		return convertToPlainMap(hierarchyMap, "");
	}
	
	private static Map convertToPlainMap(Map hierarchyMap, String keyPrefix) {
		Map result = new HashMap();
		Iterator i = hierarchyMap.entrySet().iterator();
		while (i.hasNext()) {
			Entry entry = (Entry) i.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			
			if (value instanceof Map) {
				result.putAll(convertToPlainMap((Map) value, key + MAP_KEY_SEPARATOR));
			} else {
				result.put(keyPrefix + key, value);
			}						
		}
		return result;
	}	
}
