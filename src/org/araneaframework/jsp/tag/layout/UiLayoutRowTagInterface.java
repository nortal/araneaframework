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

package org.araneaframework.jsp.tag.layout;

import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiStyledTagInterface;


/**
 * UI layout row interface.
 * 
 * @author Oleg Mürk
 */
public interface UiLayoutRowTagInterface extends UiStyledTagInterface {
	public final static String KEY_REQUEST = "org.araneaframework.jsp.ui.layout.UiLayoutRowTagInterface.KEY";

	public void setHeight(String height) throws JspException;
	
	public void setCellClass(String cellClass) throws JspException;
	public String getCellClass() throws JspException;
	
	public UiLayoutCellTagInterface getCellTag(String styleClass) throws JspException;	
}
