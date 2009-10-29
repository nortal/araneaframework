/*
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
 */

package org.araneaframework.jsp.tag.uilib.form;				

import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.StyledTagInterface;
import org.araneaframework.jsp.tag.basic.AttributedTagInterface;


/**
 * FormWidget element tag inteface.
 * 
 * @author Oleg Mürk
 */
public interface FormElementTagInterface extends StyledTagInterface, AttributedTagInterface {
	public void setId(String id) throws JspException;
	public void setEvents(String events) throws JspException;	
	public void setValidateOnEvent(String validateOnEvent) throws JspException;	
	public void setTabindex(String tabindex) throws JspException;
	public void setUpdateRegions(String updateRegions) throws JspException;
	public void setGlobalUpdateRegions(String globalUpdateRegions) throws JspException;
}
