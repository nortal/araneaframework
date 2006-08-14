package org.araneaframework.servlet.util;

import java.util.HashMap;
import java.util.Map;

import org.araneaframework.OutputData;

/**
 * Utility class for setting system form's fields.
 * 
 * @author Toomas Römer <toomas@webmedia.ee>
 */
public class ClientStateUtil {
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
