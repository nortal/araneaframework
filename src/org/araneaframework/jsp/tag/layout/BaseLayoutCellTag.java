package org.araneaframework.jsp.tag.layout;

import java.io.Writer;
import javax.servlet.jsp.JspException;
import org.araneaframework.jsp.tag.PresentationTag;
import org.araneaframework.jsp.tag.layout.support.CellClassProvider;

public abstract class BaseLayoutCellTag extends PresentationTag {
  protected int doStartTag(Writer out) throws Exception {
    requireContextEntry(CellClassProvider.KEY);

    return EVAL_BODY_INCLUDE;
  }

  public String getStyleClass() throws JspException {
    String result = ((CellClassProvider)requireContextEntry(CellClassProvider.KEY)).getCellClass();
    if (styleClass != null) 
      return super.getStyleClass();
    return (result != null && result.length() == 0) ? null : result;
  }
}
