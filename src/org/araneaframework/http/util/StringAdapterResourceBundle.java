package org.araneaframework.http.util;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Adapter resource bundle that converts all objects to string.
 */
public class StringAdapterResourceBundle extends ResourceBundle {
  ResourceBundle bundle;
  
  public StringAdapterResourceBundle(ResourceBundle bundle) {
    this.bundle = bundle;
  }
  
  protected Object handleGetObject(String key) {
    Object object = bundle.getObject(key);
    return (object != null) ? object.toString() : null;
  } 
  
  public Enumeration getKeys() {
    return bundle.getKeys();
  }
}
