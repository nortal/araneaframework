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

package org.araneaframework.backend.util;

import org.apache.commons.lang.exception.NestableRuntimeException;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class BeanUtil {
	public static Object newInstance(Class voClass) {
		Object result;
		try {
			result = voClass.newInstance();
		}
		catch (InstantiationException e) {
			throw new NestableRuntimeException("Could not create an instance of class '" + voClass + "'", e);
		}
		catch (IllegalAccessException e) {
			throw new NestableRuntimeException("Could not create an instance of class '" + voClass + "'", e);
		}
		return result;
	}
	
	/**
	 * Returns whether the given object type is a Value Object type.
	 * @param objectType object type.
	 * @return whether the given object type is a Value Object type.
	 */
	public static boolean isBean(Class objectType) {
		ConstribBeanMapper beanMapper = new ConstribBeanMapper(objectType);
		return beanMapper.getBeanFields().size() != 0;
	}	
}
