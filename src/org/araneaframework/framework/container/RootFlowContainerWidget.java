package org.araneaframework.framework.container;

import java.util.Map;
import org.araneaframework.framework.RootFlowContext;

public class RootFlowContainerWidget extends StandardFlowContainerWidget implements RootFlowContext {
  protected void putLocalEnvironmentEntries(Map nestedEnvironmentEntries) {
    super.putLocalEnvironmentEntries(nestedEnvironmentEntries);
    nestedEnvironmentEntries.put(RootFlowContext.class, this);
  }
}
