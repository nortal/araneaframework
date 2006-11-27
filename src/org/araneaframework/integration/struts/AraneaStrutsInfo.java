package org.araneaframework.integration.struts;

import java.io.Serializable;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;
import org.araneaframework.framework.TransactionContext;
import org.araneaframework.framework.container.StandardContainerWidget;

public class AraneaStrutsInfo implements Serializable {
  private static final String ARANEA_INFO_PARAMETER = "araInfo";
  private static final String DELIMITER = ":";
  
  private String containerPath;
  private String topServiceId;
  private String threadServiceId;
  private String widgetEventPath;
  private String transactionId;
  
  public AraneaStrutsInfo(String containerPath, String topServiceId, String threadServiceId, String widgetEventPath, String transactionId) {
    this.containerPath = containerPath;
    this.topServiceId = topServiceId;
    this.threadServiceId = threadServiceId;
    this.widgetEventPath = widgetEventPath;
    this.transactionId = transactionId;    
  }
  
  public String encode() {
    return new StringBuffer(ARANEA_INFO_PARAMETER).append('=').append(containerPath.replace('/', '.'))
    .append(DELIMITER).append(topServiceId)
    .append(DELIMITER).append(threadServiceId)
    .append(DELIMITER).append(widgetEventPath)
    .append(DELIMITER).append(transactionId).toString();
  }
  
  public String toQuery() {
    return new StringBuffer(containerPath.replace('.', '/')).append('?')
      .append(TopServiceContext.TOP_SERVICE_KEY).append('=').append(topServiceId).append('&')
      .append(ThreadContext.THREAD_SERVICE_KEY).append('=').append(threadServiceId).append('&')
      .append(StandardContainerWidget.EVENT_PATH_KEY).append('=').append(widgetEventPath).append('&')
      .append(StandardContainerWidget.EVENT_HANDLER_ID_KEY).append('=').append("include").append('&')
      .append(TransactionContext.TRANSACTION_ID_KEY).append('=').append(transactionId).toString();
  }
  
  public static boolean containsInfo(HttpServletRequest req) {
    return req.getParameter(ARANEA_INFO_PARAMETER) != null;
  }
  
  public static AraneaStrutsInfo decode(HttpServletRequest req) {
    Assert.isTrue(containsInfo(req), "Encoded info must correspond to the format!");
    
    StringTokenizer tokens = new StringTokenizer(req.getParameter(ARANEA_INFO_PARAMETER), DELIMITER);

    String containerPath = tokens.nextToken();
    String topServiceId = tokens.nextToken();
    String threadServiceId = tokens.nextToken();
    String widgetEventPath = tokens.nextToken();
    String transactionId = tokens.nextToken();
    
    return new AraneaStrutsInfo(containerPath, topServiceId, threadServiceId, widgetEventPath, transactionId);
  }
}
