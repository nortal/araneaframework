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

package org.araneaframework.jsp.tag.basic;

/**
 * Interface of a tag that can have attributes.
 * 
 * @author Oleg Mürk
 */
public interface AttributedTagInterface {

  /**
   * The key for retrieving the target tag from page context.
   */
  public static final String HTML_ELEMENT_KEY = "AttributedTagInterface.HTML_ELEMENT_KEY";

  /**
   * The key for retrieving the map containing tag attributes from page context.
   */
  public static final String ATTRIBUTED_TAG_KEY = "org.araneaframework.jsp.ui.basic.UiAttributedTagInterface.KEY";

  /**
   * The method to add an attribute to the tag marked with this interface. When the tag already has an attribute with
   * the same name, it will get the value, which is provided last.
   * <p>
   * Once an attribute is added, it cannot be removed. However, when an attribute has a null, empty, or blank
   * string value, it won't be rendered.
   * 
   * @param name The name for the attribute to add. 
   * @param value The value for the attribute to add.
   */
  public void addAttribute(String name, String value);
}
