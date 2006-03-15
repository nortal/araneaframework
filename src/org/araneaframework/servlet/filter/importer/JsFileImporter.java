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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.araneaframework.InputData;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class JsFileImporter extends DefaultFileImporter {
	public static final String TYPE = "jsFileImporter";
	
	public static final String INCLUDE_JS_FILE_KEY = "loadJSFile";
	public static final String INCLUDE_JS_KEY = "loadJS";
	public static final String INCLUDE_TMPLT_JS_KEY = "loadTemplateJS";
	
	private static final List jsFiles = new ArrayList();
	static {
		jsFiles.add("js/ui.js");
		jsFiles.add("js/ui-aranea.js");
		jsFiles.add("js/ui-handlers.js");
		jsFiles.add("js/ui-popups.js");
		jsFiles.add("js/aa.js");
		
		jsFiles.add("js/validation/localization.js");
		jsFiles.add("js/validation/clientside-form-validation.js");
		jsFiles.add("js/validation/clientside-datetime-form-validation.js");		
		jsFiles.add("js/validation/jwlf-jsp-ui-events.js");
		jsFiles.add("js/validation/jwlf-jsp-ui-form-validation.js");
		jsFiles.add("js/validation/jwlf-jsp-ui-validation.js");
		jsFiles.add("js/validation/jwlf-jsp-ui.js");
	}
	
	private static final List tmpltJsFiles = new ArrayList();
	static {
		tmpltJsFiles.add("js/validation/template-jwlf-jsp-ui.js");
		tmpltJsFiles.add("js/calendar/calendar.js");
		tmpltJsFiles.add("js/calendar/calendar-en.js");
		tmpltJsFiles.add("js/calendar/calendar-setup.js");
	}
	
	private static final List roJsFiles = Collections.unmodifiableList(jsFiles);
	private static final List roTmpltJsFiles = Collections.unmodifiableList(tmpltJsFiles);
	private static final List allJsFiles = new ArrayList();
	{
		allJsFiles.addAll(roJsFiles);
		allJsFiles.addAll(roTmpltJsFiles);
	}
	
	public List importFiles(InputData input) {
		List rtrn = new ArrayList();
		
		String fileName = (String)input.getGlobalData().get(INCLUDE_JS_FILE_KEY);
		String loadDefault = (String)input.getGlobalData().get(INCLUDE_JS_KEY);
		String loadTmplt = (String)input.getGlobalData().get(INCLUDE_TMPLT_JS_KEY);
		
		
		if (loadDefault != null) {
			return roJsFiles;
		}
		else if (loadTmplt != null) {
			return allJsFiles;
		}
		else if (fileName != null) {
			// have to load only ONE file, lets check if it is
			// in one of the lists
			int index = roJsFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
				return rtrn;
			}
			
			index = roTmpltJsFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
			} 
		}
		return rtrn;
	}
}
