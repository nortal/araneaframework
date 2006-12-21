package org.araneaframework.example.main.web.popups;

import org.araneaframework.Service;

public interface ClientSideReturnService extends Service {
  public void setResult(Object returnValue);
  public Object getResult();
}
