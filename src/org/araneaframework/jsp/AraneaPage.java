package org.araneaframework.jsp;

import java.util.HashMap;
import java.util.Map;

public class AraneaPage {
  public static class FunctionMapper {
    static Map functions = new HashMap();
    
    static {
      functions.put("addClientLoadEvent", "acle");
      functions.put("addSystemLoadEvent", "asle");
      functions.put("addSystemUnLoadEvent", "asule");
      
      
    }
  }
}
