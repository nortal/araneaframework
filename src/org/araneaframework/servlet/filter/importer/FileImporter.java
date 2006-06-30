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

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.araneaframework.servlet.filter.StandardServletFileImportService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class FileImporter {	
	
	/**
	 * Given a filename, returns the string that can be used for importing via the
	 * file importing service {@link StandardServletFileImportService}.
	 * @param fileName
	 */
	public final static String getImportString(String fileName) {
		StringBuffer sb = new StringBuffer("/" + StandardServletFileImportService.FILE_IMPORTER_NAME + "/");
		sb.append(fileName);
	
		return sb.toString();
	}
	
	public final static String getImportString(String fileName, ServletRequest req, ServletResponse res) {
		HttpServletResponse hres = (HttpServletResponse) res;
		HttpServletRequest hreq = (HttpServletRequest) req;
		StringBuffer url = hreq.getRequestURL();
		
		/* XXX
		 * When using jsessionid for session tracking we start getting weird url from
		 * getRequestURL(). They are in the form of xxx://host/path;jsessionid=abcd. I'd
		 * expect them as parameters separated by & and starting with ?. Right now i'm stripping
		 * the url of the jsessionid and it gets added with the encodeURL again. 
		 */
		int index = url.indexOf(";");
		if (index != -1) {
			url = new StringBuffer(url.substring(0, url.indexOf(";")));
		}
		
		return hres.encodeURL(url
			.append("/"+StandardServletFileImportService.FILE_IMPORTER_NAME + "/")
			.append(fileName).toString());
	}
}
