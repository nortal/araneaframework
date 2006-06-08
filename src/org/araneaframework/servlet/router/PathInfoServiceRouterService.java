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

package org.araneaframework.servlet.router;

import java.util.HashMap;
import java.util.Map;

import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.router.BaseServiceRouterService;
import org.araneaframework.servlet.PathInfoServiceContext;
import org.araneaframework.servlet.ServletInputData;

/**
 * @author "Jevgeni Kabanov" <ekabanov@webmedia.ee>
 */
public class PathInfoServiceRouterService extends BaseServiceRouterService {
	public static String PATH_ARGUMENT = "pathInfo";
	
	protected Object getServiceId(InputData input) throws Exception {
		return getPathInfo(input)[0];
	}
  
	protected Object getServiceKey() throws Exception {
		return "pathInfoServiceId";
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
	    output.pushAttribute(PATH_ARGUMENT, getPathInfo(input)[1]);
	    
	    try {
	      super.action(path, input, output);
	    }
	    finally {
	      output.popAttribute(PATH_ARGUMENT);
	    }
	}
	
	private static String[] getPathInfo(InputData input) {
		String serviceId  = null;
		String pathInfo = "";
		
		String path = ((ServletInputData) input).getRequest().getPathInfo();
		if (path != null) {
			// lose the first slash
			if (path.indexOf("/") != -1)
				path = path.substring(1);
			
			
			int index = path.indexOf("/");
			// we have a second slash
			if (index != -1) {
				// not interested in the first slash
				pathInfo = path.substring(index+1);
				serviceId = path.substring(0, index);
			}
			else {
				serviceId = path;
			}
		}
		
		return new String[]{serviceId, pathInfo};
	}
  
	protected Environment getChildEnvironment(Object serviceId) throws Exception {
		Map entries = new HashMap();    
		entries.put(PathInfoServiceContext.class, new ServiceRouterContextImpl(serviceId));
		return new StandardEnvironment(super.getChildEnvironment(serviceId), entries);
	}
  
	private class ServiceRouterContextImpl extends BaseServiceRouterService.ServiceRouterContextImpl implements PathInfoServiceContext {
		protected ServiceRouterContextImpl(Object serviceId) {
			super(serviceId);
		}
	}
}
