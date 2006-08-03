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

package org.araneaframework.http.util;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.araneaframework.http.filter.StandardFileImportFilterService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract  class FileImportUtil {	
	
	/**
	 * Given a filename, returns the string that can be used for importing via the
	 * file importing service {@link StandardFileImportFilterService}.
	 * @param fileName
	 */
	public final static String getImportString(String fileName) {
		StringBuffer sb = new StringBuffer("/" + StandardFileImportFilterService.FILE_IMPORTER_NAME + "/");
		sb.append(fileName);
	
		return sb.toString();
	}
	
	public final static String getImportString(String fileName, ServletRequest req, ServletResponse res) {
		HttpServletResponse hres = (HttpServletResponse) res;
		HttpServletRequest hreq = (HttpServletRequest) req;
    
    StringBuffer url = new StringBuffer();
    url.append(hreq.getScheme());
    url.append("://");
    url.append(hreq.getServerName());    
    url.append(":");
    url.append(hreq.getServerPort());
    url.append(hreq.getContextPath());
    url.append(hreq.getServletPath());
    url.append("/");
    url.append(StandardFileImportFilterService.FILE_IMPORTER_NAME);
    url.append("/");
    url.append(fileName);
		
		return hres.encodeURL(url.toString());
	}
}
