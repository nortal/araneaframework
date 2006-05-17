/**
 * 
 */
package org.araneaframework.weaver;

import org.w3c.dom.Element;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public interface WeaverRegistry {
  public WeaverElement getElement(Element el);
}
