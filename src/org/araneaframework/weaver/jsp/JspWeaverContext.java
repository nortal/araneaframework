package org.araneaframework.weaver.jsp;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author Jevgeni Kabanov (ekabanov@webmedia.ee)
 *
 */
public interface JspWeaverContext {
  PageContext getPageContext();
  
  Tag getParentTag();
  void pushParentTag(Tag parent);
  void popParentTag();
}
