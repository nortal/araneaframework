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

package org.araneaframework.servlet.filter.importer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class FileImportFactory {
	private static final Logger log = Logger.getLogger(FileImportFactory.class);
	private static final Map typeToClass = new HashMap();
	
	static {
		typeToClass.put(CssFileImporter.TYPE, CssFileImporter.class.getName());
		typeToClass.put(JsFileImporter.TYPE, JsFileImporter.class.getName());
		typeToClass.put(ImageFileImporter.TYPE, ImageFileImporter.class.getName());
	}
	
	public static FileImporter createFileImporter(String type) {
		String className = (String)typeToClass.get(type);

		if (className == null)
			return null;
		
		try {
			Class clazz = Class.forName(className);
			FileImporter fileImporter = (FileImporter) clazz.newInstance();
			return fileImporter;
		}
		catch (ClassNotFoundException e) {
			log.debug(e);
			return null;
		}
		catch (InstantiationException e) {
			log.debug(e);
			return null;
		}
		catch (IllegalAccessException e) {
			log.debug(e);
			return null;
		}
		
	}
}
