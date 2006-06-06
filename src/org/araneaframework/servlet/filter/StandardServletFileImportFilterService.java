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

package org.araneaframework.servlet.filter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.extension.resource.ExternalResource;
import org.araneaframework.extension.resource.ExternalResourceInitializer;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.servlet.ServletOutputData;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServletFileImportFilterService extends BaseFilterService {
  private static final Logger log = Logger.getLogger(StandardServletFileImportFilterService.class);
  private static final ExternalResource resources = (new ExternalResourceInitializer()).getResources();
  private long cacheHoldingTime = 3600000;
  
  
  public static final String IMPORTER_FILE_NAME = "FileImporterFileName";
  public static final String IMPORTER_GROUP_NAME = "FileImporterGroupName";
  public static final String OVERRIDE_PREFIX = "override";
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
  	String fileName = (String)input.getGlobalData().get(IMPORTER_FILE_NAME);
  	String groupName = (String)input.getGlobalData().get(IMPORTER_GROUP_NAME);
  	HttpServletResponse response = ((ServletOutputData)output).getResponse();
  	
  	List filesToLoad = new ArrayList();
  	OutputStream out = ((ServletOutputData)output).getResponse().getOutputStream();
  	
  	if (fileName != null) {
  		if (resources.isAllowedFile(fileName)) {
  			setHeaders(response, resources.getContentType(fileName));
  			filesToLoad.add(fileName);
  			loadFiles(filesToLoad, out);
  		}
  		else {
  			log.warn("Not allowed to import "+fileName+" add it to the allowed list");
  		}
  	}
  	else if (groupName != null) {
  		Map group = resources.getGroupByName(groupName);
  		if (group != null && group.size() > 0) {
  			Map.Entry entry = (Map.Entry)(group.entrySet().iterator().next());
  			setHeaders(response, (String)entry.getValue());
  			filesToLoad.addAll(group.keySet());
  			loadFiles(filesToLoad, out);
  		}
  		else {
  			log.warn("Non-existent group specified for file importing, "+groupName);
  		}
  	}
  	else {
  		log.debug("Routing to child.");
  		childService._getService().action(path, input, output);
  	}
  }
  
  private void setHeaders(HttpServletResponse response, String contentType) {
	  response.setHeader("Cache-Control", "max-age=" + (cacheHoldingTime / 1000));
	  response.setDateHeader ("Expires", System.currentTimeMillis () + cacheHoldingTime);
	  response.setContentType(contentType);
  }
  
  private void loadFiles(List files, OutputStream out) throws Exception {
  	for (Iterator iter = files.iterator(); iter.hasNext();) {
			String fileName = (String) iter.next();
			
			ClassLoader loader = getClass().getClassLoader();
			// first we try load an override
			URL fileURL = loader.getResource(OVERRIDE_PREFIX+"/"+fileName);
			
			if (fileURL == null) {
				// fallback to the original
				fileURL = loader.getResource(fileName);
			}
			
			if (fileURL != null) {
				InputStream inputStream = fileURL.openStream();
				log.debug("Loading "+fileName);
				if (inputStream!=null) {
					int length = 0;
					byte[] bytes = new byte[1024];
					do {
		    		length = inputStream.read(bytes);
		    		if (length==-1) break;
		    		out.write(bytes, 0, length);
					} while (length!=-1);
				}
			}
			else {
				log.warn("Unable to locate resource '"+fileName+"'");
			}
	}
  }
}
