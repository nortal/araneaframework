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
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.filter.importer.FileImportFactory;
import org.araneaframework.servlet.filter.importer.FileImporter;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class StandardServletFileImportFilterService extends BaseFilterService {
  private static final Logger log = Logger.getLogger(StandardServletFileImportFilterService.class);
  
  public static final String IMPORTER_TYPE_KEY = "importerType";
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
  	String type = (String)input.getGlobalData().get(IMPORTER_TYPE_KEY);
  	FileImporter importer = FileImportFactory.createFileImporter(type);
  	
  	if (importer != null) {
  		List filesToLoad = importer.importFiles(input);
  		importer.setHeaders(output);
  	
  		loadFiles(filesToLoad, 
  			((ServletOutputData)output).getResponse().getOutputStream());
  	}
  	else {
  		log.debug("Routing to child.");
  		childService._getService().action(path, input, output);
  	}
  }
  
  private void loadFiles(List files, OutputStream out) throws Exception {
  	for (Iterator iter = files.iterator(); iter.hasNext();) {
			String fileName = (String) iter.next();
			
			ClassLoader loader = getClass().getClassLoader();	
			URL fileURL = loader.getResource(fileName);
			
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
