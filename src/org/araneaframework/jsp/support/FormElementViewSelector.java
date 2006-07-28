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

package org.araneaframework.jsp.support;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public class FormElementViewSelector implements Serializable {
	public static final String FORM_ELEMENT_VIEW_SELECTOR_PROPERTY = "form.element.view.selector";
	
  /**
   * Uri of the taglib this form element view belongs to
   */
	public static final String DEFAULT_URI = "http://araneaframework.org/tag-library/standard";
  
	protected String tag;
	protected Map attributes;
  protected String uri;

	public FormElementViewSelector(String tag, Map attributes) {
    this.uri = DEFAULT_URI;
		this.tag = tag;
		this.attributes = attributes == null ? new HashMap() : attributes;
	}
  
  public FormElementViewSelector(String uri, String tag, Map attributes) {
    this.uri = uri;
    this.tag = tag;
    this.attributes = attributes == null ? new HashMap() : attributes;
  }
	
	public Map getAttributes() {
		return attributes;
	}
  
	public String getTag() {
		return tag;
	}
  
  public String getUri() {
    return uri;
  }
}
