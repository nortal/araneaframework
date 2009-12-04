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

package org.araneaframework.jsp.support;

import java.io.Serializable;
import org.araneaframework.jsp.util.AutomaticFormElementUtil;

/**
 * Represents a tag attribute. Used with {@link AutomaticFormElementUtil} to
 * define tag attributes.
 * 
 * @author Jevgeni Kabanov (ekabanov@araneaframework.org)
 * @see AutomaticFormElementUtil
 * @see TagInfo
 */
public class TagAttr implements Serializable {

  /**
   * The name of the tag attribute.
   */
  protected String name;

  /**
   * The value of the tag attribute.
   */
  protected Object value;

  /**
   * Creates a new tag attribute with given attribute <code>name</code> and
   * <code>value</code>.
   * 
   * @param name The name of the tag attribute.
   * @param value The value of the tag attribute.
   */
  public TagAttr(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Gets the name of the tag attribute.
   * 
   * @return the name of the tag attribute.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the value of the tag attribute.
   * 
   * @return the value of the tag attribute.
   */
  public Object getValue() {
    return value;
  }
}
