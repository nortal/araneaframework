package org.araneaframework.framework.filter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.araneaframework.AuditContext;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.http.HttpOutputData;

public class StandardAuditFilterService extends BaseFilterService implements AuditContext {
  private List auditRecords = new ArrayList();
  
  protected Environment getChildEnvironment() {
    return new StandardEnvironment(getEnvironment(), AuditContext.class, this);
  }
  
  protected void action(Path path, InputData input, OutputData output) throws Exception {
    HttpInputData httpInput = (HttpInputData) input;
    if (httpInput.getPath() != null && httpInput.getPath().startsWith("/audit")) {
      HttpOutputData httpOutput = (HttpOutputData) output;
      PrintWriter out = httpOutput.getWriter();
      
      out.println("<html>"); {
        out.println("<body>"); {
          out.println("<table border=\"1\" cellspacing=\"0\" bordercolor=\"black\">"); {
            for (Iterator i = auditRecords.iterator(); i.hasNext();) {
              AuditRecord arec = (AuditRecord) i.next();
              out.println("<tr>"); {
                out.println("<td>"); {
                  out.println(arec.toString());
                }out.println("</td>");                             
              }out.println("</tr>");             
            }
          }out.println("</body>");
          
        }out.println("</body>");
      } out.println("</html>");
    }
    else
      super.action(path, input, output);
  }
  
  public void add(AuditRecord record) {
    auditRecords.add(record);
  }
  
  public List getAll() {
    return Collections.unmodifiableList(auditRecords);
  }
}
