/**
 * 
 */
package org.araneaframework.weaver;

import org.w3c.dom.Element;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public abstract class BaseWeaverFactory implements WeaverFactory {
  public WeaverElementBody buildBody(Element el) {
    return null;
  }
}
