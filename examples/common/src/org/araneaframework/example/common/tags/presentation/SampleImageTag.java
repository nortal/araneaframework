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

package org.araneaframework.example.common.tags.presentation;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.jsp.JspException;
import org.araneaframework.http.util.FileImportUtil;
import org.araneaframework.jsp.tag.presentation.BaseImageHtmlTag;
/**
 * SAMPLE image tag.
 * 
 * @author Oleg Mürk
 * 
 * @jsp.tag
 *   name = "image"
 *   body-content = "JSP"
 */
public class SampleImageTag extends BaseImageHtmlTag {
	/**
	 * Static method to write out image with given code with default style.
	 */
	public  void writeImage(Writer out, String code) throws IOException {
		writeImage(out, code, "aranea-image", getStyleClass(), null);
	}
  
	/**
	 * @jsp.attribute
	 *   type = "java.lang.String"
	 *   required = "false"
	 *   description = "Image code." 
	 */
	public void setCode(String code) throws JspException {
		super.setCode(code);
	}
  
	public void writeImageLocal(Writer out, String src, String width, String height, String alt, String styleClass, String title) throws IOException {
		String url = FileImportUtil.getImportString(src, pageContext.getRequest());
		writeImage(out, url, width, height, alt, styleClass, title);
	}

	protected Info getImageInfo(String code) {
		return (Info)imageInfo.get(code);
	} 
  
	/**
	 * Map: String(image code) -> Info
	 */
	protected static final Map imageInfo = new HashMap();
	//XXX: this is bullshit
	static {
		imageInfo.put("add",             new Info("gfx/ico_add3.gif",      "30",   "16"));
	    imageInfo.put("old",             new Info("gfx/ico_old.gif",       "32",   "32"));
	    imageInfo.put("delete",          new Info("gfx/ico_delete.gif",    "23",   "16"));
	    imageInfo.put("downArrow",       new Info("gfx/dot14.gif",         "9",    "7"));
	    imageInfo.put("flag",            new Info("gfx/ico_flag.gif",    "12",   "12"));
	    imageInfo.put("edit2",           new Info("gfx/ico_edit2.gif",     "30",   "16"));
	    imageInfo.put("triangle",        new Info("gfx/dot05.gif",         "30",   "9"));
	    imageInfo.put("folder",          new Info("gfx/ico_folder.gif",    "25",   "15"));
	    imageInfo.put("clients",         new Info("gfx/ico_myclients.gif", "34",   "22"));
	    imageInfo.put("help",            new Info("gfx/ico_help3.gif",     "17",   "16"));   
	    imageInfo.put("feedback",        new Info("gfx/ico_info.gif",      null,   null));
	    imageInfo.put("projects",        new Info("gfx/ico_projects.gif",  "34",   "22"));
	    imageInfo.put("error",           new Info("gfx/ico_error.gif",     "17",   "17"));
	    imageInfo.put("forward",         new Info("gfx/ico_forward.gif",   "25",   "15"));
	    imageInfo.put("print",           new Info("gfx/ico_print.gif",     "30",   "16"));
	    imageInfo.put("back",            new Info("gfx/ico_back.gif",      null,   null));
	    imageInfo.put("back2",           new Info("gfx/ico_back2.gif",     "25",   "15"));    
	    imageInfo.put("buttonChange",    new Info("gfx/butt_change3.gif",    null,  null));
	    imageInfo.put("buttonDelete",    new Info("gfx/butt_delete2.gif",  null,  null));
	    imageInfo.put("buttonAdd",       new Info("gfx/butt_add.gif",    null,   null));
	    imageInfo.put("buttonSearch",    new Info("gfx/butt_search.gif",     null,   null));
	    imageInfo.put("calendar",        new Info("gfx/ico_calendar.gif",  null, null));
	}
}
