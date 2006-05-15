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

package org.araneaframework.jsp.tag.updateRegion;

import java.io.Writer;
import org.araneaframework.jsp.util.UiUtil;

/**
 * 
 * @author Jevgeni kabanov (ekabanov@webmedia.ee)
 * 
 * @jsp.tag
 *   name = "updateRegionRows" 
 *   body-content = "JSP"
 *   description = "See "updateRegion" tag. Use this tag if you need to update several table rows (there is no way to update table cells only)."
 */
public class UiUpdateRegionRowsTag extends UiUpdateRegionBaseTag {

	protected int doStartTag(Writer out) throws Exception {
		super.doStartTag(out);
		
		UiUtil.writeOpenStartTag(out, "tbody");
		UiUtil.writeAttribute(out, "id", fullId);
		UiUtil.writeCloseStartTag(out);
		
		out.write("<!--BEGIN:" + fullId + "-->");
		
		return EVAL_BODY_INCLUDE;
	}

	protected int doEndTag(Writer out) throws Exception {
		out.write("<!--END:" + fullId + "-->");
		
		UiUtil.writeEndTag(out, "tbody");
		
		return super.doEndTag(out);
	}
}
