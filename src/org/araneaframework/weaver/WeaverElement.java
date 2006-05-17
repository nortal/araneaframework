package org.araneaframework.weaver;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 */
public interface WeaverElement {  
  public void init(WeaverRegistry reg, WeaverElementBody body);
  public void render(WeaverContext ctx);
}