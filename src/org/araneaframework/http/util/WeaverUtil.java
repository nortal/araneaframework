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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.Widget;
import org.araneaframework.http.JspContext;
import org.araneaframework.uilib.core.BaseUIWidget;

/**
 * Utils for JSP Weaver.
 * 
 * <p>
 * Normally all JSP files have to be contained in the servlet context.
 * JSP Weaver also enables to render the files from the class path
 * (<code>/WEB-INF/classes</code> as well as <code>.jar</code> files).
 * 
 * To render a JSP from the class path JSP Weaver Servlet must be enabled
 * and the corresponding file path passed to the method
 * <code>ServletUtil.include()</code> must begin with {@value #CLASSPATH_PREFIX}.
 * </p>
 * 
 * <p>
 * Instead of setting a different view resolver for each {@link BaseUIWidget}
 * one can omit setting the view resolver if JSP Weaver is present in the class path.
 * In such case a JSP is used from the same directory as the Widget itself.
 * E.g. Widget <code>org.araneaframework.example.main.release.demos.DemoContextMenuWidget</code>
 * is paired with the JSP file <code>org/araneaframework/example/main/release/demos/demoContextMenu.jsp</code>.
 * which is located in the <code>/WEB-INF/classes</code> directory.
 * Java compiler copies all non-Java files from the source directory over there
 * which makes developing Widgets and JSP together easier.
 * </p>
 * 
 * <p>
 * If there are Widgets contained in some JAR files they can render their JSP files
 * which are contained in archives just using the same class path resource notation.
 * </p>
 * 
 * @author Rein Raudjärv
 * 
 * @see ServletUtil
 */
public abstract class WeaverUtil {

	private static final Log log = LogFactory.getLog(WeaverUtil.class);
	
	private static final String WEAVER_SERVLET_CLASS = "com.zeroturnaround.jspweaver.JspInterpretingServlet";
	
	/**
	 * JSP file path prefix for the class path resources.
	 */
	public static final String CLASSPATH_PREFIX = "/classpath";
	
	private static final String WIDGET_SUFFIX = "Widget";
	
	private static boolean initialized;
	private static boolean weaverPresent;
	
	/**
	 * Returns the JPS URI corresponding to the Widget.
	 * 
	 * @param widget Widget that is about to render its JSP. 
	 * @return the corresponding JPS URI. 
	 * 
	 * @see #getClasspathJspUri(JspContext, String)
	 */
	public static String getClasspathJspUri(JspContext jspContext, Widget widget) {
		return getClasspathJspUri(jspContext, widget.getClass().getName());
	}

	/**
	 * Returns the JPS URI corresponding to the Widget.
	 * 
	 * @param clazz Class of the Widget that is about to render its JSP. 
	 * @return the corresponding JPS URI.
	 * 
	 * @see #getClasspathJspUri(JspContext, String)
	 */
	public static String getClasspathJspUri(JspContext jspContext, Class clazz) {
		return getClasspathJspUri(jspContext, clazz.getName());
	}
	
	/**
	 * Returns the JPS URI corresponding to the Widget.
	 * 
	 * <p>
	 * E.g. for Widget <code>org.araneaframework.example.main.release.demos.DemoContextMenuWidget</code>
	 * the returned URI is <code>/classpath/org/araneaframework/example/main/release/demos/demoContextMenu.jsp</code>.
	 * </p>
	 * 
	 * <p>
	 * The convention is the following:
	 * <ul>
	 *   <li>JSP file is searched from the class path (the URI begins with {@value #CLASSPATH_PREFIX})</li>
	 *   <li>JSP file is located in the same directory as the Widget</li> 
	 *   <li>JSP file name begins with a lower case letter</li> 
	 *   <li>JSP file name omits the suffix <code>Widget</code></li> 
	 *   <li>JSP file has the extension retrieved by {@link JspContext#getJspExtension()</li> 
	 * </ul>
	 * </p>
	 * 
	 * @param className class name of the Widget that is about to render its JSP. 
	 * @return the corresponding JPS URI. 
	 */
	public static String getClasspathJspUri(JspContext jspContext, String className) {
		return CLASSPATH_PREFIX + getJspUri(jspContext, className);
	}
	
	private static String getJspUri(JspContext jspContext, String className) {
		String subDir = className.substring(0, className.lastIndexOf(".")).replace('.', '/');
		String simpleName = className.substring(className.lastIndexOf(".") + 1);
		
		// Make the first letter lower
		simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
		
		// Remove "Widget" from the end
		if (simpleName.endsWith(WIDGET_SUFFIX)) {
			simpleName = simpleName.substring(0, simpleName.length() - WIDGET_SUFFIX.length());
		}
		
		String path = "/" + subDir + "/" + simpleName + jspContext.getJspExtension();
		
		if (log.isDebugEnabled()) {
			log.debug("Path of the '" + className + "' is '" + path + "'");
		}
		
		return path;
	}
	
	/**
	 * Returns <code>true</code> if the JSP Weaver Servlet is available.
	 * <p>
	 * The corresponding class is searched only at the first call of this method.
	 * 
	 * @return <code>true</code> if the JSP Weaver Servlet is available.
	 */
	public static boolean isWeaverPresent() {
		if (!initialized) {
			try {
				Class.forName(WEAVER_SERVLET_CLASS);
				weaverPresent = true;
			} catch (ClassNotFoundException e) {
				weaverPresent = false;
			}
			initialized = true;
		}
		return weaverPresent;
	}

}
