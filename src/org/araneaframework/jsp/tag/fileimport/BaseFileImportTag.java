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

package org.araneaframework.jsp.tag.fileimport;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.BaseTag;

/**
 * 
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public abstract class BaseFileImportTag extends BaseTag {

  public static final String DEFAULT_GROUP_NAME = "all";
  public static final String GROUP_CSS_SUFFIX = "_css";
  public static final String GROUP_JS_SUFFIX = "_js";

    protected String includeGroupName;
	protected String includeFileName;

	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "The name of the group of files that should get included."
	 */
	public void setGroup(String group) throws JspException {
		this.includeGroupName = (String) evaluate("group", group, String.class);
	}
	
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "The name of the file that should get included."
	 */
	public void setFile(String file) throws JspException {
		this.includeFileName = (String) evaluate("file", file, String.class);
	}
		
	public int doEndTag(Writer out) throws Exception {
		return EVAL_PAGE;
	}
	
	protected abstract void writeContent(Writer out, String key) throws Exception;
}
