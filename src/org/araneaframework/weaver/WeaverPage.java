/**
 * 
 */
package org.araneaframework.weaver;

import org.w3c.dom.Document;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public class WeaverPage {
  private WeaverElement rootElement;
  
  public WeaverPage(WeaverElement rootElement) {
    this.rootElement = rootElement;
  }

  public static WeaverPage buildPage(Document doc, WeaverRegistry reg) {
    WeaverElement rootElement = reg.getElement(doc.getDocumentElement());
    
    
    
    return new WeaverPage(rootElement);
  }
}
