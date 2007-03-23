package org.araneaframework.framework;

import java.io.Serializable;
import java.util.Map;

/**
 * Stores system form fields that will be written out in &lt;ui:systemForm&gt; tag.
 * 
 * @author Alar Kvell (alar@araneaframework.org)
 */
public interface SystemFormContext extends Serializable {

  /**
   * Add a system form field.
   */
  void addField(String key, String value);
  
  /**
   * Get all system form fields. Both keys and values of the map are Strings. If
   * no fields are present, returns an empty map.
   */
  Map getFields();
}
