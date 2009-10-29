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

package org.araneaframework.uilib.list.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * Methods to convert a flat {@link Map} into hierarchical and vice-versa.
 * <p>
 * Plain map is one which may contain keys like "aaa.bb.c" and has no other {@link Map}s as values.
 * </p>
 * <p>
 * hierarchical map is one which cannot contain keys like "aaa.bb.c". Instead a key of "aaa" corresponds to another
 * sub-map that has a key of "bb" etc.
 * </p>
 * 
 * @author Rein Raudjärv
 */
public class MapUtil {

  public static final String MAP_KEY_SEPARATOR = ".";

  /**
   * Convert plain map into a hierarchical.
   */
  public static Map<String, Object> convertToHierachyMap(Map<String, Object> plainMap) {
    Map<String, Object> result = new HashMap<String, Object>();
    for (Map.Entry<String, Object> entry : plainMap.entrySet()) {
      putByFullKey(result, entry.getKey(), entry.getValue());
    }
    return result;
  }

  /**
   * Convert hierarchical map into a plain.
   */
  public static Map<String, Object> convertToPlainMap(Map<String, Object> hierarchyMap) {
    return convertToPlainMap(hierarchyMap, "");
  }

  /**
   * Gets an entry from the hierarchical map.
   * 
   * @param map the hierarchical map.
   * @param key the full key of the entry.
   * @return the value.
   */
  @SuppressWarnings("unchecked")
  public static Object getByFullKey(Map<String, Object> map, String key) {
    if (key.indexOf(MAP_KEY_SEPARATOR) != -1) {
      // The key contains a dot

      String subMapKey = StringUtils.substringBefore(key, MAP_KEY_SEPARATOR);
      String nextKey = key.substring(subMapKey.length() + 1);

      return getByFullKey((Map<String, Object>) map.get(subMapKey), nextKey);
    }
    return map.get(key);
  }

  /**
   * Put an entry into hierarchical map.
   * <p>
   * The corresponding sub-maps are created automatically if necessary.
   * 
   * @param map the hierarchical map.
   * @param key the full key of the entry.
   * @param value value of the entry.
   * @return old value.
   */
  @SuppressWarnings("unchecked")
  public static <V> V putByFullKey(Map<String, Object> map, String key, V value) {
    if (key.indexOf(MAP_KEY_SEPARATOR) != -1) {
      // The key contains a dot
      String subMapKey = StringUtils.substringBefore(key, MAP_KEY_SEPARATOR);
      String nextKey = key.substring(subMapKey.length() + 1);

      Map<String, Object> subMap;
      if (map.containsKey(subMapKey)) {
        subMap = (Map<String, Object>) map.get(subMapKey);
      } else {
        subMap = new HashMap<String, Object>();
        map.put(subMapKey, subMap);
      }
      return putByFullKey(subMap, nextKey, value);
    }
    return (V) map.put(key, value);
  }

  /**
   * Convert hierarchical map into a plain.
   * <p>
   * All entries returned have a value with the specified <code>keyPrefix</code>.
   */
  @SuppressWarnings("unchecked")
  private static <V> Map<String, Object> convertToPlainMap(Map<String, Object> hierarchyMap, String keyPrefix) {
    Map<String, Object> result = new HashMap<String, Object>();
    for (Map.Entry<String, Object> entry : hierarchyMap.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (value instanceof Map) {
        Map<String, Object> subMap = (Map<String, Object>) value; // A sub-map found
        result.putAll(convertToPlainMap(subMap, keyPrefix + key + MAP_KEY_SEPARATOR));
      } else {
        // A normal entry
        result.put(keyPrefix + key, value);
      }
    }
    return result;
  }

}
