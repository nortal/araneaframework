package org.araneaframework.tests.mock;

import java.util.Enumeration;
import java.util.ResourceBundle;

public class MockResourceBundle extends ResourceBundle {

  @Override
  protected Object handleGetObject(String key) {
    return key;
  }

  @Override
  public Enumeration getKeys() {
    return null;
  }

}
