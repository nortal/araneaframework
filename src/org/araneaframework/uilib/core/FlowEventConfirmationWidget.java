package org.araneaframework.uilib.core;

import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.Closure;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.StandardEventListener;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.jsp.util.JspUtil;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class FlowEventConfirmationWidget extends BaseApplicationWidget {
  private static final long serialVersionUID = 1L;
  private String message;
  private Closure closure;

  public FlowEventConfirmationWidget(String message, Closure navigationEventClosure) {
    this.message = message;
    this.closure = navigationEventClosure;
  }

  public String getMessage() {
    return this.message;
  }
  
  protected void init() throws Exception {
    addEventListener("flowEventConfirmation",  new FlowEventConfirmationEventListener());
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
	  return "Aranea.UI.flowEventConfirm('" + getScope().toString() + "', '" + message + "');";
  }

  private class FlowEventConfirmationEventListener extends StandardEventListener {
	public void processEvent(Object eventId, String eventParam, InputData input) throws Exception {
      System.out.println("Flow event confirmation ::: " + Boolean.valueOf(eventParam));
      closure.execute(null);
	}
  }
}
