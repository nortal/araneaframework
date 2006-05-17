/**
 * 
 */
package org.araneaframework.weaver.jsp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TagInfo implements Serializable {
  protected String tagName;

  protected String tagClassName;

  protected Map attributes = new HashMap();

  public String getTagClassName() {
    return tagClassName;
  }

  public String getTagName() {
    return tagName;
  }

  public static TagInfo readTagInfo(Element tagElement) {
    TagInfo tag = new TagInfo();

    NodeList nameElements = tagElement.getElementsByTagName("name");
    Element nameElement = (Element) nameElements.item(0);
    tag.tagName = nameElement.getFirstChild().getNodeValue();

    NodeList classElements = tagElement.getElementsByTagName("tag-class");
    Element classElement = (Element) classElements.item(0);
    tag.tagClassName = classElement.getFirstChild().getNodeValue();

    NodeList attrElements = tagElement.getElementsByTagName("attribute");

    for (int j = 0; j < attrElements.getLength(); j++) {
      Element attributeEl = (Element) attrElements.item(j);
      AttrInfo attrInfo = AttrInfo.readAttrInfo(attributeEl);
      tag.attributes.put(attrInfo.getName(), attrInfo);
    }

    return tag;
  }

  public Map getAttributes() {
    return attributes;
  }
}