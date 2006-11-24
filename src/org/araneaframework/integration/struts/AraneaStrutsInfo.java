package org.araneaframework.integration.struts;

import java.io.Serializable;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import org.araneaframework.core.Assert;
import org.araneaframework.framework.ThreadContext;
import org.araneaframework.framework.TopServiceContext;

public class AraneaStrutsInfo implements Serializable {
  private static final String ARANEA_INFO_PARAMETER = "araInfo";
  private static final String DELIMITER = ":";
  
  private String containerPath;
  private String topServiceId;
  private String threadServiceId;
  //private String transactionId;
  
  public AraneaStrutsInfo(String containerPath, String topServiceId, String threadServiceId, String transactionId) {
    this.containerPath = containerPath;
    this.topServiceId = topServiceId;
    this.threadServiceId = threadServiceId;
    //this.transactionId = transactionId;
  }
  
  public String encode() {
    return new StringBuffer(ARANEA_INFO_PARAMETER).append('=').append(containerPath.replace('/', '.'))
    .append(DELIMITER).append(topServiceId)
    .append(DELIMITER).append(threadServiceId)
    /*.append(DELIMITER).append(transactionId)*/.toString();
  }
  
  public String toQuery() {
    return new StringBuffer(containerPath.replace('.', '/')).append('?')
      .append(TopServiceContext.TOP_SERVICE_KEY).append('=').append(topServiceId).append('&')
      .append(ThreadContext.THREAD_SERVICE_KEY).append('=').append(threadServiceId)
      /*.append(TransactionContext.TRANSACTION_ID_KEY).append('=').append(transactionId)*/.toString();
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
    String transactionId = null; /*tokens.nextToken();*/
    
    return new AraneaStrutsInfo(containerPath, topServiceId, threadServiceId, transactionId);
  }
}
