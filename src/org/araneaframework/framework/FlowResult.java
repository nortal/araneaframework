package org.araneaframework.framework;

public class FlowResult {
  
  static ThreadLocal flowResult = new ThreadLocal();
  
  public FlowResult(Object result) {
    FlowResult.flowResult.set(result);
  }

  public FlowResult onFinish(FlowResultClosure closure) {
    return this;
  }
  
  public FlowResult onCancel() {
    return this;
  }
}
