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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A class to hold some information about a tag read from the tag library (TLD).
 * Provides an easy way to create an instance of it by giving an XML element
 * <code>tag</code> to the {@link #readTagInfo(Element)} method.
 */
public class TagInfo implements Serializable {

  /**
   * The name of the tag as defined in the class library.
   */
  protected String tagName;

  /**
   * The full class name of the tag.
   */
  protected String tagClassName;

  /**
   * Gets the class name of the tag.
   * 
   * @return The class name of the tag.
   */
  public String getTagClassName() {
    return tagClassName;
  }

  /**
   * Sets the class name of the tag.
   * 
   * @param tagClassName The class name of the tag.
   */
  public void setTagClassName(String tagClassName) {
    this.tagClassName = tagClassName;
  }

  /**
   * Gets the name of the tag as defined in the tag library.
   * 
   * @return The name of the tag.
   */
  public String getTagName() {
    return tagName;
  }

  /**
   * Sets the name of the tag as defined in the tag library.
   * 
   * @param tagName The name of the tag.
   */
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  /**
   * Reads tag information from given XML element <code>tag</code> of a tag
   * library.
   * 
   * @param tagElement An XML element named <code>tag</code>.
   * @return An instance of this class containing the read information.
   */
  public static TagInfo readTagInfo(Element tagElement) {
    TagInfo tag = new TagInfo();

    NodeList nameElements = tagElement.getElementsByTagName("name");
    Element nameElement = (Element) nameElements.item(0);
    tag.setTagName(nameElement.getFirstChild().getNodeValue());

    NodeList classElements = tagElement.getElementsByTagName("tag-class");
    Element classElement = (Element) classElements.item(0);
    tag.setTagClassName(classElement.getFirstChild().getNodeValue());

    return tag;
  }

}
