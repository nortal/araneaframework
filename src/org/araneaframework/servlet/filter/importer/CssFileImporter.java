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
public class CssFileImporter extends DefaultFileImporter {
	public static final String TYPE = "cssFileImporter";
	
	public static final String INCLUDE_CSS_FILE_KEY = "loadCSSFile";
	public static final String INCLUDE_CSS_KEY = "loadCSS";
	public static final String INCLUDE_TMPLT_CSS_KEY = "loadTemplateCSS";
	
	private static final List tmpltCssFiles = new ArrayList();
	static {
		tmpltCssFiles.add("styles/_styles_global.css");
		tmpltCssFiles.add("styles/_styles_screen.css");
		tmpltCssFiles.add("styles/_styles_print.css");
		tmpltCssFiles.add("styles/_styles_new.css");
		tmpltCssFiles.add("js/calendar/calendar.css");
	}
	
	private static final List cssFiles = new ArrayList();
	static {
	}
	
	private static final List roCssFiles = Collections.unmodifiableList(cssFiles);
	private static final List roTmpltCssFiles = Collections.unmodifiableList(tmpltCssFiles);
	private static final List allCssFiles = new ArrayList();
	{
		allCssFiles.addAll(roCssFiles);
		allCssFiles.addAll(roTmpltCssFiles);
	}
	
	public List importFiles(InputData input) {
		List rtrn = new ArrayList();
		
		String fileName = (String)input.getGlobalData().get(INCLUDE_CSS_FILE_KEY);
		String loadDefault = (String)input.getGlobalData().get(INCLUDE_CSS_KEY);
		String loadTmplt = (String)input.getGlobalData().get(INCLUDE_TMPLT_CSS_KEY);

		if (loadDefault != null) {
			return roCssFiles;
		}
		else if (loadTmplt != null) {
			return allCssFiles;
		}
		else if (fileName != null) {
			// have to load only ONE file, lets check if it is
			// in one of the lists
			int index = tmpltCssFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
				return rtrn;
			}
			
			index = cssFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
			} 
		}
		return rtrn;
	}
}
