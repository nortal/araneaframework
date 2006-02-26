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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TagInfo implements Serializable {
  protected String tagName;
  protected String tagClassName;
  
  public String getTagClassName() {
    return tagClassName;
  }
  public void setTagClassName(String tagClassName) {
    this.tagClassName = tagClassName;
  }
  public String getTagName() {
    return tagName;
  }
  public void setTagName(String tagName) {
    this.tagName = tagName;
  }
  
  public static Map makeTagMapping(Element tldElement) {
    Map result = new HashMap();
     
    NodeList tagElements = tldElement.getElementsByTagName("tag");
       
     for(int i = 0; i < tagElements.getLength(); i++) {
       Element tagElement = (Element) tagElements.item(i);
       
       TagInfo tag = new TagInfo();
       
       NodeList nameElements =  tagElement.getElementsByTagName("name");
       Element nameElement = (Element) nameElements.item(0);
       tag.setTagName(nameElement.getFirstChild().getNodeValue());
       
       NodeList classElements =  tagElement.getElementsByTagName("tag-class");
       Element classElement = (Element) classElements.item(0);
       tag.setTagClassName(classElement.getFirstChild().getNodeValue());  

       result.put(tag.getTagName(), tag);
     }  
     
     return result;  
   }
}
