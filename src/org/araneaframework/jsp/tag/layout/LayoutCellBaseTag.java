package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import org.araneaframework.jsp.tag.UiPresentationTag;

public abstract class LayoutCellBaseTag extends UiPresentationTag {
  protected int doStartTag(Writer out) throws Exception {
    requireContextEntry(LayoutRowInterface.KEY);
    
    return EVAL_BODY_INCLUDE;
  }
}
