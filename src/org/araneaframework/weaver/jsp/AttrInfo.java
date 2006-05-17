/**
 * 
 */
package org.araneaframework.weaver.jsp;

import java.io.Serializable;
import org.w3c.dom.Element;

public class AttrInfo implements Serializable {
  protected String name;

  protected String type;

  public static AttrInfo readAttrInfo(Element attributeEl) {
    AttrInfo attr = new AttrInfo();

    attr.name = ((Element) attributeEl.getElementsByTagName("name").item(0)).getFirstChild().getNodeValue();
    if (attributeEl.getElementsByTagName("type").getLength() == 0)
      attr.type = "java.lang.String";
    else
      attr.type = ((Element) attributeEl.getElementsByTagName("type").item(0)).getFirstChild().getNodeValue();

    return attr;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }
}