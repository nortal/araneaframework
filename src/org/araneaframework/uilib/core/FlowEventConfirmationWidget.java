package org.araneaframework.uilib.core;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.araneaframework.Component;
import org.araneaframework.OutputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.http.util.EnvironmentUtil;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FlowEventConfirmationWidget extends BaseApplicationWidget {
  private static final long serialVersionUID = 1L;
  private String message;

  public FlowEventConfirmationWidget(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }

  protected void render(OutputData output) throws Exception {
    HttpServletResponse response = ServletUtil.getResponse(output);
    PrintWriter out = response.getWriter();
    
    out.flush();
    
    JspUtil.writeStartTag(out, "script");
    out.write("_ap.addClientLoadEvent(");
    out.write("function() {");
    JspUtil.writeEscapedAttribute(out, getEventConfirmScript());
    out.write("} );\n");
    JspUtil.writeEndTag(out, "script");
    
    out.flush();
  }
  
  protected String getEventConfirmScript() {
    Component fc = (Component) EnvironmentUtil.requireFlowContext(getEnvironment());
    return "Aranea.UI.flowEventConfirm('" + fc.getScope().toString() + "', '" + message + "');";
  }
}
