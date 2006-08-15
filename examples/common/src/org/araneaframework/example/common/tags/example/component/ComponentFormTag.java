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

package org.araneaframework.example.common.tags.example.component;

import org.araneaframework.jsp.tag.layout.LayoutTag;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @jsp.tag
 *   name = "componentForm"
 *   body-content = "JSP"
 */
public class ComponentFormTag extends LayoutTag {
  public final static String COMPONENT_FORM_STYLE_CLASS = "form";

  {
    styleClass = ComponentFormTag.COMPONENT_FORM_STYLE_CLASS;
  }
}
