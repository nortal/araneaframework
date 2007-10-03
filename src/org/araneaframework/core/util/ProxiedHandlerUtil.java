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

package org.araneaframework.core.util;

import java.lang.reflect.Method;
import org.araneaframework.core.ProxyActionListener;
import org.araneaframework.core.ProxyEventListener;

/**
 * Utility class for accessing methods that will be invocation targets of {@link ProxyEventListener} and
 * {@link ProxyActionListener}.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @since 1.1
 */
public abstract class ProxiedHandlerUtil {
	private static final Class[] EMTPY_CLASS_ARRAY = new Class[] {};

	public static final String EVENT_HANDLER_PREFIX = "handleEvent";
	public static final String ACTION_HANDLER_PREFIX = "handleAction";
	
	private ProxiedHandlerUtil() {}
	
	public static Method getEventHandler(String eventId, Object eventTarget) throws SecurityException, NoSuchMethodException {
		return getEventHandler(eventId, eventTarget, EMTPY_CLASS_ARRAY);
	}

	public static Method getEventHandler(String eventId, Object eventTarget, Class [] params) throws SecurityException, NoSuchMethodException {
		return getHandler(EVENT_HANDLER_PREFIX, eventId, eventTarget, params);
	}

	public static Method getActionHandler(String actionId, Object eventTarget) throws SecurityException, NoSuchMethodException {
		return getActionHandler(actionId, eventTarget, EMTPY_CLASS_ARRAY);
	}

	public static Method getActionHandler(String eventId, Object eventTarget, Class [] params) throws SecurityException, NoSuchMethodException {
        return getHandler(ACTION_HANDLER_PREFIX, eventId, eventTarget, params);
	}

	private static Method getHandler(String handlerPrefix, String eventId, Object eventTarget, Class [] params) throws SecurityException, NoSuchMethodException {
		String eventHandlerName = handlerPrefix + eventId.substring(0, 1).toUpperCase() + eventId.substring(1);
		Method result = null;
		try {
			result = eventTarget.getClass().getDeclaredMethod(eventHandlerName, params);
		} catch (NoSuchMethodException e) {
			result = eventTarget.getClass().getMethod(eventHandlerName, params);
		}
		if (!result.isAccessible()) result.setAccessible(true);
		return result;
	}
}