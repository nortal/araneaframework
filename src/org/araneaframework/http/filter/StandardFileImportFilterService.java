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
package org.araneaframework.http.filter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.AraneaRuntimeException;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.extension.ExternalResource;
import org.araneaframework.http.extension.ExternalResourceInitializer;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.http.util.URLUtil;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
*/
public class StandardFileImportFilterService  extends BaseFilterService {
	private static final Logger log = Logger.getLogger(StandardFileImportFilterService.class);
	private static boolean isInitialized = false;
	private static ExternalResource resources;
	private long cacheHoldingTime = 3600000;

	public static final String IMPORTER_FILE_NAME = "FileImporterFileName";
	public static final String IMPORTER_GROUP_NAME = "FileImporterGroupName";
	public static final String OVERRIDE_PREFIX = "override";
	public static final String FILE_IMPORTER_NAME = "fileimporter";
	
	synchronized static void initialize(ServletContext context) {
		if (!isInitialized) {
			ExternalResourceInitializer initializer = new ExternalResourceInitializer();
			resources = initializer.getResources(context);
			isInitialized = true;
		}
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		if (!isInitialized) {
			ServletConfig config = (ServletConfig) getEnvironment().getEntry(ServletConfig.class);
			initialize(config.getServletContext());
		}
    
    String uri = URLUtil.normalizeURI(((HttpInputData) input).getPath());
    
    if (uri == null || 
        URLUtil.splitURI(uri).length == 0 || 
        !URLUtil.splitURI(uri)[0].equals(FILE_IMPORTER_NAME)) {        
      childService._getService().action(path, input, output);
      return;
    }

		String fileName = (String)input.getGlobalData().get(IMPORTER_FILE_NAME);
		String groupName = (String)input.getGlobalData().get(IMPORTER_GROUP_NAME);
		
		if (fileName == null) {
			fileName = uri.substring(FILE_IMPORTER_NAME.length() + 1);
			
			if (fileName.indexOf(".") == -1) {
				groupName = fileName;
				fileName = null;
			}
		}
		else if (groupName == null) {
			groupName = fileName;
		}

		HttpServletResponse response = ServletUtil.getResponse(output);
	
		List filesToLoad = new ArrayList();
		OutputStream out = response.getOutputStream();
		try {
			if (fileName != null) {
				if (resources.isAllowedFile(fileName)) {
					setHeaders(response, resources.getContentType(fileName));
					filesToLoad.add(fileName);
					loadFiles(filesToLoad, out);
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
					log.warn("Unexistent group specified for file importing, "+groupName);
					throw new AraneaFileNotFoundException();
				}
			}	
		}
		catch (AraneaFileNotFoundException e) {
			String notFoundName = fileName == null ? groupName : fileName;
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imported file or group '" + notFoundName + "' not found.");
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
			
			FileInputStream fileInputStream = null;
			// fallback to the filesystem
			if (fileURL == null) {
				try {
					fileInputStream = new FileInputStream(fileName);
				}
				catch (FileNotFoundException e) {
					// not being able to load results in AraneaFileNotFoundException, see below
				}
				catch (SecurityException e) {
					// not being able to load results in AraneaFileNotFoundException, see below
				}
			}
			
			if (fileURL != null || fileInputStream != null) {
				InputStream inputStream = null;
				
				if (fileInputStream != null) {
					inputStream = fileInputStream;
				} 
				else {
					inputStream = fileURL.openStream();
				}
				
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
				throw new AraneaFileNotFoundException();
			}
		}
	}
  
  private class AraneaFileNotFoundException extends AraneaRuntimeException {
    public AraneaFileNotFoundException() {
      super();
    }
  }
}
