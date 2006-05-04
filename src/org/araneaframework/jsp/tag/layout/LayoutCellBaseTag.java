package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.UiPresentationTag;

public abstract class LayoutCellBaseTag extends UiPresentationTag {
  private CellClassProvider cellProvider;

  protected int doStartTag(Writer out) throws Exception {
    cellProvider = (CellClassProvider)requireContextEntry(LayoutRowInterface.KEY);
    
    return EVAL_BODY_INCLUDE;
  }
  
  public String getStyleClass() throws JspException {
    String result = cellProvider.getCellClass();
    if (styleClass != null) 
      return super.getStyleClass();
    return result;
  }
}
